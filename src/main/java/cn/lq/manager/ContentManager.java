package cn.lq.manager;

import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.es.ContentEsInnerQuery;
import org.springframework.data.domain.Page;

/**
 * ContentManager
 *
 * @author liqian477
 */
public interface ContentManager {

    /**
     * 添加文章
     * 插入es
     */
    long insert(ContentEsPO contentEsPO);

    /**
     * 根据编号删除文章
     * 删除es
     */
    void delete(Long id);

    /**
     * 更新文章
     */
    @SuppressWarnings("UnusedReturnValue")
    int update(ContentEsPO contentEsPO);

    /**
     * 根据编号获取文章
     * 查es
     */
    ContentEsPO queryForObject(Long id);

    /**
     * 查总数
     */
    long queryForCount(ContentEsInnerQuery contentEsInnerQuery);

    /**
     * 分页查询
     */
    Page<ContentEsPO> queryForPage(ContentEsInnerQuery contentEsInnerQuery, int page, int pageSize);

}
