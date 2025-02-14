package cn.lq.common.domain.query.inner.es;

import cn.lq.common.domain.anno.EsQueryField;
import cn.lq.common.domain.other.OrderBy;

import java.io.Serializable;
import java.util.List;

/**
 * BaseInnerQuery
 *
 * @author liqian477
 * @date 2023/06/28 16:19
 */
public class BaseEsInnerQuery implements Serializable {
    /**
     * id
     */
    @EsQueryField
    private Long id;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 排序字段
     */
    private List<OrderBy> orderByList;

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


    public List<OrderBy> getOrderByList() {
        return orderByList;
    }

    public void setOrderByList(List<OrderBy> orderByList) {
        this.orderByList = orderByList;
    }

    @Override
    public String toString() {
        return "BaseEsInnerQuery{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", orderByList=" + orderByList +
                '}';
    }
}
