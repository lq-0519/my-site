package cn.lq.common.domain.query.inner;

/**
 * @author liqian477
 * @date 2023/06/30 11:16
 */
public class ConfigInnerQuery extends BaseInnerQuery {
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
        return "ConfigInnerQuery{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
