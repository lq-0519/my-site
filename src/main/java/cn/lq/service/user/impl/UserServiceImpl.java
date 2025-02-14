package cn.lq.service.user.impl;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.po.UserPO;
import cn.lq.common.domain.query.inner.UserInnerQuery;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.exception.ParamInvalidException;
import cn.lq.common.utils.CollectionUtils;
import cn.lq.common.utils.TaleUtils;
import cn.lq.manager.UserManager;
import cn.lq.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author winterchen
 * @date 2018/4/20
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserManager userManager;


    @Transactional
    @Override
    public int updateUserInfo(UserPO user) {
        if (null == user.getId()) {
            throw BusinessException.withErrorCode("用户id不可能为空");
        }
        return userManager.update(user);
    }

    @Override
    public UserPO getUserInfoById(Long uId) {
        return userManager.queryForObject(uId);
    }

    @Override
    public UserPO login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw BusinessException.withErrorCode(Constant.Auth.USERNAME_PASSWORD_IS_EMPTY);
        }

        String pwd = TaleUtils.MD5encode(username + password);
        UserInnerQuery userInnerQuery = new UserInnerQuery(username, pwd);
        List<UserPO> userPOList = userManager.queryForList(userInnerQuery);
        if (CollectionUtils.isEmpty(userPOList)) {
            throw BusinessException.withErrorCode(Constant.Auth.USERNAME_PASSWORD_ERROR);
        }

        return userPOList.get(0);
    }

    @Override
    public UserPO queryByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new ParamInvalidException("queryByUsername 入参为空");
        }

        UserInnerQuery userInnerQuery = new UserInnerQuery();
        userInnerQuery.setUsername(username);
        return Optional.ofNullable(userManager.queryForList(userInnerQuery))
                .filter(CollectionUtils::isNotEmpty)
                .map(v -> v.get(0))
                .orElse(null);
    }
}
