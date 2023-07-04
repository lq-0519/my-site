package cn.lq.common.domain.po;

/**
 * @author winterchen
 * @date 2018/4/29
 */
public class CommentPO extends BasePO {
    /**
     * contents表主键,关联字段
     */
    private Long contentId;
    /**
     * 评论作者
     */
    private String author;
    /**
     * 评论所属用户id
     */
    private Long authorId;
    /**
     * 评论所属内容作者id
     */
    private Long ownerId;
    /**
     * 评论者邮件
     */
    private String mail;
    /**
     * 评论者ip地址
     */
    private String ip;
    /**
     * 评论者客户端
     */
    private String agent;
    /**
     * 评论类型
     */
    private String type;
    /**
     * 评论状态
     *
     * @see cn.lq.common.domain.enums.CommentStatusEnum
     */
    private String status;
    /**
     * 父级评论
     */
    private Long parent;
    /**
     * 评论内容
     */
    private String content;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentPO{" +
                "contentId=" + contentId +
                ", author='" + author + '\'' +
                ", authorId=" + authorId +
                ", ownerId=" + ownerId +
                ", mail='" + mail + '\'' +
                ", ip='" + ip + '\'' +
                ", agent='" + agent + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", parent=" + parent +
                ", content='" + content + '\'' +
                '}';
    }
}
