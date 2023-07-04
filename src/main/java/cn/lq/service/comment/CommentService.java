package cn.lq.service.comment;

import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 评论服务层
 *
 * @author winterchen
 * @date 2018/4/29
 */
public interface CommentService {

    /**
     * 新增评论
     *
     * @param commentPO 评论的实体
     */
    void addComment(CommentPO commentPO);

    /**
     * 删除评论
     *
     * @param coid 评论的主键编号
     */
    void deleteComment(Long coid);

    /**
     * 更新评论的状态
     *
     * @param commentId 评论的主键编号
     * @param status    状态
     */
    void updateCommentStatus(Long commentId, String status);

    /**
     * 查找单条评论
     */
    CommentPO getCommentById(Long coid);

    /**
     * 根据文章编号获取评论列表--只显示通过审核的评论-正常状态的
     *
     * @param cid 文章主键编号
     */
    List<CommentPO> getCommentsByCId(Long cid);

    /**
     * 根据条件获取评论列表
     *
     * @param commentInnerQuery 查询条件
     * @param pageNum           分页参数 第几页
     * @param pageSize          分页参数 每页条数
     */
    PageInfo<CommentPO> getCommentsByCond(CommentInnerQuery commentInnerQuery, int pageNum, int pageSize);
}
