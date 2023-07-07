package cn.lq.common.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * ES查询方式
 *
 * @author lq
 * @date 2023-07-06 17:03:42
 */
public enum EsQueryWayEnum {
    EQ_CACHE("EQ_CACHE", "全匹配(带缓存)"),
    EQ("EQ", "全匹配"),
    NEQ("NEQ", "不等于"),
    GT("GT", "大于"),
    GTE("GTE", "大于等于"),
    LT("LT", "小于"),
    LTE("LTE", "小于等于"),
    PREFIX_LIKE("PREFIX_LIKE", "匹配开头"),
    LIKE("LIKE", "模糊匹配"),
    IN("IN", "指定范围匹配"),
    ISNULL("ISNULL", "字段不存在及字段值为空"),
    IGNORE("IGNORE", "忽略字段"),
    NOT_IN("NOT_IN", "指定范围忽略"),
    ;

    private final static Map<String, EsQueryWayEnum> MAP_QUERY_WAY = new HashMap<>();
    /**
     * 查询方式
     */
    private final String way;
    /**
     * 描述
     */
    private final String desc;

    static {
        EsQueryWayEnum[] enums = EsQueryWayEnum.values();
        for (EsQueryWayEnum enumTmp : enums) {
            MAP_QUERY_WAY.put(enumTmp.getWay(), enumTmp);
        }
    }

    EsQueryWayEnum(String way, String desc) {
        this.way = way;
        this.desc = desc;
    }

    public static EsQueryWayEnum parseOf(String way) {
        return MAP_QUERY_WAY.get(way);
    }

    public String getWay() {
        return way;
    }

    public String getDesc() {
        return desc;
    }
}
