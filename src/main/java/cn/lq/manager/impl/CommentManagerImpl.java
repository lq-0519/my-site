package cn.lq.manager.impl;

import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import cn.lq.dao.CommentDao;
import cn.lq.manager.CommentManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author liqian477
 * @date 2023/06/28 11:59
 */
@Service("commentManager")
public class CommentManagerImpl implements CommentManager {
    @Resource
    private CommentDao commentDao;

    /**
     * 新增评论
     */
    @Override
    public int insert(CommentPO commentPO) {
        return commentDao.insert(commentPO);
    }

    /**
     * 删除评论
     */
    @Override
    public int delete(Long id) {
        return commentDao.delete(id);
    }

    /**
     * 更新评论的状态
     */
    @Override
    public int update(CommentPO commentPO) {
        return commentDao.update(commentPO);
    }

    /**
     * 获取单条评论
     */
    @Override
    public CommentPO queryForObject(Long id) {
        return commentDao.queryForObject(id);
    }

    /**
     * 根据条件获取评论列表
     */
    @Override
    public List<CommentPO> queryForList(CommentInnerQuery commentInnerQuery) {
        return commentDao.queryForList(commentInnerQuery);
    }

    @Override
    public int queryForCount(CommentInnerQuery commentInnerQuery) {
        return commentDao.queryForCount(commentInnerQuery);
    }
}
