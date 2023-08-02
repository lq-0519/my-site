package cn.lq.service.api;

import com.qiniu.storage.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Dragon Wu
 * @since 2022-10-15 16:17
 **/
public interface QiniuOSSService {

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return key 文件存储的路径
     */
    String uploadFile(MultipartFile file);

    /**
     * 删除文件
     *
     * @param key 文件的key, 是originalFileName
     * @return 删除成功返回true, 否则返回false
     */
    Boolean deleteFile(String key);

    /**
     * 获取文件访问的路径
     *
     * @param key 文件存储的相对路径
     * @return 文件的访问路径
     */
    String getFileUrl(String key);

    /**
     * 检查文件是否存在于七牛云
     *
     * @param key 文件的key
     * @return 文件信息，若为null代表不存在
     */
    FileInfo checkFile(String key);
}