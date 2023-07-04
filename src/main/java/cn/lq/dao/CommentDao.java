package cn.lq.dao;

import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论实体类
 *
 * @author winterchen
 * @date 2018/4/29
 */
public interface CommentDao {

    /**
     * 新增评论
     */
    int insert(CommentPO commentPO);

    /**
     * 删除评论
     */
    int delete(@Param("id") Long id);

    /**
     * 更新评论的状态
     */
    int update(CommentPO commentPO);

    /**
     * 获取单条评论
     */
    CommentPO queryForObject(@Param("id") Long id);

    /**
     * 根据条件获取评论列表
     */
    List<CommentPO> queryForList(CommentInnerQuery commentInnerQuery);

    /**
     * 获取文章数量
     */
    int queryForCount(CommentInnerQuery commentInnerQuery);
}
