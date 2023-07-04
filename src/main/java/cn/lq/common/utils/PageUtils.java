package cn.lq.common.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author liqian477
 * @date 2023/07/04 17:48
 */
public class PageUtils {

    public static <R> PageInfo<R> pack(int page, int pageSize, DaoRunner<R> daoRunner) {
        page = page <= 0 ? 1 : page;
        pageSize = pageSize <= 0 || pageSize > 100 ? 10 : pageSize;
        PageHelper.startPage(page, pageSize);
        List<R> run = daoRunner.run();
        return CollectionUtils.isEmpty(run) ? new PageInfo<>() : new PageInfo<>(run);
    }

    public interface DaoRunner<T> {
        List<T> run();
    }
}
