package cn.lq.manager;

import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.po.MetaPO;
import cn.lq.common.domain.query.inner.MetaInnerQuery;

import java.util.List;
import java.util.Map;

/**
 * MetaManager
 *
 * @author liqian477
 */
public interface MetaManager {

    /**
     * 添加元数据
     */
    int insert(MetaPO meta);

    /**
     * 删除元数据
     */
    int delete(Long id);

    /**
     * 更新元数据
     */
    int update(MetaPO meta);

    /**
     * 根据编号获取元数据
     */
    MetaPO queryForObject(Long id);

    /**
     * 根据条件查询
     */
    List<MetaPO> queryForList(MetaInnerQuery metaInnerQuery);

    /**
     * 根据类型获取meta数量
     */
    int queryForCount(MetaInnerQuery metaInnerQuery);

    /**
     * 根据sql查询
     */
    List<MetaExtendPO> selectFromSql(Map<String, Object> paraMap);
}
