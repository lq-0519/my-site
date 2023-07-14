package cn.lq.common.utils;

import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.vo.PageVO;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共函数
 * Created by winterchen on 2018/4/30.
 */
@Component
public class Commons {

    /**
     * 网站链接
     */
    public static String site_url() {
        return site_url("/");
    }

    /**
     * 返回网站链接下的全址
     *
     * @param sub 后面追加的地址
     */
    public static String site_url(String sub) {
        return site_option("site_url") + sub;
    }


    /**
     * 文件上传，为文件重新命名
     **/
    public static String getFileRename(String name) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String sdfDate = sdf.format(date);
        int pos = name.lastIndexOf(".");
        String suffix = name.substring(pos);
        return sdfDate + suffix;
    }

    /**
     * 获取网站的描述
     */
    public static String site_desc() {
        return site_option("site_description");
    }

    public static String site_keywords() {
        return site_option("site_keywords");
    }

    /**
     * 获取网站的备案信息
     */
    public static String site_record() {
        return site_option("site_record");
    }

    /**
     * 获取网站标题
     */
    public static String site_title() {
        return site_option("site_title");
    }

    /**
     * 网站配置项
     */
    public static String site_option(String key) {
        return site_option(key, "");
    }

    /**
     * 获取GitHub地址
     */
    public static String social_github() {
        return site_option("social_github");
    }

    /**
     * 获取google网站验证码
     */
    public static String google_site_verification() {
        return site_option("google_site_verification");
    }

    /**
     * 获取百度网站验证码
     */
    public static String baidu_site_verification() {
        return site_option("baidu_site_verification");
    }

    /**
     * 网站配置项
     *
     * @param defalutValue 默认值
     */
    public static String site_option(String key, String defalutValue) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        String str = WebConst.initConfig.get(key);
        if (StringUtils.isNotBlank(str)) {
            return str;
        } else {
            return defalutValue;
        }
    }

    /**
     * 格式化unix时间戳为日期
     */
    @Deprecated
    public static String fmtdate(Integer unixTime) {
        return fmtdate(unixTime, "yyyy-MM-dd");
    }

    /**
     * 格式化unix时间戳为日期
     */
    public static String fmtdate(Date unixTime) {
        return fmtdate(unixTime, "yyyy-MM-dd");
    }

    /**
     * 格式化unix时间戳为日期
     */
    @Deprecated
    public static String fmtdate(Integer unixTime, String patten) {
        if (null != unixTime && StringUtils.isNotBlank(patten)) {
            return DateKit.formatDateByUnixTime(unixTime, patten);
        }
        return "";
    }


    /**
     * 格式化unix时间戳为日期
     */
    public static String fmtdate(Date unixTime, String patten) {
        SimpleDateFormat format = new SimpleDateFormat(patten);
        return unixTime != null ? format.format(unixTime) : "";
    }

    /**
     * 英文格式的日期
     */
    public static String fmtdate_en(Date unixTime) {
        String fmtdate = fmtdate(unixTime, "yyyy,MMM,d");
        String[] dateArr = fmtdate.split(",");
        return "<span>" + dateArr[0] + "</span> " + dateArr[1] + "  " + dateArr[2];
    }


    /**
     * 英文格式的日期-月，日
     */
    public static String fmtdate_en_m(Integer unixTime) {
        return fmtdate(unixTime, "MMM d");
    }

    /**
     * 日期-年
     */
    public static String fmtdate_en_y(Integer unixTime) {
        return fmtdate(unixTime, "yyyy");
    }

    /**
     * 将中文的yyyy年MM月 - > yyyy
     */
    public static String parsedate_zh_y_m(String date) {
        if (StringUtils.isNotBlank(date)) {
            Date d = DateKit.dateFormat(date, "yyyy年MM月");
            return DateKit.dateFormat(d, "yyyy");
        }
        return null;
    }

    /**
     * 字符串转Date
     */
    public static Date fmtdate_date(String date) {
        if (StringUtils.isNotBlank(date)) {
            return DateKit.dateFormat(date, "yyyy年MM月");
        }
        return null;
    }

    /**
     * 根据nuix时间戳获取Date
     */
    public static Date fmtdate_unxtime(Integer nuixTime) {
        if (null != nuixTime) {
            return DateKit.getDateByUnixTime(nuixTime);
        }
        return null;
    }

    /**
     * 获取社交的链接地址
     */
    public static Map<String, String> social() {
        final String prefix = "social_";
        Map<String, String> map = new HashMap<>();
        map.put("csdn", WebConst.initConfig.get(prefix + "csdn"));
        map.put("jianshu", WebConst.initConfig.get(prefix + "jianshu"));
        map.put("resume", WebConst.initConfig.get(prefix + "resume"));
        map.put("weibo", WebConst.initConfig.get(prefix + "weibo"));
        map.put("zhihu", WebConst.initConfig.get(prefix + "zhihu"));
        map.put("github", WebConst.initConfig.get(prefix + "github"));
        map.put("twitter", WebConst.initConfig.get(prefix + "twitter"));
        return map;
    }


    /**
     * An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!
     * <p>
     * 这种格式的字符转换为emoji表情
     */
    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }

    /**
     * 获取随机数
     */
    public static String random(int max, String str) {
        return UUID.random(1, max) + str;
    }

    public static String random(Long seed, int max, String str) {
        if (seed == null) {
            return random(max, str);
        }
        Random random = new Random(seed);
        return random.nextInt(max) + str;
    }

    /**
     * 如果blog没有配图，随机获取一张
     */
    public static String randomBlogPic(Long seed) {
        return "/site/images/blog-images/blog-" + random(seed, 12, ".jpg");
    }

    /**
     * 返回github头像地址
     */
    public static String gravatar(String email) {
        String avatarUrl = "https://github.com/identicons/";
        if (StringUtils.isBlank(email)) {
            email = "user@hanshuai.xin";
        }
        String hash = TaleUtils.MD5encode(email.trim().toLowerCase());
        return avatarUrl + hash + ".png";
    }

    /**
     * 显示文章内容，转换markdown为html
     */
    public static String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            value = value.replace("<!-- more -->", "\r\n");
            return TaleUtils.mdToHtml(value);
        }
        return "";
    }

    /**
     * 返回文章链接地址
     */
    public static String permalink(ContentEsPO contents) {
        return permalink(contents.getId(), contents.getSlug());
    }


    /**
     * 返回文章链接地址
     */
    public static String permalink(Long cid, String slug) {
        return site_url("/article/" + (StringUtils.isNotBlank(slug) ? slug : cid.toString()));
    }

    /**
     * 判断分页中是否有数据
     */
    public static boolean is_empty(PageVO<?> pageVO) {
        return PageUtils.isEmpty(pageVO);
    }

    /**
     * 截取字符串
     */
    public static String substr(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        }
        return str;
    }

    /**
     * 返回作品文章地址
     */
    public static String photoPermalink(Long cid) {
        return site_url("/photo/article/" + cid.toString());
    }

    /**
     * 返回blog文章地址
     */
    public static String blogPermalink(Long contentId) {
        return site_url(String.format("/blog/article?id=%s", contentId));
    }

    /**
     * 返回blog文章地址
     */
    public static String blogPermalink(Long contentId, String content) {
        return site_url(String.format("/blog/article?id=%s&content=%s", contentId, StringUtils.isEmpty(content) ? "" : content));
    }

    /**
     * 获取blog归档地址
     */
    public static String archivePermalink(String date) {
        return site_url("/blog/archives/" + date);
    }


    public static String archiveYearPermalink(String date) {
        return site_url("/blog/archives/year/" + date);
    }

    /**
     * 返回blog分类的地址
     */
    public static String categoriePermalink(String categorie) {
        return site_url("/blog/categories/" + categorie);
    }

    /**
     * 返回blog标签页的地址
     */
    public static String tagPermalink(String tag) {
        return site_url("/blog/tag/" + tag);
    }

    /**
     * 获取文章第一张图片
     */
    public static String show_thumb(String content) {
        content = TaleUtils.mdToHtml(content);
        if (content.contains("<img")) {
            String img = "";
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(content);
            if (m_image.find()) {
                img = img + "," + m_image.group();
                // //匹配src
                Matcher m = Pattern.compile("src\\s*=\\s*\'?\"?(.*?)(\'|\"|>|\\s+)").matcher(img);
                if (m.find()) {
                    return m.group(1);
                }
            }
        }
        return "";
    }

    /**
     * 获取文章中的所有图片
     */
    public static List<String> show_all_thumb(String content) {
        List<String> rs = new LinkedList<>();
        content = TaleUtils.mdToHtml(content);
        if (content.contains("<img")) {
            String img = "";
            String regEx_img = "<[a-zA-Z]+.*?>([\\s\\S]*?)</[a-zA-Z]*>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.MULTILINE);
            Matcher m_image = p_image.matcher(content);
            while (m_image.find()) {
                String data = m_image.group(1).trim();
                if (!"".equals(data) && data.contains("<img")) {
                    // //匹配src
                    Matcher m = Pattern.compile("src\\s*=\\s*\'?\"?(.*?)(\'|\"|>|\\s+)").matcher(data);
                    while (m.find()) {
                        //  if (m.find()) {
                        rs.add(m.group(1));
                    }
                }

            }
        }
        return rs;
    }

    /**
     * 获取文章的文字预览
     */
    public static String show_p(String content) {
        String result = "";
        content = TaleUtils.mdToHtml(content);
        String reg = "<[a-zA-Z]+.*?>([\\s\\S]*?)</[a-zA-Z]*>";

        Pattern p = Pattern.compile(reg, Pattern.MULTILINE);
        content = content.replace("&nbsp;", "");
        Matcher m = p.matcher(content);
        if (m.find()) {
            String data = m.group(1).trim();
            if (!"".equals(data) && !data.contains("<img")) {
                result = data;
            }
        }
        result = result.replace("<img>", "");
        result = result.replace("</img>", "");
        result = result.replace("<p>", "");
        result = result.replace("</p>", "");
        if (result.length() > 20) {
            result = result.substring(0, 20);
        }
        return result;
    }

    /**
     * 获取文章中所有的文字
     */
    public static List<String> show_all_p(String content) {
        List<String> rs = new LinkedList();
        content = TaleUtils.mdToHtml(content);
        String reg = "<[a-zA-Z]+.*?>([\\s\\S]*?)</[a-zA-Z]*>";

        Pattern p = Pattern.compile(reg, Pattern.MULTILINE);
        content = content.replace("&nbsp;", "");
        Matcher m = p.matcher(content);
        while (m.find()) {
            String data = m.group(1).trim();
            if (!"".equals(data) && !data.contains("<img")) {
                data = "<p>" + data + "</p>";
                rs.add(data);
            }
        }
        return rs;
    }

    /**
     * 显示分类
     */
    public static String show_categories(String categories) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(categories)) {
            String[] arr = categories.split(",");
            StringBuilder sbuf = new StringBuilder();
            for (String c : arr) {
                sbuf.append("<a class=\"article-category-link\" href=\"/blog/category/").append(URLEncoder.encode(c, "UTF-8")).append("\">").append(c).append("</a>");
            }
            return sbuf.toString();
        }
        return show_categories("默认分类");
    }

    /**
     * 显示标签
     */
    public static String show_tags(String tags) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(tags)) {
            String[] arr = tags.split(",");
            StringBuilder sbuf = new StringBuilder();
            for (String c : arr) {
                sbuf.append("<li class=\"article-tag-list-item\"><a href=\"/blog/tag/").append(URLEncoder.encode(c, "UTF-8")).append("\">#").append(c).append("</a></li>");
            }
            return sbuf.toString();
        }
        return "";
    }

    /**
     * 截取文章摘要
     *
     * @param value 文章内容
     * @param len   要截取文字的个数
     */
    public static String intro(String value, int len) {
        int pos = value.indexOf("<!--more-->");
        if (pos == 0 || pos == -1) {
            pos = value.indexOf("<!-- more -->");
        }
        if (pos != -1) {
            String html = value.substring(0, pos);
            return TaleUtils.mdToHtml(TaleUtils.mdToHtml(html));
        } else {
            String text = TaleUtils.mdToHtml(TaleUtils.mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len);
            }
            return text;
        }
    }


}
