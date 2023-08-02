package cn.lq.common.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liqian477
 * @date 2023/08/02 11:58
 */
public class AttachmentVO implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 修改时间
     */
    private Date modified;
    /**
     * 是否删除
     */
    private Integer deleted;
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
    /**
     * fileUrl
     */
    private String fileUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "AttachmentVO{" +
                "id=" + id +
                ", created=" + created +
                ", modified=" + modified +
                ", deleted=" + deleted +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileKey='" + fileKey + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
