package cn.lq.dao;

import cn.lq.common.domain.po.ConfigPO;
import cn.lq.common.domain.query.inner.ConfigInnerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网站配置dao
 *
 * @author winterchen
 * @date 2018/4/29
 */
public interface ConfigDao {

    /**
     * 删除网站配置
     */
    int deleteByQuery(ConfigInnerQuery configInnerQuery);

    /**
     * 更新网站配置
     */
    int updateByQuery(@Param("update") ConfigPO configPO, @Param("query") ConfigInnerQuery configInnerQuery);

    /**
     * queryForList
     */
    List<ConfigPO> queryForList(ConfigInnerQuery configInnerQuery);
}
