package cn.lq.common.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean拷贝工具
 * <p>
 * 对Orika简单封装, 方便使用
 * <p>
 * Orika优点:
 * 1.支持递归映射, 功能更强: 可以拷贝带有内部类的对象, 可以拷贝属性不同但属性名称相同的对象, 支持拆箱装箱
 * 2.操作字节码文件, 速度更快
 *
 * @author liqian
 * @date 2021/12/9
 */
public class BeanConverter {

    /**
     * 默认字段工厂
     */
    private static final MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    /**
     * 映射关系本地缓存
     */
    private static final Map<String, MapperFacade> CACHE_MAPPER_FACADE_MAP = new ConcurrentHashMap<>();

    /**
     * convert
     *
     * @param targetClass 目标对象class
     * @param source      源对象
     * @param <T>         target
     * @param <S>         source
     * @return 目标对象
     */
    public static <T, S> T convert(Class<T> targetClass, S source) {
        if (source == null) {
            return null;
        }

        // 获取source的class类
        Class<?> sourceClass = source.getClass();
        // 调用Orika的api
        MapperFacade mapperFacade = getMapperFacade(targetClass, sourceClass);
        return mapperFacade.map(source, targetClass);
    }

    /**
     * convertToList
     *
     * @param targetClass 目标集合元素class
     * @param sourceList  源集合对象
     * @param <T>         target
     * @param <S>         source
     * @return 目标集合
     */
    public static <T, S> List<T> convertToList(Class<T> targetClass, List<S> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return null;
        }

        // 获取源集合元素的class类
        Class<?> sourceClass = null;
        for (S s : sourceList) {
            if (s == null) {
                continue;
            }

            sourceClass = s.getClass();
            break;
        }

        if (sourceClass == null) {
            throw new RuntimeException("List拷贝出现异常, 集合中元素可能全为null");
        }
        // 调用Orika的api
        MapperFacade mapperFacade = getMapperFacade(targetClass, sourceClass);
        return mapperFacade.mapAsList(sourceList, targetClass);
    }

    /**
     * 获取自定义映射
     *
     * @param targetClass 目标class
     * @param sourceClass 源class
     * @return 映射类对象
     */
    private static <T, S> MapperFacade getMapperFacade(Class<T> targetClass, Class<S> sourceClass) {
        String mapKey = sourceClass.getCanonicalName() + "-" + targetClass.getCanonicalName();
        MapperFacade mapperFacade = CACHE_MAPPER_FACADE_MAP.get(mapKey);
        if (mapperFacade == null) {
            MAPPER_FACTORY.classMap(sourceClass, targetClass).byDefault().register();
            mapperFacade = MAPPER_FACTORY.getMapperFacade();
            CACHE_MAPPER_FACADE_MAP.put(mapKey, mapperFacade);
        }

        return mapperFacade;
    }
}
