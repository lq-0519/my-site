package cn.lq.common.domain.po;

import java.io.Serializable;

/**
 * 文章实体
 *
 * @author winterchen
 * @date 2018/4/29
 */
public class ContentPO extends BasePO implements Serializable {

    /**
     * 内容标题
     */
    private String title;
    /**
     * 标题图片
     */
    private String titlePic;
    /**
     * 内容缩略名
     */
    private String slug;
    /**
     * 内容所属用户id
     */
    private Long authorId;
    /**
     * 内容类别
     */
    private String type;
    /**
     * 内容状态
     */
    private String status;
    /**
     * 标签列表
     */
    private String tags;
    /**
     * 分类列表
     */
    private String categories;
    /**
     * 点击次数
     */
    private Integer hits;
    /**
     * 是否允许评论
     */
    private Integer allowComment;
    /**
     * 是否允许ping
     */
    private Integer allowPing;
    /**
     * 允许出现在聚合中
     */
    private Integer allowFeed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePic() {
        return titlePic;
    }

    public void setTitlePic(String titlePic) {
        this.titlePic = titlePic;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

    public Integer getAllowPing() {
        return allowPing;
    }

    public void setAllowPing(Integer allowPing) {
        this.allowPing = allowPing;
    }

    public Integer getAllowFeed() {
        return allowFeed;
    }

    public void setAllowFeed(Integer allowFeed) {
        this.allowFeed = allowFeed;
    }

    @Override
    public String toString() {
        return "ContentPO{" +
                "title='" + title + '\'' +
                ", titlePic='" + titlePic + '\'' +
                ", slug='" + slug + '\'' +
                ", authorId=" + authorId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", tags='" + tags + '\'' +
                ", categories='" + categories + '\'' +
                ", hits=" + hits +
                ", allowComment=" + allowComment +
                ", allowPing=" + allowPing +
                ", allowFeed=" + allowFeed +
                '}';
    }
}
