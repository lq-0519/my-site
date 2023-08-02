package cn.lq.web.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * https://blog.csdn.net/qq_50909707/article/details/127349094
 *
 * @author Dragon Wu
 * @since 2022-10-16 13:48
 **/
@Configuration
public class QiniuOSSConfig {
    /**
     * 公钥
     */
    @Value("${OSS.accessKey}")
    private String accessKey;
    /**
     * 私钥
     */
    @Value("${OSS.secretKey}")
    private String secretKey;

    /**
     * 构建一个七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qnConfig());
    }

    /**
     * 认证信息实例
     */
    @Bean
    public Auth auth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     * 构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), qnConfig());
    }

    /**
     * 华北机房
     */
    private com.qiniu.storage.Configuration qnConfig() {
        //根据自己机房的位置来选择
        return new com.qiniu.storage.Configuration(Region.huabei());
    }
}