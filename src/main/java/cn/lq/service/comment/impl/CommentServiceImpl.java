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
    public void addComment(CommentPO comments) {
        //字符串不为空代表有错误
        String errorMsg = null;
        if (comments == null) {
            errorMsg = "评论对象为空";
        }

        if (comments != null) {
            if (StringUtils.isBlank(comments.getAuthor())) {
                comments.setAuthor("热心网友");
            }
            if (StringUtils.isNotBlank(comments.getMail()) && !TaleUtils.isEmail(comments.getMail())) {
                errorMsg = "请输入正确的邮箱格式";
            }
            if (StringUtils.isBlank(comments.getContent())) {
                errorMsg = "评论内容不能为空";
            }
            if (comments.getContent().length() < 1 || comments.getContent().length() > 2000) {
                errorMsg = "评论字数在1-2000个字符";
            }
            if (comments.getContentId() == null) {
                errorMsg = "评论文章不能为空";
            }

            if (StringUtils.isNotBlank(errorMsg)) {
                throw BusinessException.withErrorCode(errorMsg);
            }

            ContentEsPO article = contentService.getArticleById(comments.getContentId());
            if (null == article) {
                throw BusinessException.withErrorCode("该文章不存在");
            }

            comments.setOwnerId(article.getAuthorId());
            comments.setStatus(STATUS_MAP.get(STATUS_BLANK));
            commentManager.insert(comments);
        }
    }

    @Transactional
    @Override
    public void deleteComment(Long coid) {
        if (null == coid) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        // 如果删除的评论存在子评论，一并删除
        //查找当前评论是否有子评论
        CommentInnerQuery commentInnerQuery = new CommentInnerQuery();
        commentInnerQuery.setParent(coid);
        List<CommentPO> childComments = commentManager.queryForList(commentInnerQuery);

        //删除子评论
        if (null != childComments && childComments.size() > 0) {
            for (CommentPO childComment : childComments) {
                commentManager.delete(childComment.getId());
            }
        }

        //删除当前评论
        commentManager.delete(coid);
    }

    @Override
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
    public CommentPO getCommentById(Long coid) {
        if (null == coid) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        return commentManager.queryForObject(coid);
    }

    @Override
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
    public PageVO<CommentPO> getCommentsByCond(CommentInnerQuery commentInnerQuery, int pageNum, int pageSize) {
        if (null == commentInnerQuery) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        return PageUtils.pack(pageNum, pageSize, () -> commentManager.queryForList(commentInnerQuery));
    }
}
