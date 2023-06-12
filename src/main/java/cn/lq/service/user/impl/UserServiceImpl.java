package cn.lq.service.user.impl;

import cn.lq.common.constant.ErrorConstant;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.model.UserDomain;
import cn.lq.common.utils.TaleUtils;
import cn.lq.dao.UserDao;
import cn.lq.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author winterchen
 * @date 2018/4/20
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;//这里会报错，但是并不会影响


    @Transactional
    @Override
    public int updateUserInfo(UserDomain user) {
        if (null == user.getUid()) {
            throw BusinessException.withErrorCode("用户编号不可能为空");
        }
        return userDao.updateUserInfo(user);
    }

    @Override
    public UserDomain getUserInfoById(Integer uId) {
        return userDao.getUserInfoById(uId);
    }

    @Override
    public UserDomain login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY);
        }

        String pwd = TaleUtils.MD5encode(username + password);
        UserDomain user = userDao.getUserInfoByCond(username, pwd);
        if (null == user) {
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_ERROR);
        }

        return user;
    }
}
