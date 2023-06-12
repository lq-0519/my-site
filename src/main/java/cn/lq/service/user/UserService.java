package cn.lq.service.user;

import cn.lq.common.model.UserDomain;
import org.springframework.stereotype.Service;

/**
 * @author winterchen
 * @date 2018/4/20
 */
@Service
public interface UserService {

    /**
     * @Author: winterchen
     * @Description: 更改用户信息
     * @Date: 2018/4/20
     */
    int updateUserInfo(UserDomain user);

    /**
     * @param uId 主键
     * @Description: 根据主键编号获取用户信息
     * @Date: 2018/4/20
     */
    UserDomain getUserInfoById(Integer uId);


    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     */
    UserDomain login(String username, String password);

}
