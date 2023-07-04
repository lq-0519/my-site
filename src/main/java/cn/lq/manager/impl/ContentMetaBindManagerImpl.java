package cn.lq.manager.impl;

import cn.lq.common.domain.po.ContentMetaBindPO;
import cn.lq.common.domain.query.inner.ContentMetaBindInnerQuery;
import cn.lq.dao.ContentMetaBindDao;
import cn.lq.manager.ContentMetaBindManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liqian477
 * @date 2023/06/29 16:20
 */
@Service("contentMetaBindManager")
public class ContentMetaBindManagerImpl implements ContentMetaBindManager {
    @Resource
    private ContentMetaBindDao contentMetaBindDao;

    /**
     * 添加
     */
    @Override
    public int insert(ContentMetaBindPO contentMetaBindPO) {
        return contentMetaBindDao.insert(contentMetaBindPO);
    }

    /**
     * 根据文章编号和meta编号删除关联
     */
    @Override
    public int deleteByQuery(ContentMetaBindInnerQuery contentMetaBindInnerQuery) {
        return contentMetaBindDao.deleteByQuery(contentMetaBindInnerQuery);
    }

    /**
     * queryForList
     */
    @Override
    public List<ContentMetaBindPO> queryForList(ContentMetaBindInnerQuery contentMetaBindInnerQuery) {
        return contentMetaBindDao.queryForList(contentMetaBindInnerQuery);
    }

    /**
     * 获取数量
     */
    @Override
    public int queryForCount(ContentMetaBindInnerQuery contentMetaBindInnerQuery) {
        return contentMetaBindDao.queryForCount(contentMetaBindInnerQuery);
    }
}
