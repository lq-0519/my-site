package cn.lq.service.comment.impl;

import cn.lq.common.cond.CommentCond;
import cn.lq.common.constant.ErrorConstant;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.model.CommentDomain;
import cn.lq.common.model.ContentDomain;
import cn.lq.common.utils.DateKit;
import cn.lq.common.utils.TaleUtils;
import cn.lq.dao.CommentDao;
import cn.lq.service.comment.CommentService;
import cn.lq.service.content.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
@Service
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
    private CommentDao commentDao;

    @Resource
    private ContentService contentService;

    static {
        STATUS_MAP.put("approved", STATUS_NORMAL);
        STATUS_MAP.put("not_audit", STATUS_BLANK);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"commentCache", "siteCache"}, allEntries = true)
    public void addComment(CommentDomain comments) {
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
            if (null == comments.getCid()) {
                msg = "评论文章不能为空";
            }
            if (msg != null) {
                throw BusinessException.withErrorCode(msg);
            }
            ContentDomain article = contentService.getArticleById(comments.getCid());
            if (null == article) {
                throw BusinessException.withErrorCode("该文章不存在");
            }
            comments.setOwnerId(article.getAuthorId());
            comments.setStatus(STATUS_MAP.get(STATUS_BLANK));
            comments.setCreated(DateKit.getCurrentUnixTime());
            commentDao.addComment(comments);

            ContentDomain temp = new ContentDomain();
            temp.setCid(article.getCid());
            Integer count = article.getCommentsNum();
            if (null == count) {
                count = 0;
            }
            temp.setCommentsNum(count + 1);
            contentService.updateContentByCid(temp);
        }

    }

    @Transactional
    @Override
    @CacheEvict(value = {"commentCache", "siteCache"}, allEntries = true)
    public void deleteComment(Integer coid) {
        if (null == coid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        // 如果删除的评论存在子评论，一并删除
        //查找当前评论是否有子评论
        CommentCond commentCond = new CommentCond();
        commentCond.setParent(coid);
        CommentDomain comment = commentDao.getCommentById(coid);
        List<CommentDomain> childComments = commentDao.getCommentsByCond(commentCond);
        Integer count = 0;
        //删除子评论
        if (null != childComments && childComments.size() > 0) {
            for (CommentDomain childComment : childComments) {
                commentDao.deleteComment(childComment.getCoid());
                count++;
            }
        }
        //删除当前评论
        commentDao.deleteComment(coid);
        count++;

        //更新当前文章的评论数
        ContentDomain contentDomain = contentService.getArticleById(comment.getCid());
        if (null != contentDomain
                && null != contentDomain.getCommentsNum()
                && contentDomain.getCommentsNum() != 0) {
            contentDomain.setCommentsNum(contentDomain.getCommentsNum() - count);
            contentService.updateContentByCid(contentDomain);
        }
    }

    @Override
    @CacheEvict(value = {"commentCache", "siteCache"}, allEntries = true)
    public void updateCommentStatus(Integer coid, String status) {
        if (null == coid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        commentDao.updateCommentStatus(coid, status);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentById_' + #p0")
    public CommentDomain getCommentById(Integer coid) {
        if (null == coid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        return commentDao.getCommentById(coid);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentsByCId_' + #p0")
    public List<CommentDomain> getCommentsByCId(Integer cid) {
        if (null == cid) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return commentDao.getCommentsByCId(cid);
    }

    @Override
    @Cacheable(value = "commentCache", key = "'commentsByCond_' + #p1")
    public PageInfo<CommentDomain> getCommentsByCond(CommentCond commentCond, int pageNum, int pageSize) {
        if (null == commentCond) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<CommentDomain> comments = commentDao.getCommentsByCond(commentCond);
        PageInfo<CommentDomain> pageInfo = new PageInfo<>(comments);
        return pageInfo;
    }
}
