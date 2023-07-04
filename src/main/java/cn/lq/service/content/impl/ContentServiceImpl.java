package cn.lq.service.content.impl;

import cn.lq.common.domain.bo.ContentBO;
import cn.lq.common.domain.constant.ErrorConstant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.enums.CommentStatusEnum;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.po.ContentMetaBindPO;
import cn.lq.common.domain.po.ContentPO;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import cn.lq.common.domain.query.inner.ContentInnerQuery;
import cn.lq.common.domain.query.inner.ContentMetaBindInnerQuery;
import cn.lq.common.domain.vo.ContentVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.BeanConverter;
import cn.lq.common.utils.CollectionUtils;
import cn.lq.common.utils.MapCache;
import cn.lq.common.utils.PageUtils;
import cn.lq.manager.CommentManager;
import cn.lq.manager.ContentManager;
import cn.lq.manager.ContentMetaBindManager;
import cn.lq.service.content.ContentService;
import cn.lq.service.meta.MetaService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author winterchen
 * @date 2018/4/29
 */
@Service("contentService")
public class ContentServiceImpl implements ContentService {

    private final MapCache cache = MapCache.single();
    @Resource
    private ContentManager contentManager;

    @Resource
    private CommentManager commentManager;

    @Resource
    private MetaService metaService;

    @Resource
    private ContentMetaBindManager contentMetaBindManager;


    @Transactional
    @Override
    @CacheEvict(value = {"atricleCache", "atricleCaches", "siteCache"}, allEntries = true, beforeInvocation = true)
    public void addArticle(ContentBO contentBO) {
        if (null == contentBO) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        if (StringUtils.isBlank(contentBO.getTitle())) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_CAN_NOT_EMPTY);
        }
        if (contentBO.getTitle().length() > WebConst.MAX_TITLE_COUNT) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_IS_TOO_LONG);
        }
        if (StringUtils.isBlank(contentBO.getContent())) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_CAN_NOT_EMPTY);
        }
        if (contentBO.getContent().length() > WebConst.MAX_TEXT_COUNT) {
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_IS_TOO_LONG);
        }

        //标签和分类
        String tags = contentBO.getTags();
        String categories = contentBO.getCategories();
        ContentPO contentPO = BeanConverter.convert(ContentPO.class, contentBO);
        contentManager.insert(contentPO);
        Long contentId = contentPO.getId();
        metaService.addMetas(contentId, tags, Types.TAG.getType());
        metaService.addMetas(contentId, categories, Types.CATEGORY.getType());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"atricleCache", "atricleCaches", "siteCache"}, allEntries = true, beforeInvocation = true)
    public void deleteArticleById(Long contentId) {
        if (null == contentId) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        contentManager.delete(contentId);
        //同时也要删除该文章下的所有评论
        CommentInnerQuery commentInnerQuery = new CommentInnerQuery();
        commentInnerQuery.setContentId(contentId);
        commentInnerQuery.setStatus(CommentStatusEnum.APPROVED.getStatus());
        List<CommentPO> comments = commentManager.queryForList(commentInnerQuery);
        if (null != comments && comments.size() > 0) {
            comments.forEach(comment -> {
                commentManager.delete(comment.getId());
            });
        }
        //删除标签和分类关联
        ContentMetaBindInnerQuery query = new ContentMetaBindInnerQuery();
        query.setContentId(contentId);
        List<ContentMetaBindPO> relationShips = contentMetaBindManager.queryForList(query);
        if (null != relationShips && relationShips.size() > 0) {
            contentMetaBindManager.deleteByQuery(query);
        }

    }

    @Override
    @Transactional
    @CacheEvict(value = {"atricleCache", "atricleCaches", "siteCache"}, allEntries = true, beforeInvocation = true)
    public void updateArticleById(ContentBO contentBO) {
        //标签和分类
        String tags = contentBO.getTags();
        String categories = contentBO.getCategories();
        ContentPO contentPO = BeanConverter.convert(ContentPO.class, contentBO);
        contentManager.update(contentPO);
        Long cid = contentBO.getId();
        ContentMetaBindInnerQuery delQuery = new ContentMetaBindInnerQuery();
        delQuery.setContentId(cid);
        contentMetaBindManager.deleteByQuery(delQuery);
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());

    }

    @Override
    @Transactional
    @CacheEvict(value = {"atricleCache", "atricleCaches", "siteCache"}, allEntries = true, beforeInvocation = true)
    public void updateCategory(String ordinal, String newCatefory) {
        ContentInnerQuery cond = new ContentInnerQuery();
        cond.setCategory(ordinal);
        List<ContentPO> atricles = contentManager.queryForList(cond);
        atricles.forEach(atricle -> {
            atricle.setCategories(atricle.getCategories().replace(ordinal, newCatefory));
            contentManager.update(atricle);
        });
    }


    @Override
    @CacheEvict(value = {"atricleCache", "atricleCaches", "siteCache"}, allEntries = true, beforeInvocation = true)
    public void updateContentByCid(ContentPO content) {
        if (null != content && null != content.getId()) {
            contentManager.update(content);
        }
    }

    @Override
    @Cacheable(value = "atricleCache", key = "'atricleById_' + #p0")
    public ContentPO getArticleById(Long cid) {
        if (null == cid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        return contentManager.queryForObject(cid);
    }

    @Override
    @Cacheable(value = "atricleCaches", key = "'articlesByCond_' + #p1 + 'type_' + #p0.type")
    public PageInfo<ContentVO> queryContentPage(ContentInnerQuery contentInnerQuery, int pageNum, int pageSize) {
        if (null == contentInnerQuery) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        List<ContentPO> list = PageUtils.pack(pageNum, pageSize, () -> contentManager.queryForList(contentInnerQuery)).getList();
        if (CollectionUtils.isEmpty(list)) {
            return new PageInfo<>();
        } else {
            return new PageInfo<>(BeanConverter.convertToList(ContentVO.class, list));
        }
    }

    @Override
    public PageInfo<ContentPO> searchContent(String param, int pageNun, int pageSize) {
        // TODO: liqian477 2023/7/4 分页待实现
        return null;
    }

    @Override
    public ContentVO getArticleDetail(Long contentId) {
        if (contentId == null) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        ContentPO contentPO = contentManager.queryForObject(contentId);
        ContentVO contentVO = BeanConverter.convert(ContentVO.class, contentPO);
        CommentInnerQuery commentInnerQuery = new CommentInnerQuery();
        commentInnerQuery.setContentId(contentId);
        commentInnerQuery.setStatus(CommentStatusEnum.APPROVED.getStatus());
        int commentsCount = commentManager.queryForCount(commentInnerQuery);
        contentVO.setCommentsNum(commentsCount);
        return contentVO;
    }

    @Override
    public void updateArticleHit(Long contentId, Integer contentHits) {
        Integer hits = cache.hget("article", "hits");
        if (contentHits == null) {
            contentHits = 0;
        }

        hits = null == hits ? 1 : hits + 1;
        if (hits >= WebConst.HIT_EXCEED) {
            ContentPO temp = new ContentPO();
            temp.setId(contentId);
            temp.setHits(contentHits + hits);
            updateContentByCid(temp);
            cache.hset("article", "hits", 1);
        } else {
            cache.hset("article", "hits", hits);
        }
    }
}
