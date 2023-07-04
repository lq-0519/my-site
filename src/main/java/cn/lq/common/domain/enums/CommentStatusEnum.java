package cn.lq.common.domain.enums;

import java.io.Serializable;

/**
 * 评论状态枚举
 *
 * @author liqian477
 */
public enum CommentStatusEnum implements Serializable {
    APPROVED("approved", "通过"),
    ;
    private final String status;
    private final String desc;

    CommentStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "CommentStatusEnum{" +
                "status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
