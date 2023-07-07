package cn.lq.manager.impl;

import cn.lq.common.domain.po.ConfigPO;
import cn.lq.common.domain.query.inner.ConfigInnerQuery;
import cn.lq.common.exception.ParamInvalidException;
import cn.lq.common.utils.CollectionUtils;
import cn.lq.dao.mysql.ConfigDao;
import cn.lq.manager.ConfigManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author liqian477
 * @date 2023/06/30 14:30
 */
@Service("configManager")
public class ConfigManagerImpl implements ConfigManager {
    @Resource
    private ConfigDao configDao;

    /**
     * 删除网站配置
     */
    @Override
    public int deleteByQuery(ConfigInnerQuery configInnerQuery) {
        return configDao.deleteByQuery(configInnerQuery);
    }

    /**
     * 更新网站配置
     */
    @Override
    public int updateByQuery(ConfigPO configPO, ConfigInnerQuery configInnerQuery) {
        return configDao.updateByQuery(configPO, configInnerQuery);
    }

    /**
     * queryForList
     */
    @Override
    public List<ConfigPO> queryForList(ConfigInnerQuery configInnerQuery) {
        return configDao.queryForList(configInnerQuery);
    }

    @Override
    public ConfigPO getConfigByCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new ParamInvalidException("queryConfigByCode code为空");
        }

        ConfigInnerQuery configInnerQuery = new ConfigInnerQuery();
        configInnerQuery.setCode(code);
        return Optional.ofNullable(configDao.queryForList(configInnerQuery))
                .filter(CollectionUtils::isNotEmpty)
                .map(v -> v.get(0))
                .orElse(null);
    }
}
