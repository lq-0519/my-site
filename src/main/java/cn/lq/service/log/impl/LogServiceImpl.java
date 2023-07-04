package cn.lq.service.log.impl;

import cn.lq.common.domain.constant.ErrorConstant;
import cn.lq.common.domain.po.LogPO;
import cn.lq.common.domain.query.inner.LogInnerQuery;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.PageUtils;
import cn.lq.manager.LogManager;
import cn.lq.service.log.LogService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 请求日志
 *
 * @author winterchen
 * @date 2018/4/29
 */
@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogManager logManager;

    @Override
    public void addLog(String action, String data, String ip, Long userId) {
        LogPO logPO = new LogPO();
        logPO.setUserId(userId);
        logPO.setIp(ip);
        logPO.setData(data);
        logPO.setAction(action);
        logManager.insert(logPO);
    }

    @Override
    public void deleteLogById(Long id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        logManager.delete(id);
    }

    @Override
    public PageInfo<LogPO> getLogs(int pageNum, int pageSize) {
        return PageUtils.pack(pageNum, pageSize, () -> logManager.queryForList(new LogInnerQuery()));
    }
}
