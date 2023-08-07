package cn.lq.common.utils;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.SysConstant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


/**
 * 博客内容推送工具
 *
 * @author liqian477
 */
public class BaiduBlogUploadUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaiduBlogUploadUtils.class);
    private static final String URL = "http://www.1qian.net/blog/article?id=%s&content=";
    /**
     * 推送的token
     */
    private static String bdToken;

    public static void upload(Long contentId) {
        //测试环境不推送
        if (Constant.Env.DEV.equals(SysConstant.env)) {
            return;
        }

        //构建url
        String url = String.format(URL, contentId);
        pushPost(url);
    }


    /**
     * 百度链接实时推送
     *
     * @param postUrl 需要推送的内容链接
     */
    public static void pushPost(String postUrl) {
        String linkSubmitUrl = "http://data.zz.baidu.com/urls?site=www.1qian.net&token=" + bdToken;
        String host = "data.zz.baidu.com";
        //HttpClient
        CloseableHttpClient client = (CloseableHttpClient) wrapClient();
        HttpPost post = new HttpPost(linkSubmitUrl);
        //发送请求参数
        try {
            StringEntity s = new StringEntity(postUrl, "utf-8");
            s.setContentType("application/json");
            post.setEntity(s);
            post.setHeader("Host", host);
            post.setHeader("User-Agent", "curl/7.12.1");
            post.setHeader("Content-Type", "text/plain");
            HttpResponse res = client.execute(post);
            HttpEntity entity = res.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            LOGGER.warn("百度链接实时推送, postUrl:{}, result:{}", postUrl, result);
        } catch (Exception e) {
            LOGGER.error("百度链接实时推送出现异常", e);
        }
    }

    private static HttpClient wrapClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLSv1");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(ctx, new String[]{"TLSv1"}, null, new DefaultHostnameVerifier());
            return HttpClients.custom().setSSLSocketFactory(factory).build();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Value("${bd.search.token}")
    public void setBdToken(String bdToken) {
        BaiduBlogUploadUtils.bdToken = bdToken;
    }
}

