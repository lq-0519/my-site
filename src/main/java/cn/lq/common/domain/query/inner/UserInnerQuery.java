package cn.lq.common.domain.query.inner;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liqian477
 * @date 2023/06/30 17:01
 */
public class UserInnerQuery extends BaseInnerQuery implements Serializable {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * email
     */
    private String email;
    /**
     * 用户显示的名称
     */
    private String screenName;
    /**
     * 最后活动时间
     */
    private Date activated;
    /**
     * 上次登录最后活跃时间
     */
    private Date logged;
    /**
     * 用户组
     */
    private String groupName;

    public UserInnerQuery() {
    }

    public UserInnerQuery(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Date getActivated() {
        return activated;
    }

    public void setActivated(Date activated) {
        this.activated = activated;
    }

    public Date getLogged() {
        return logged;
    }

    public void setLogged(Date logged) {
        this.logged = logged;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "UserInnerQuery{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", screenName='" + screenName + '\'' +
                ", activated=" + activated +
                ", logged=" + logged +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
