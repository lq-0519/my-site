package cn.lq.dao;

import cn.lq.common.model.UserDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author winterchen
 * @date 2018/4/20
 */
@Mapper
public interface UserDao {

    /**
     * @Author: winterchen
     * @Description: 更改用户信息
     * @Date: 2018/4/20
     */
    int updateUserInfo(UserDomain user);

    /**
     * @param uId 主键
     * @Author: winterchen
     * @Description: 根据主键编号获取用户信息
     * @Date: 2018/4/20
     */
    UserDomain getUserInfoById(@Param("uid") Integer uId);

    /**
     * 根据用户名和密码获取用户信息
     */
    UserDomain getUserInfoByCond(@Param("username") String username, @Param("password") String password);

}
