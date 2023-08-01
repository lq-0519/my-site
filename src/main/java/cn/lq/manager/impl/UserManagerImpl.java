package cn.lq.manager.impl;

import cn.lq.common.domain.po.UserPO;
import cn.lq.common.domain.query.inner.UserInnerQuery;
import cn.lq.dao.mysql.UserDao;
import cn.lq.manager.UserManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liqian477
 * @date 2023/08/01 19:58
 */
@Service("userManager")
public class UserManagerImpl implements UserManager {

    @Resource
    private UserDao userDao;

    /**
     * 更改用户信息
     */
    @Override
    public int update(UserPO user) {
        return userDao.update(user);
    }

    /**
     * 根据主键编号获取用户信息
     */
    @Override
    public UserPO queryForObject(Long id) {
        return userDao.queryForObject(id);
    }

    /**
     * 根据用户名和密码获取用户信息
     */
    @Override
    public List<UserPO> queryForList(UserInnerQuery userInnerQuery) {
        return userDao.queryForList(userInnerQuery);
    }
}
