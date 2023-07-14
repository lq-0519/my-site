package cn.lq.service.content;

import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.es.ContentEsInnerQuery;
import cn.lq.common.domain.vo.ContentVO;
import cn.lq.common.domain.vo.PageVO;

/**
 * 文章服务层
 *
 * @author winterchen
 * @date 2018/4/29
 */
public interface ContentService {

    /**
     * 添加文章
     */
    void addArticle(ContentEsPO contentEsPO);

    /**
     * 根据编号删除文章
     */
    void deleteArticleById(Long cid);

    /**
     * 更新文章
     */
    void updateArticleById(ContentEsPO contentEsPO);

    /**
     * 更新分类
     */
    void updateCategory(String ordinal, String newCategory);

    /**
     * 添加文章点击量
     */
    void updateContentByCid(ContentEsPO content);

    /**
     * 根据编号获取文章
     */
    ContentEsPO getArticleById(Long id);

    /**
     * 根据条件获取文章列表
     */
    PageVO<ContentEsPO> queryContentPage(ContentEsInnerQuery contentEsInnerQuery, int page, int pageSize);

    /**
     * 根据条件获取文章列表
     * 带评论信息
     */
    PageVO<ContentVO> queryContentPageWithComment(ContentEsInnerQuery contentEsInnerQuery, int page, int pageSize);

    /**
     * 更新文章的点击量
     */
    void updateArticleHit(Long contentId, Integer contentHits);
}
