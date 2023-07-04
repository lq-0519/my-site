package cn.lq.common.domain.query.inner;

import java.io.Serializable;

/**
 * BaseInnerQuery
 *
 * @author liqian477
 * @date 2023/06/28 16:19
 */
public class BaseInnerQuery implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 是否删除
     */
    private Integer deleted;

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseInnerQuery{" +
                "id=" + id +
                ", deleted=" + deleted +
                '}';
    }
}
