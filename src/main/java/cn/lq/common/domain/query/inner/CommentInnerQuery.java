package cn.lq.common.domain.query.inner;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论的查找参数
 *
 * @author winterchen
 * @date 2018/4/29
 */
public class CommentInnerQuery extends BaseInnerQuery implements Serializable {
    /**
     * 状态
     */
    private String status;
    /**
     * 开始时间戳
     */
    private Date startTime;
    /**
     * 结束时间戳
     */
    private Date endTime;

    /**
     * 父评论编号
     */
    private Long parent;
    /**
     * 文章ID
     */
    private Long contentId;

    public CommentInnerQuery() {
    }

    public CommentInnerQuery(Long contentId) {
        this.contentId = contentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return "CommentInnerQuery{" +
                "status='" + status + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", parent=" + parent +
                ", contentId=" + contentId +
                '}';
    }
}
