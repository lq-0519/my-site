package cn.lq.common.domain.query.inner;

/**
 * meta查询条件
 *
 * @author winterchen
 * @date 2018/4/30
 */
public class MetaInnerQuery extends BaseInnerQuery {

    /**
     * meta Name
     */
    private String name;
    /**
     * 类型
     */
    private String type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MetaInnerQuery{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
