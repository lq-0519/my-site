package cn.lq.manager;

import cn.lq.common.domain.po.ContentMetaBindPO;
import cn.lq.common.domain.query.inner.ContentMetaBindInnerQuery;

import java.util.List;

/**
 * @author liqian477
 */
public interface ContentMetaBindManager {
    /**
     * 添加
     */
    int insert(ContentMetaBindPO contentMetaBindPO);

    /**
     * 根据文章编号和meta编号删除关联
     */
    int deleteByQuery(ContentMetaBindInnerQuery contentMetaBindInnerQuery);

    /**
     * queryForList
     */
    List<ContentMetaBindPO> queryForList(ContentMetaBindInnerQuery contentMetaBindInnerQuery);

    /**
     * 获取数量
     */
    int queryForCount(ContentMetaBindInnerQuery contentMetaBindInnerQuery);
}
