package cn.lq.service.log;

import cn.lq.common.domain.po.LogPO;
import cn.lq.common.domain.vo.PageVO;

/**
 * 用户请求日志
 *
 * @author winterchen
 * @date 2018/4/29
 */
public interface LogService {

    /**
     * 添加
     */
    void addLog(String action, String data, String ip, Long userId);

    /**
     * 删除日志
     */
    void deleteLogById(Long id);

    /**
     * 获取日志
     */
    PageVO<LogPO> getLogs(int pageNum, int pageSize);
}
