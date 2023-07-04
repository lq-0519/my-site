package cn.lq.service.option;

import cn.lq.common.domain.po.ConfigPO;

import java.util.List;
import java.util.Map;

/**
 * @author winterchen
 * @date 2018/4/28
 */
public interface OptionService {

    /**
     * 更新网站配置
     */
    void updateOptionByCode(String name, String value);

    /**
     * 保存网站配置
     */
    void saveOptions(Map<String, String> options);

    /**
     * 根据名称获取网站配置
     */
    ConfigPO getOptionByCode(String name);

    /**
     * 获取全部网站配置
     */
    List<ConfigPO> getOptions();
}
