package cn.lq.common.domain.enums;

/**
 * @author liqian477
 */

public enum DeletedEnum {
    DELETE(1),
    NOT_DELETE(0),
    ;
    private final Integer status;

    DeletedEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public boolean isEqual(Integer deleted) {
        return this.status.equals(deleted);
    }
}
