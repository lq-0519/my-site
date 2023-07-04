package cn.lq.common.domain.po;

import java.io.Serializable;

/**
 * 文章关联信息表
 *
 * @author winterchen
 * @date 2018/4/30
 */
public class ContentMetaBindPO extends BasePO implements Serializable {

    /**
     * 文章主键编号
     */
    private Long contentId;
    /**
     * 项目编号
     */
    private Long metaId;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getMetaId() {
        return metaId;
    }

    public void setMetaId(Long metaId) {
        this.metaId = metaId;
    }

    @Override
    public String toString() {
        return "ContentMetaBindPO{" +
                "contentId=" + contentId +
                ", metaId=" + metaId +
                '}';
    }
}
