package cn.lq.common.domain.other;

import java.io.Serializable;

/**
 * @author liqian477
 * @date 2023/07/14 18:47
 */
public class OrderBy implements Serializable {
    /**
     * 字段
     */
    private String field;
    /**
     * true: 升序
     * false: 降序
     */
    private Boolean asc;

    public OrderBy() {
    }

    public OrderBy(String field, Boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Boolean isAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    @Override
    public String toString() {
        return "OrderBy{" +
                "field='" + field + '\'' +
                ", asc=" + asc +
                '}';
    }
}
