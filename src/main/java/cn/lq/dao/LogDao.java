package cn.lq.dao;

import cn.lq.common.domain.po.LogPO;
import cn.lq.common.domain.query.inner.LogInnerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author winterchen
 * @date 2018/4/29
 */
public interface LogDao {

    /**
     * 添加日志
     */
    int insert(LogPO logPO);

    /**
     * 删除日志
     */
    int delete(@Param("id") Long id);

    /**
     * 获取日志
     */
    List<LogPO> queryForList(LogInnerQuery logInnerQuery);
}
