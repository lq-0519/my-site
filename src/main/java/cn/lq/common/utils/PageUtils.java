package cn.lq.common.utils;

import cn.lq.common.domain.vo.PageVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author liqian477
 * @date 2023/07/04 17:48
 */
public class PageUtils {

    public static <R> PageVO<R> pack(int page, int pageSize, DaoRunner<R> daoRunner) {
        page = page <= 0 ? 1 : page;
        pageSize = pageSize <= 0 || pageSize > 100 ? 10 : pageSize;
        PageHelper.startPage(page, pageSize);
        List<R> run = daoRunner.run();
        PageInfo<R> pageInfo = CollectionUtils.isEmpty(run) ? new PageInfo<>() : new PageInfo<>(run);
        @SuppressWarnings("unchecked") PageVO<R> result = BeanConverter.convert(PageVO.class, pageInfo);
        result.setList(pageInfo.getList());
        return result;
    }

    public static <R> PageVO<R> convertPageVO(Page<R> esPage) {
        PageInfo<R> pageInfo = new PageInfo<>(esPage.getContent());
        pageInfo.setPageNum(esPage.getPageable().getPageNumber());
        pageInfo.setTotal(esPage.getTotalElements());
        pageInfo.setPages(esPage.getTotalPages());
        pageInfo.setList(esPage.getContent());
        pageInfo.setNavigatepageNums(getNavigatePageNums(esPage.getTotalPages(), esPage.getPageable().getPageNumber()));
        @SuppressWarnings("unchecked") PageVO<R> result = BeanConverter.convert(PageVO.class, pageInfo);
        result.setList(pageInfo.getList());
        return result;
    }

    public static <R> boolean isEmpty(PageVO<R> pageVO) {
        return pageVO == null || CollectionUtils.isEmpty(pageVO.getList());
    }

    public static <R> boolean isNotEmpty(PageVO<R> pageVO) {
        return !isEmpty(pageVO);
    }

    public interface DaoRunner<T> {
        List<T> run();
    }

    /**
     * 根据总页数和当前页数生成一个长度是8的分页导航条
     */
    private static int[] getNavigatePageNums(int totalPages, int page) {
        if (totalPages == 0) {
            return null;
        }

        if (totalPages < 8) {
            int[] a = new int[totalPages];
            for (int i = 1; i <= totalPages; i++) {
                a[i - 1] = i;
            }
            return a;
        }

        int[] ints = new int[8];
        if (page <= 4) {
            for (int i = 0; i < 8; i++) {
                ints[i] = i + 1;
            }
            return ints;
        }

        if (totalPages - page <= 3) {
            for (int i = 0; i < 8; i++) {
                ints[i] = totalPages - (7 - i);
            }
            return ints;
        }

        int i = page - 3;
        for (int j = 0; j < 8; j++) {
            ints[j] = i + j;
        }
        return ints;
    }
}
