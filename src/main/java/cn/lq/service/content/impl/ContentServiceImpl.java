package cn.lq.service.content.impl;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.enums.CommentStatusEnum;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.po.ContentMetaBindPO;
import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import cn.lq.common.domain.query.inner.ContentMetaBindInnerQuery;
import cn.lq.common.domain.query.inner.es.ContentEsInnerQuery;
import cn.lq.common.domain.vo.ContentVO;
import cn.lq.common.domain.vo.PageVO;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
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
    public void addArticle(ContentEsPO contentEsPO) {
        if (null == contentEsPO) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }
        if (StringUtils.isBlank(contentEsPO.getTitle())) {
            throw BusinessException.withErrorCode(Constant.Article.TITLE_CAN_NOT_EMPTY);
        }
        if (contentEsPO.getTitle().length() > WebConst.MAX_TITLE_COUNT) {
            throw BusinessException.withErrorCode(Constant.Article.TITLE_IS_TOO_LONG);
        }
        if (StringUtils.isBlank(contentEsPO.getContent())) {
            throw BusinessException.withErrorCode(Constant.Article.CONTENT_CAN_NOT_EMPTY);
        }
        if (contentEsPO.getContent().length() > WebConst.MAX_TEXT_COUNT) {
            throw BusinessException.withErrorCode(Constant.Article.CONTENT_IS_TOO_LONG);
        }

        //标签和分类
        String tags = contentEsPO.getTags();
        String categories = contentEsPO.getCategories();
        long contentId = contentManager.insert(contentEsPO);
        metaService.addMetas(contentId, tags, Types.TAG.getType());
        metaService.addMetas(contentId, categories, Types.CATEGORY.getType());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"atricleCache", "atricleCaches", "siteCache"}, allEntries = true, beforeInvocation = true)
    public void deleteArticleById(Long contentId) {
        if (null == contentId) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
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
    public void updateArticleById(ContentEsPO contentEsPO) {
        //标签和分类
        String tags = contentEsPO.getTags();
        String categories = contentEsPO.getCategories();
        contentManager.update(contentEsPO);
        Long cid = contentEsPO.getId();
        ContentMetaBindInnerQuery delQuery = new ContentMetaBindInnerQuery();
        delQuery.setContentId(cid);
        contentMetaBindManager.deleteByQuery(delQuery);
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());

    }

    @Override
    @Transactional
    @CacheEvict(value = {"atricleCache", "atricleCaches", "siteCache"}, allEntries = true, beforeInvocation = true)
    public void updateCategory(String ordinal, String newCategory) {
        ContentEsInnerQuery query = new ContentEsInnerQuery();
        query.setCategory(ordinal);
        while (true) {
            Page<ContentEsPO> contentEsPOS = contentManager.queryForPage(query, 1, 100);
            List<ContentEsPO> content = contentEsPOS.getContent();
            if (CollectionUtils.isEmpty(content)) {
                break;
            }

            content.forEach(contentEsPO -> {
                contentEsPO.setCategories(contentEsPO.getCategories().replace(ordinal, newCategory));
                contentManager.update(contentEsPO);
            });
        }
    }

    @Override
    @CacheEvict(value = {"atricleCache", "atricleCaches", "siteCache"}, allEntries = true, beforeInvocation = true)
    public void updateContentByCid(ContentEsPO content) {
        if (null != content && null != content.getId()) {
            contentManager.update(content);
        }
    }

    @Override
    @Cacheable(value = "atricleCache", key = "'atricleById_' + #p0")
    public ContentEsPO getArticleById(Long id) {
        if (null == id) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        return contentManager.queryForObject(id);
    }

    @Override
    public PageVO<ContentEsPO> queryContentPage(ContentEsInnerQuery contentEsInnerQuery, int page, int pageSize) {
        if (contentEsInnerQuery == null) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        Page<ContentEsPO> contentEsPOS = contentManager.queryForPage(contentEsInnerQuery, page, pageSize);
        return PageUtils.convertPageVO(contentEsPOS);
    }

    @Override
    public ContentVO getArticleDetail(Long contentId) {
        if (contentId == null) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        ContentEsPO contentEsPO = contentManager.queryForObject(contentId);
        ContentVO contentVO = BeanConverter.convert(ContentVO.class, contentEsPO);
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
            ContentEsPO temp = new ContentEsPO();
            temp.setId(contentId);
            temp.setHits(contentHits + hits);
            updateContentByCid(temp);
            cache.hset("article", "hits", 1);
        } else {
            cache.hset("article", "hits", hits);
        }
    }
}
