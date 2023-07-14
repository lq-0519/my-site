package cn.lq.service.comment.impl;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.enums.CommentStatusEnum;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import cn.lq.common.domain.vo.PageVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.PageUtils;
import cn.lq.common.utils.TaleUtils;
import cn.lq.manager.CommentManager;
import cn.lq.service.comment.CommentService;
import cn.lq.service.content.ContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 评论实现类
 *
 * @author winterchen
 * @date 2018/4/29
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private static final Map<String, String> STATUS_MAP = new ConcurrentHashMap<>();
    /**
     * 评论状态：正常
     */
    private static final String STATUS_NORMAL = "approved";
    /**
     * 评论状态：不显示
     */
    private static final String STATUS_BLANK = "not_audit";

    @Resource
    private CommentManager commentManager;

    @Resource
    private ContentService contentService;

    static {
        STATUS_MAP.put("approved", STATUS_NORMAL);
        STATUS_MAP.put("not_audit", STATUS_BLANK);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"commentCache", "siteCache"}, allEntries = true)
    public void addComment(CommentPO comments) {
        String msg = null;
        if (null == comments) {
            msg = "评论对象为空";
        }
        if (comments != null) {
            if (StringUtils.isBlank(comments.getAuthor())) {
                comments.setAuthor("热心网友");
            }
            if (StringUtils.isNotBlank(comments.getMail()) && !TaleUtils.isEmail(comments.getMail())) {
                msg = "请输入正确的邮箱格式";
            }
            if (StringUtils.isBlank(comments.getContent())) {
                msg = "评论内容不能为空";
            }
            if (comments.getContent().length() < 5 || comments.getContent().length() > 2000) {
                msg = "评论字数在5-2000个字符";
            }
            if (null == comments.getContentId()) {
                msg = "评论文章不能为空";
            }
            if (msg != null) {
                throw BusinessException.withErrorCode(msg);
            }
            ContentEsPO article = contentService.getArticleById(comments.getContentId());
            if (null == article) {
                throw BusinessException.withErrorCode("该文章不存在");
            }

            comments.setOwnerId(article.getAuthorId());
            comments.setStatus(STATUS_MAP.get(STATUS_BLANK));
            commentManager.insert(comments);

            // TODO: liqian477 2023/6/27 更新评论数
        }

    }

    @Transactional
    @Override
    @CacheEvict(value = {"commentCache", "siteCache"}, allEntries = true)
    public void deleteComment(Long coid) {
        if (null == coid) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }
        // 如果删除的评论存在子评论，一并删除
        //查找当前评论是否有子评论
        CommentInnerQuery commentInnerQuery = new CommentInnerQuery();
        commentInnerQuery.setParent(coid);
        List<CommentPO> childComments = commentManager.queryForList(commentInnerQuery);
        Integer count = 0;
        //删除子评论
        if (null != childComments && childComments.size() > 0) {
            for (CommentPO childComment : childComments) {
                commentManager.delete(childComment.getId());
                count++;
            }
        }
        //删除当前评论
        commentManager.delete(coid);
        count++;

        // TODO: liqian477 2023/6/27 更新评论数
    }

    @Override
    @CacheEvict(value = {"commentCache", "siteCache"}, allEntries = true)
    public void updateCommentStatus(Long commentId, String status) {
        if (null == commentId) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }
        CommentPO commentPO = new CommentPO();
        commentPO.setId(commentId);
        commentPO.setStatus(status);
        commentManager.update(commentPO);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentById_' + #p0")
    public CommentPO getCommentById(Long coid) {
        if (null == coid) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        return commentManager.queryForObject(coid);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentsByCId_' + #p0")
    public List<CommentPO> getCommentsByContentId(Long contentId) {
        if (null == contentId) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }
        CommentInnerQuery commentInnerQuery = new CommentInnerQuery();
        commentInnerQuery.setContentId(contentId);
        commentInnerQuery.setStatus(CommentStatusEnum.APPROVED.getStatus());
        return commentManager.queryForList(commentInnerQuery);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentsByCond_' + #p1")
    public PageVO<CommentPO> getCommentsByCond(CommentInnerQuery commentInnerQuery, int pageNum, int pageSize) {
        if (null == commentInnerQuery) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        return PageUtils.pack(pageNum, pageSize, () -> commentManager.queryForList(commentInnerQuery));
    }
}
