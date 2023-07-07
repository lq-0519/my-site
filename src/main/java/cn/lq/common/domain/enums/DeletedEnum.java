package cn.lq.common.domain.enums;

/**
 * @author liqian477
 */

public enum DeletedEnum {
    DELETE(1),
    NOT_DELETE(0),
    ;
    private final Integer delete;

    DeletedEnum(Integer delete) {
        this.delete = delete;
    }

    public Integer getDelete() {
        return delete;
    }

    public boolean isEqual(Integer deleted) {
        return this.delete.equals(deleted);
    }
}
