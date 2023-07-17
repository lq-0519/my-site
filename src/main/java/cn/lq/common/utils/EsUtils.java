package cn.lq.common.utils;

import cn.lq.common.domain.anno.EsQueryField;
import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.enums.DeletedEnum;
import cn.lq.common.domain.enums.EsQueryWayEnum;
import cn.lq.common.domain.other.OrderBy;
import cn.lq.common.domain.po.es.BaseEsPO;
import cn.lq.common.domain.query.inner.es.BaseEsInnerQuery;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.exception.ParamInvalidException;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liqian477
 * @date 2023/07/06 17:38
 */
public class EsUtils {
    /**
     * 缓存查询类-属性对象
     */
    private static final ConcurrentHashMap<String, List<Field>> CACHE_CLASS_FIELD_LIST = new ConcurrentHashMap<>();
    /**
     * 缓存类-属性名称
     * <p>
     * key:类全名 value:属性名称集合
     */
    private static final ConcurrentHashMap<String, Set<String>> CACHE_CLASS_FILED_NAME = new ConcurrentHashMap<>();
    /**
     * 不更新字段
     */
    private static final List<String> CANNOT_UPDATE_FIELD_LIST = Lists.newArrayList(Constant.Es.FIELD_ID, Constant.Es.FIELD_MODIFIED, Constant.Es.FIELD_CREATED);

    private static final Logger LOGGER = LoggerFactory.getLogger(EsUtils.class);

    /**
     * 获取排序字段
     * 默认按照创建时间降序排列
     */
    public static Sort getSort(List<OrderBy> orderByList, Class<?> clazz) {
        if (CollectionUtils.isEmpty(orderByList) || clazz == null) {
            return Sort.by(Sort.Order.desc(Constant.Es.FIELD_CREATED));
        }

        ArrayList<Sort.Order> orders = new ArrayList<>();
        for (OrderBy orderBy : orderByList) {
            if (orderBy == null) {
                continue;
            }

            if (isNotEsPoClassField(clazz, orderBy.getField())) {
                continue;
            }

            if (Boolean.TRUE.equals(orderBy.isAsc())) {
                orders.add(Sort.Order.asc(orderBy.getField()));
            } else {
                orders.add(Sort.Order.desc(orderBy.getField()));
            }
        }

        if (CollectionUtils.isEmpty(orders)) {
            return Sort.by(Sort.Order.desc(Constant.Es.FIELD_CREATED));
        } else {
            return Sort.by(orders);
        }
    }

