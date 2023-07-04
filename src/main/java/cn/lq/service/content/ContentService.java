package cn.lq.service.content;

import cn.lq.common.domain.bo.ContentBO;
import cn.lq.common.domain.po.ContentPO;
import cn.lq.common.domain.query.inner.ContentInnerQuery;
import cn.lq.common.domain.vo.ContentVO;
import com.github.pagehelper.PageInfo;

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
    void addArticle(ContentBO contentBO);

    /**
     * 根据编号删除文章
     */
    void deleteArticleById(Long cid);

    /**
     * 更新文章
     */
    void updateArticleById(ContentBO contentBO);

    /**
     * 更新分类
     */
    void updateCategory(String ordinal, String newCatefory);

    /**
     * 添加文章点击量
     */
    void updateContentByCid(ContentPO content);

    /**
     * 根据编号获取文章
     */
    ContentPO getArticleById(Long cid);

    /**
     * 根据条件获取文章列表
     */
    PageInfo<ContentVO> queryContentPage(ContentInnerQuery contentInnerQuery, int pageNum, int pageSize);

    /**
     * 搜索文章
     */
    PageInfo<ContentPO> searchContent(String param, int pageNun, int pageSize);

    /**
     * 获取文章详细信息
     */
    ContentVO getArticleDetail(Long contentId);

    /**
     * 更新文章的点击量
     */
    void updateArticleHit(Long contentId, Integer contentHits);
}
