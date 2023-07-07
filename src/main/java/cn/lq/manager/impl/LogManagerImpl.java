package cn.lq.manager.impl;

import cn.lq.common.domain.po.LogPO;
import cn.lq.common.domain.query.inner.LogInnerQuery;
import cn.lq.dao.mysql.LogDao;
import cn.lq.manager.LogManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liqian477
 * @date 2023/07/04 16:14
 */
@Service("logManager")
public class LogManagerImpl implements LogManager {
    @Resource
    private LogDao logDao;

    /**
     * 添加日志
     */
    @Override
    public int insert(LogPO logPO) {
        return logDao.insert(logPO);
    }

    /**
     * 删除日志
     */
    @Override
    public int delete(Long id) {
        return logDao.delete(id);
    }

    /**
     * 获取日志
     */
    @Override
    public List<LogPO> queryForList(LogInnerQuery logInnerQuery) {
        return logDao.queryForList(logInnerQuery);
    }
}
