package cn.lq.dao;

import cn.lq.common.cond.CommentCond;
import cn.lq.common.model.CommentDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 评论实体类
 * Created by winterchen on 2018/4/29.
 */
@Mapper
@Component
public interface CommentDao {

    /**
     * 新增评论
     */
    int addComment(CommentDomain commentDomain);

    /**
     * 删除评论
     */
    int deleteComment(@Param("coid") Integer coid);

    /**
     * 更新评论的状态
     */
    int updateCommentStatus(@Param("coid") Integer coid, @Param("status") String status);

    /**
     * 获取单条评论
     */
    CommentDomain getCommentById(@Param("coid") Integer coid);

    /**
     * 根据文章编号获取评论列表
     */
    List<CommentDomain> getCommentsByCId(@Param("cid") Integer cid);

    /**
     * 根据条件获取评论列表
     */
    List<CommentDomain> getCommentsByCond(CommentCond commentCond);

    /**
     * 获取文章数量
     */
    Long getCommentsCount();
}
