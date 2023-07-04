package cn.lq.common.domain.query.inner;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章查询条件
 *
 * @author winterchen
 * @date 2018/4/29
 */
public class ContentInnerQuery extends BaseInnerQuery implements Serializable {

    /**
     * 标签
     */
    private String tag;
    /**
     * 类别
     */
    private String category;
    /**
     * 状态
     */
    private String status;
    /**
     * 标题
     */
    private String title;
    /**
     * 文章类型
     */
    private String type;

    /**
     * 开始时间戳
     */
    private Date startTime;

    /**
     * 结束时间戳
     */
    private Date endTime;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "ContentInnerQuery{" +
                "tag='" + tag + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
