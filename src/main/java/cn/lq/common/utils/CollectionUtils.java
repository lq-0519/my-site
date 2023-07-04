package cn.lq.common.utils;

import java.util.Collection;

/**
 * @author liqian477
 * @date 2023/06/30 14:57
 */
public class CollectionUtils {
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
}
