package cn.lq.common.domain.dto;

import java.io.Serializable;

/**
 * 后台统计对象
 *
 * @author winterchen
 * @date 2018/4/30
 */
public class StatisticsDto implements Serializable {

    /**
     * 文章数
     */
    private Integer articles;
    /**
     * 评论数
     */
    private Integer comments;
    /**
     * 连接数
     */
    private Integer links;
    /**
     * 附件数
     */
    private Integer attachs;

    public Integer getArticles() {
        return articles;
    }

    public void setArticles(Integer articles) {
        this.articles = articles;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getLinks() {
        return links;
    }

    public void setLinks(Integer links) {
        this.links = links;
    }

    public Integer getAttachs() {
        return attachs;
    }

    public void setAttachs(Integer attachs) {
        this.attachs = attachs;
    }

    @Override
    public String toString() {
        return "StatisticsDto{" +
                "articles=" + articles +
                ", comments=" + comments +
                ", links=" + links +
                ", attachs=" + attachs +
                '}';
    }
}
