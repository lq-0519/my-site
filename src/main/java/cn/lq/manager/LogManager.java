package cn.lq.manager;

import cn.lq.common.domain.po.LogPO;
import cn.lq.common.domain.query.inner.LogInnerQuery;

import java.util.List;

/**
 * @author liqian477
 */
public interface LogManager {

    /**
     * 添加日志
     */
    int insert(LogPO logPO);

    /**
     * 删除日志
     */
    int delete(Long id);

    /**
     * 获取日志
     */
    List<LogPO> queryForList(LogInnerQuery logInnerQuery);
}
