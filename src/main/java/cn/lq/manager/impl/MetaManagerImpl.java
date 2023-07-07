package cn.lq.manager.impl;

import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.po.MetaPO;
import cn.lq.common.domain.query.inner.MetaInnerQuery;
import cn.lq.dao.mysql.MetaDao;
import cn.lq.manager.MetaManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author liqian477
 * @date 2023/06/28 20:57
 */
@Service("metaManager")
public class MetaManagerImpl implements MetaManager {
    @Resource
    private MetaDao metaDao;

    /**
     * 添加元数据
     */
    @Override
    public int insert(MetaPO meta) {
        return metaDao.insert(meta);
    }

    /**
     * 删除元数据
     */
    @Override
    public int delete(Long id) {
        return metaDao.delete(id);
    }

    /**
     * 更新元数据
     */
    @Override
    public int update(MetaPO meta) {
        return metaDao.update(meta);
    }

    /**
     * 根据编号获取元数据
     */
    @Override
    public MetaPO queryForObject(Long id) {
        return metaDao.queryForObject(id);
    }

    /**
     * 根据条件查询
     */
    @Override
    public List<MetaPO> queryForList(MetaInnerQuery metaInnerQuery) {
        return metaDao.queryForList(metaInnerQuery);
    }

    /**
     * 根据类型获取meta数量
     */
    @Override
    public int queryForCount(MetaInnerQuery metaInnerQuery) {
        return metaDao.queryForCount(metaInnerQuery);
    }

    /**
     * 根据sql查询
     */
    @Override
    public List<MetaExtendPO> selectFromSql(Map<String, Object> paraMap) {
        return metaDao.selectFromSql(paraMap);
    }
}
