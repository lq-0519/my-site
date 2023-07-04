package cn.lq.common.domain.enums;

import java.io.Serializable;

/**
 * 元数据类型
 *
 * @author liqian477
 */
public enum MetaTypeEnum implements Serializable {
    CATEGORY("category", "类别"),
    TAG("tag", "类别"),
    LINK("link", "友链"),
    ;
    private final String type;
    private final String desc;

    MetaTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MetaTypeEnum{" +
                "type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
