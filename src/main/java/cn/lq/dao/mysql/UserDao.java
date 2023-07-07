package cn.lq.dao.mysql;

import cn.lq.common.domain.po.UserPO;
import cn.lq.common.domain.query.inner.UserInnerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author winterchen
 * @date 2018/4/20
 */
public interface UserDao {

    /**
     * 更改用户信息
     */
    int update(UserPO user);

    /**
     * 根据主键编号获取用户信息
     */
    UserPO queryForObject(@Param("id") Long id);

    /**
     * 根据用户名和密码获取用户信息
     */
    List<UserPO> queryForList(UserInnerQuery userInnerQuery);

}
