package cn.lq.common.utils;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.po.UserPO;
import cn.lq.common.exception.BusinessException;
import cn.lq.web.controller.admin.AttachmentController;
import com.vladsch.flexmark.ext.jekyll.tag.JekyllTagExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author winterchen
 * @date 2018/4/30
 */
public class TaleUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaleUtils.class);
    /**
     * 匹配邮箱正则
     */
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern SLUG_REGEX = Pattern.compile("^[A-Za-z0-9_-]{5,100}$", Pattern.CASE_INSENSITIVE);

    private static final MutableDataSet MD_OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), JekyllTagExtension.create(), TocExtension.create(), SimTocExtension.create()))
            .set(TocExtension.LEVELS, 255)
            .set(TocExtension.DIV_CLASS, "toc");

    private static final Parser MD_PARSER = Parser.builder(MD_OPTIONS).build();

    private static final HtmlRenderer MD_RENDERER = HtmlRenderer.builder(MD_OPTIONS).build();
    /**
     * 使用双重检查锁的单例方式需要添加 volatile 关键字
     */
    private static volatile DataSource newDataSource;

    /**
     * 判断是否是邮箱
     */
    public static boolean isEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    /**
     * @param fileName 获取jar外部的文件
     * @return 返回属性
     */
    private static Properties getPropFromFile(String fileName) {
        Properties properties = new Properties();
        try {
//            默认是classPath路径
            InputStream resourceAsStream = new FileInputStream(fileName);
            properties.load(resourceAsStream);
        } catch (BusinessException | IOException e) {
            LOGGER.error("get properties file fail={}", e.getMessage());
        }
        return properties;
    }

    /**
     * md5加密
     *
     * @param source 数据源
     * @return 加密字符串
     */
    public static String MD5encode(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] encode = messageDigest.digest(source.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte anEncode : encode) {
                String hex = Integer.toHexString(0xff & anEncode);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return "";
    }

    /**
     * 获取新的数据源
     */
    public static DataSource getNewDataSource() {
        if (newDataSource == null) {
            synchronized (TaleUtils.class) {
                if (newDataSource == null) {
                    Properties properties = TaleUtils.getPropFromFile("application-default.properties");
                    if (properties.size() == 0) {
                        return newDataSource;
                    }
                    DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
                    managerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
                    managerDataSource.setPassword(properties.getProperty("spring.datasource.password"));
                    String str = "jdbc:mysql://" + properties.getProperty("spring.datasource.url") + "/" + properties.getProperty("spring.datasource.dbname") + "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
                    managerDataSource.setUrl(str);
                    managerDataSource.setUsername(properties.getProperty("spring.datasource.username"));
                    newDataSource = managerDataSource;
                }
            }
        }
        return newDataSource;
    }

    /**
     * 返回当前登录用户
     */
    public static UserPO getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (null == session) {
            return null;
        }
        return (UserPO) session.getAttribute(Constant.LOGIN_SESSION_KEY);
    }


    /**
     * 获取cookie中的用户id
     */
    public static Long getCookieUid(HttpServletRequest request) {
        if (null != request) {
            Cookie cookie = cookieRaw(Constant.USER_IN_COOKIE, request);
            if (cookie != null && cookie.getValue() != null) {
                try {
                    String uid = Tools.deAes(cookie.getValue(), Constant.AES_SALT);
                    return StringUtils.isNotBlank(uid) && NumberUtils.isNumber(uid) ? Long.valueOf(uid) : null;
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    /**
     * 从cookies中获取指定cookie
     *
     * @param name    名称
     * @param request 请求
     * @return cookie
     */
    private static Cookie cookieRaw(String name, HttpServletRequest request) {
        javax.servlet.http.Cookie[] servletCookies = request.getCookies();
        if (servletCookies == null) {
            return null;
        }
        for (javax.servlet.http.Cookie c : servletCookies) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * 设置记住密码cookie
     */
    public static void setCookie(HttpServletResponse response, Long uid) {
        try {
            String val = Tools.enAes(uid.toString(), Constant.AES_SALT);
            boolean isSSL = false;
            Cookie cookie = new Cookie(Constant.USER_IN_COOKIE, val);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30);
            cookie.setSecure(isSSL);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提取html中的文字
     */
    public static String htmlToText(String html) {
        if (StringUtils.isNotBlank(html)) {
            return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        }
        return "";
    }

    /**
     * markdown转换为html
     */
    public static String mdToHtml(String markdown) {
        Node document = MD_PARSER.parse(markdown);
        String content = MD_RENDERER.render(document);
        return Commons.emoji(content);
    }

    /**
     * markdown转换为html
     * 带目录版本
     */
    public static String mdToHtmlWithCategory(String markdown) {
        Node document = MD_PARSER.parse("[TOC]\n" + markdown);
        String content = MD_RENDERER.render(document);
        return Commons.emoji(content);
    }

    /**
     * 退出登录状态
     */
    public static void logout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute(Constant.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(Constant.USER_IN_COOKIE, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 替换HTML脚本
     */
    public static String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\"'][\\s]*javascript:(.*)[\"']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }

    /**
     * 过滤XSS注入
     */
    public static String filterXSS(String value) {
        String cleanValue = null;
        if (value != null) {
            cleanValue = Normalizer.normalize(value, Normalizer.Form.NFD);
            // Avoid null characters
            cleanValue = cleanValue.replaceAll("\0", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid anything in a src='...' type of expression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");
        }
        return cleanValue;
    }

    /**
     * 判断是否是合法路径
     */
    public static boolean isPath(String slug) {
        if (StringUtils.isNotBlank(slug)) {
            if (slug.contains("/") || slug.contains(" ") || slug.contains(".")) {
                return false;
            }
            Matcher matcher = SLUG_REGEX.matcher(slug);
            return matcher.find();
        }
        return false;
    }


    /**
     * 判断文件是否是图片类型
     */
    public static boolean isImage(InputStream imageFile) {
        try {
            Image img = ImageIO.read(imageFile);
            return img != null && img.getWidth(null) > 0 && img.getHeight(null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 随机数
     */
    public static String getRandomNumber(int size) {
        StringBuilder num = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            double a = Math.random() * 9.0D;
            a = Math.ceil(a);
            int randomNum = (new Double(a)).intValue();
            num.append(randomNum);
        }

        return num.toString();
    }

    /**
     * 获取保存文件的位置,jar所在目录的路径
     */
    public static String getUplodFilePath() {
        String path = TaleUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(1);
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int lastIndex = path.lastIndexOf("/") + 1;
        path = path.substring(0, lastIndex);
        File file = new File("");
        return file.getAbsolutePath() + "/";
    }

    public static String getFileKey(String name) {
        String prefix = "/upload/" + DateKit.dateFormat(new Date(), "yyyy/MM");
        if (!new File(AttachmentController.CLASSPATH + prefix).exists()) {
            new File(AttachmentController.CLASSPATH + prefix).mkdirs();
        }

        name = StringUtils.trimToNull(name);
        if (name == null) {
            return prefix + "/" + UUID.UU32() + "." + null;
        } else {
            name = name.replace('\\', '/');
            name = name.substring(name.lastIndexOf("/") + 1);
            int index = name.lastIndexOf(".");
            String ext = null;
            if (index >= 0) {
                ext = StringUtils.trimToNull(name.substring(index + 1));
            }
            return prefix + "/" + UUID.UU32() + "." + ext;
        }
    }
}
