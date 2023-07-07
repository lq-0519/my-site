package cn.lq.common.domain.po.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liqian477
 * @date 2023/07/05 15:04
 */
public class BaseEsPO implements Serializable {
    /**
     * id
     */
    @Id
    private Long id;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date created;
    /**
     * 修改时间
     */
    @Field(type = FieldType.Date)
    private Date modified;
    /**
     * 是否删除
     */
    @Field(type = FieldType.Integer)
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
        return "BaseEsPO{" +
                "id=" + id +
                ", created=" + created +
                ", modified=" + modified +
                ", deleted=" + deleted +
                '}';
    }
}
