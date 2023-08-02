package cn.lq.service.api.impl;

import cn.lq.common.exception.BusinessException;
import cn.lq.common.exception.ParamInvalidException;
import cn.lq.service.api.QiniuOSSService;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Dragon Wu
 * @since 2022-10-15 16:18
 **/
@Service("qiniuOSSService")
public class QiniuOSSServiceImpl implements QiniuOSSService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuOSSServiceImpl.class);
    @Resource
    private Auth auth;

    @Resource
    private UploadManager uploadManager;

    @Resource
    private BucketManager bucketManager;
    /**
     * 域名
     */
    @Value("${OSS.path}")
    private String path;
    /**
     * 图片允许的扩展名
     */
    @Value("${file.allowed}")
    private String[] allowed;
    /**
     * 存储空间
     */
    @Value("${OSS.bucketName}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) throws RuntimeException {
        try {
            //获取原始文件名
            String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
            //判断文件是否允许上传
            if (!isFileAllowed(originalFileName)) {
                throw new ParamInvalidException("当前文件类型不支持上传");
            }

            byte[] uploadBytes = file.getBytes();
            String upToken = auth.uploadToken(bucketName);
            uploadManager.put(uploadBytes, originalFileName, upToken);
            return originalFileName;
        } catch (Exception e) {
            LOGGER.error("文件上传出错", e);
            throw new BusinessException("文件上传出错", e);
        }
    }

    @Override
    public Boolean deleteFile(String key) throws RuntimeException {
        if (checkFile(key) == null) {
            return true;
        }

        try {
            bucketManager.delete(bucketName, key);
            return true;
        } catch (QiniuException e) {
            throw new BusinessException(String.format("删除文件出现异常, key:%s", key), e);
        }
    }

    @Override
    public String getFileUrl(String key) {
        return "http://" + path + "/" + key;
    }

    @Override
    public FileInfo checkFile(String key) {
        FileInfo fileInfo = null;
        try {
            fileInfo = bucketManager.stat(bucketName, key);
        } catch (QiniuException ignored) {
        }

        return fileInfo;
    }

    /**
     * 判断文件是否被允许上传
     *
     * @param fileName 文件名
     * @return 允许true, 否则false
     */
    private boolean isFileAllowed(String fileName) {
        // 获取后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        for (String allow : allowed) {
            if (allow.equals(suffixName)) {
                return true;
            }
        }

        return false;
    }
}