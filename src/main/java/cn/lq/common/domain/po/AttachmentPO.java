package cn.lq.common.domain.po;

/**
 * 网站图片文件相关
 *
 * @author winterchen
 * @date 2018/4/29
 */
public class AttachmentPO extends BasePO {
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
     * userId
     */
    private Long userId;

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

    @Override
    public String toString() {
        return "AttachmentPO{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileKey='" + fileKey + '\'' +
                ", userId=" + userId +
                '}';
    }
}
