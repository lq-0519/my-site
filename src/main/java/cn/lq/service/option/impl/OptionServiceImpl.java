package cn.lq.service.option.impl;

import cn.lq.common.domain.constant.ErrorConstant;
import cn.lq.common.domain.po.ConfigPO;
import cn.lq.common.domain.query.inner.ConfigInnerQuery;
import cn.lq.common.exception.BusinessException;
import cn.lq.manager.ConfigManager;
import cn.lq.service.option.OptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 网站配置服务层
 *
 * @author winterchen
 * @date 2018/4/28
 */
@Service
public class OptionServiceImpl implements OptionService {

    @Resource
    private ConfigManager configManager;

    @Override
    @Transactional
    @CacheEvict(value = {"optionsCache", "optionCache"}, allEntries = true, beforeInvocation = true)
    public void updateOptionByCode(String code, String value) {
        if (StringUtils.isBlank(code)) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        ConfigPO option = new ConfigPO();
        option.setValue(value);
        ConfigInnerQuery configInnerQuery = new ConfigInnerQuery();
        configInnerQuery.setCode(code);
        configManager.updateByQuery(option, configInnerQuery);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"optionsCache", "optionCache"}, allEntries = true, beforeInvocation = true)
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::updateOptionByCode);
        }
    }

    @Override
    @Cacheable(value = "optionCache", key = "'optionByCode_' + #p0")
    public ConfigPO getOptionByCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        return configManager.getConfigByCode(code);
    }

    @Override
    @Cacheable(value = "optionsCache", key = "'options_'")
    public List<ConfigPO> getOptions() {
        return configManager.queryForList(new ConfigInnerQuery());
    }
}
