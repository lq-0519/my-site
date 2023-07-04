package cn.lq.manager;

import cn.lq.common.domain.po.ConfigPO;
import cn.lq.common.domain.query.inner.ConfigInnerQuery;

import java.util.List;

/**
 * @author liqian477
 */
public interface ConfigManager {

    /**
     * 删除网站配置
     */
    int deleteByQuery(ConfigInnerQuery configInnerQuery);

    /**
     * 更新网站配置
     */
    int updateByQuery(ConfigPO configPO, ConfigInnerQuery configInnerQuery);

    /**
     * queryForList
     */
    List<ConfigPO> queryForList(ConfigInnerQuery configInnerQuery);

    /**
     * 根据code查config
     */
    ConfigPO getConfigByCode(String code);
}