    /**
     * 通用更新IndexRequest构建
     */
    public static IndexRequest getIndexRequest(BaseEsPO baseEsPO, Class<?> clazz) {
        IndexRequest indexRequest = new IndexRequest();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put(Constant.Es.FIELD_MODIFIED, new Date());
        List<Field> fieldList = getClassFields(clazz);
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object paramValue = field.get(baseEsPO);
                if (paramValue == null) {
                    continue;
                }

                String fieldName = field.getName();
                if (CANNOT_UPDATE_FIELD_LIST.contains(fieldName)) {
                    continue;
                }

                updateMap.put(fieldName, paramValue);
            }
            indexRequest.source(updateMap);
        } catch (Exception e) {
            LOGGER.error(String.format("getIndexRequest error! --> %s", JSON.toJSONString(baseEsPO)), e);
            throw new BusinessException("es根据参数构建更新条件异常", e);
        }

        return indexRequest;
    }


    /**
     * 根据query获取es的BoolQueryBuilder
     */
    public static BoolQueryBuilder getBoolQueryBuilder(BaseEsInnerQuery baseQuery, Class<?> clazz) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<Field> fieldList = getClassFields(clazz);
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                Object paramValue = field.get(baseQuery);
                if (paramValue == null) {
                    continue;
                }

                EsQueryField anno = field.getAnnotation(EsQueryField.class);
                if (anno == null) {
                    LOGGER.info("属性{}标签为空或不支持ES查询", field.getName());
                    continue;
                }

                EsQueryWayEnum queryWayEnum = anno.way();
                //以注解上面写的字段名称为准
                String fieldName = StringUtils.isNotBlank(anno.field()) ? anno.field() : field.getName();
                if (queryWayEnum == EsQueryWayEnum.IGNORE) {
                    LOGGER.info("属性{}EsQueryWayEnum为忽略查询", fieldName);
                    continue;
                }
                //有可能注解上写的字段名是错的
                if (isNotEsPoClassField(clazz, fieldName)) {
                    throw new ParamInvalidException("注解上写的es字段名是错的");
                }

                switch (queryWayEnum) {
                    case GT:
                        boolQueryBuilder.filter(QueryBuilders.rangeQuery(fieldName).gt(paramValue));
                        break;
                    case GTE:
                        boolQueryBuilder.filter(QueryBuilders.rangeQuery(fieldName).gte(paramValue));
                        break;
                    case LT:
                        boolQueryBuilder.filter(QueryBuilders.rangeQuery(fieldName).lt(paramValue));
                        break;
                    case LTE:
                        boolQueryBuilder.filter(QueryBuilders.rangeQuery(fieldName).lte(paramValue));
                        break;
                    case LIKE:
                        boolQueryBuilder.must(QueryBuilders.matchQuery(fieldName, paramValue.toString()));
                        break;
                    case PREFIX_LIKE:
                        boolQueryBuilder.must(QueryBuilders.prefixQuery(fieldName, paramValue.toString()));
                        break;
                    case EQ_CACHE:
                        boolQueryBuilder.filter(QueryBuilders.termQuery(fieldName, paramValue));
                        break;
                    case NEQ:
                        boolQueryBuilder.mustNot(QueryBuilders.termQuery(fieldName, paramValue));
                        break;
                    case IN:
                        boolQueryBuilder.filter(QueryBuilders.termsQuery(fieldName, (Collection<?>) paramValue));
                        break;
                    case NOT_IN:
                        boolQueryBuilder.mustNot(QueryBuilders.termsQuery(fieldName, (Collection<?>) paramValue));
                        break;
                    case ISNULL:
                        boolQueryBuilder.filter(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(paramValue.toString())));
                        break;
                    case EQ:
                    default:
                        boolQueryBuilder.must(QueryBuilders.termQuery(fieldName, paramValue));
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("buildBoolQueryBuilder error! --> %s", JSON.toJSONString(baseQuery)), e);
            throw new BusinessException("es根据参数构建查询条件异常", e);
        }

        if (DeletedEnum.DELETE.isEqual(baseQuery.getDeleted())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(Constant.Es.FIELD_DELETED, DeletedEnum.DELETE.getStatus()).queryName("deleteQuery"));
        } else {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery(Constant.Es.FIELD_DELETED, DeletedEnum.DELETE.getStatus()).queryName("noDeleteQuery"));
        }
        return boolQueryBuilder;
    }

    /**
     * 判断fieldName是不是clazz的字段
     *
     * @return true: 不是
     */
    private static boolean isNotEsPoClassField(Class<?> clazz, String fieldName) {
        if (!CACHE_CLASS_FILED_NAME.containsKey(clazz.getName())) {
            initClassFieldCache(clazz);
        }

        return !CACHE_CLASS_FILED_NAME.get(clazz.getName()).contains(fieldName);
    }

    /**
     * 获取已缓存的类属性
     */
    private static List<Field> getClassFields(Class<?> clazz) {
        if (!CACHE_CLASS_FIELD_LIST.containsKey(clazz.getName())) {
            initClassFieldCache(clazz);
        }

        return CACHE_CLASS_FIELD_LIST.get(clazz.getName());
    }

    /**
     * 初始化缓存信息
     */
    private static synchronized void initClassFieldCache(Class<?> clazz) {
        String className = clazz.getName();
        Set<String> fieldNameSet = new HashSet<>();
        List<Field> fieldList = getAllDeclaredFields(clazz);
        for (Field field : fieldList) {
            fieldNameSet.add(field.getName());
        }

        CACHE_CLASS_FILED_NAME.put(className, fieldNameSet);
        CACHE_CLASS_FIELD_LIST.put(className, fieldList);
    }

    /**
     * 获取所有属性
     */
    private static List<Field> getAllDeclaredFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        Set<String> fieldSet = new HashSet<>();
        while (!clazz.equals(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                if (fieldSet.contains(field.getName())) {
                    continue;
                }

                fieldSet.add(field.getName());
                fieldList.add(field);
            }

            clazz = clazz.getSuperclass();
        }

        return fieldList;
    }
}
