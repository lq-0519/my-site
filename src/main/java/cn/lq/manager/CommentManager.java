package cn.lq.manager;

import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.query.inner.CommentInnerQuery;

import java.util.List;

/**
 * @author liqian477
 */
public interface CommentManager {
    /**
     * 新增评论
     */
    int insert(CommentPO commentPO);

    /**
     * 删除评论
     */
    int delete(Long id);

    /**
     * 更新评论的状态
     */
    int update(CommentPO commentPO);

    /**
     * 获取单条评论
     */
    CommentPO queryForObject(Long id);

    /**
     * 根据条件获取评论列表
     */
    List<CommentPO> queryForList(CommentInnerQuery commentInnerQuery);

    /**
     * 获取文章数量
     */
    int queryForCount(CommentInnerQuery commentInnerQuery);
}
