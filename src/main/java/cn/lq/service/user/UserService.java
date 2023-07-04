package cn.lq.service.user;

import cn.lq.common.domain.po.UserPO;
import org.springframework.stereotype.Service;

/**
 * @author winterchen
 * @date 2018/4/20
 */
@Service
public interface UserService {

    /**
     * 更改用户信息
     */
    int updateUserInfo(UserPO user);

    /**
     * 根据主键编号获取用户信息
     */
    UserPO getUserInfoById(Long uId);

    /**
     * 用户登录
     */
    UserPO login(String username, String password);

}
