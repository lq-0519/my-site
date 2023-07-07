package cn.lq.dao.mysql;

import cn.lq.common.domain.po.ContentMetaBindPO;
import cn.lq.common.domain.query.inner.ContentMetaBindInnerQuery;

import java.util.List;

/**
 * 中间表
 *
 * @author winterchen
 * @date 2018/4/30
 */
public interface ContentMetaBindDao {

    /**
     * 添加
     */
    int insert(ContentMetaBindPO relationShip);

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
