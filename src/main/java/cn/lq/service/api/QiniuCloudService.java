package cn.lq.service.api;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.exception.BusinessException;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author winterchen
 * @date 2018/5/1
 */
@Service
public class QiniuCloudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuCloudService.class);
    /**
     * 七牛云外网访问地址
     */
    @Value("${qiniu.cdn.url}")
    public String QINIU_UPLOAD_SITE;
    @Value("${qiniu.accesskey}")
    private String ACCESS_KEY;
    @Value("${qiniu.serectkey}")
    private String SECRET_KEY;
    /**
     * 仓库
     */
    @Value("${qiniu.bucket}")
    private String BUCKET;

    public String upload(MultipartFile file, String fileName) {

        //构造一个带指定Zone对象的配置类，注意这里需要根据自己的选择的存储区域来选择对应的Zone对象
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response;

            response = uploadManager.put(file.getInputStream(), fileName, upToken, null, null);

            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            LOGGER.error(r.toString());
            throw BusinessException.withErrorCode(Constant.Att.UPLOAD_FILE_FAIL).withErrorMessageArguments(ex.getMessage());
        } catch (IOException e) {
            LOGGER.error("file upload failed", e);
            throw BusinessException.withErrorCode(Constant.Att.UPLOAD_FILE_FAIL).withErrorMessageArguments(e.getMessage());
        }

    }

}