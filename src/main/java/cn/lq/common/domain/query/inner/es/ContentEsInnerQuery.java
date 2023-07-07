package cn.lq.common.domain.query.inner.es;

import cn.lq.common.domain.anno.EsQueryField;
import cn.lq.common.domain.enums.EsQueryWayEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章查询条件
 *
 * @author winterchen
 * @date 2018/4/29
 */
public class ContentEsInnerQuery extends BaseEsInnerQuery implements Serializable {
    /**
     * 内容标题
     */
    @EsQueryField(way = EsQueryWayEnum.LIKE)
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 标签
     */
    @EsQueryField
    private String tag;
    /**
     * 类别
     */
    @EsQueryField
    private String category;
    /**
     * 状态
     */
    @EsQueryField
    private String status;
    /**
     * 文章类型
     */
    @EsQueryField
    private String type;
    /**
     * 开始时间戳
     */
    @EsQueryField(field = "created", way = EsQueryWayEnum.GTE)
    private Date startTime;
    /**
     * 结束时间戳
     */
    @EsQueryField(field = "created", way = EsQueryWayEnum.LTE)
    private Date endTime;

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
        return "ContentEsInnerQuery{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tag='" + tag + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
