package cn.lq.dao.mysql;

import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.po.MetaPO;
import cn.lq.common.domain.query.inner.MetaInnerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 元数据dao
 *
 * @author winterchen
 * @date 2018/4/29
 */
public interface MetaDao {

    /**
     * 添加元数据
     */
    int insert(MetaPO meta);

    /**
     * 删除元数据
     */
    int delete(@Param("id") Long id);

    /**
     * 更新元数据
     */
    int update(MetaPO meta);

    /**
     * 根据编号获取元数据
     */
    MetaPO queryForObject(@Param("id") Long id);

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
