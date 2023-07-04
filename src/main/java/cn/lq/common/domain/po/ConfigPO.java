package cn.lq.common.domain.po;

import java.io.Serializable;

/**
 * 网站配置项
 *
 * @author winterchen
 * @date 2018/4/28
 */
public class ConfigPO extends BasePO implements Serializable {

    /**
     * 名称
     */
    private String code;
    /**
     * 内容
     */
    private String value;
    /**
     * 备注
     */
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ConfigPO{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
