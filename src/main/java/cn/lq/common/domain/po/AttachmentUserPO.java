package cn.lq.common.domain.po;

import java.io.Serializable;

/**
 * @author winterchen
 * @date 2018/4/29
 */
public class AttachmentUserPO extends BasePO implements Serializable {

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件的地址
     */
    private String fileKey;
    /**
     * 创建人的id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AttachmentUserPO{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileKey='" + fileKey + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
