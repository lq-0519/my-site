package cn.lq.common.domain.anno;

import cn.lq.common.domain.enums.EsQueryWayEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ES Field查询标记
 * 搭配 EsUtils 使用
 *
 * @author lq
 * @date 2023-07-06 17:04:44
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsQueryField {

    /**
     * 查询字段名称
     */
    String field() default "";

    /**
     * 查询方式
     */
    EsQueryWayEnum way() default EsQueryWayEnum.EQ_CACHE;

}
