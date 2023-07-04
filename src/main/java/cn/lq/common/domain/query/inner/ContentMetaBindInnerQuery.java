package cn.lq.common.domain.query.inner;

import java.io.Serializable;

/**
 * ContentMetaBindInnerQuery
 *
 * @author liqian477
 * @date 2023/06/29 16:03
 */
public class ContentMetaBindInnerQuery extends BaseInnerQuery implements Serializable {
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
