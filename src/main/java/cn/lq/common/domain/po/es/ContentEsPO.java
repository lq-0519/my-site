package cn.lq.common.domain.po.es;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author liqian477
 * @date 2023/07/05 15:05
 */
@Document(indexName = "content", type = "content", shards = 1, replicas = 0)
public class ContentEsPO extends BaseEsPO implements Serializable {
    /**
     * 内容标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;
    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;
    /**
     * 标题图片
     */
    @Field(type = FieldType.Keyword)
    private String titlePic;
    /**
     * 内容缩略名
     */
    @Field(type = FieldType.Keyword)
    private String slug;
    /**
     * 内容所属用户id
     */
    @Field(type = FieldType.Long)
    private Long authorId;
    /**
     * 内容类别
     */
    @Field(type = FieldType.Keyword)
    private String type;
    /**
     * 内容状态
     */
    @Field(type = FieldType.Keyword)
    private String status;
    /**
     * 标签列表
     */
    @Field(type = FieldType.Keyword)
    private String tags;
    /**
     * 分类列表
     */
    @Field(type = FieldType.Keyword)
    private String categories;
    /**
     * 点击次数
     */
    @Field(type = FieldType.Integer)
    private Integer hits;
    /**
     * 是否允许评论
     */
    @Field(type = FieldType.Integer)
    private Integer allowComment;
    /**
     * 是否允许ping
     */
    @Field(type = FieldType.Integer)
    private Integer allowPing;
    /**
     * 允许出现在聚合中
     */
    @Field(type = FieldType.Integer)
    private Integer allowFeed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return "ContentEsPO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
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
