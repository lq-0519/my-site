package cn.lq.manager;

import cn.lq.common.domain.po.UserPO;
import cn.lq.common.domain.query.inner.UserInnerQuery;

import java.util.List;

/**
 * UserManager
 *
 * @author liqian477
 */
public interface UserManager {
    /**
     * 更改用户信息
     */
    int update(UserPO user);

    /**
     * 根据主键编号获取用户信息
     */
    UserPO queryForObject(Long id);

    /**
     * 根据用户名和密码获取用户信息
     */
    List<UserPO> queryForList(UserInnerQuery userInnerQuery);
}
