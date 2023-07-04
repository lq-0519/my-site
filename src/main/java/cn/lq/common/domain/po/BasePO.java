package cn.lq.common.domain.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liqian477
 * @date 2023/06/27 14:25
 */
public class BasePO implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 修改时间
     */
    private Date modified;
    /**
     * 是否删除
     */
    private Integer deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "BasePO{" +
                "id=" + id +
                ", create=" + created +
                ", modified=" + modified +
                ", deleted=" + deleted +
                '}';
    }
}
