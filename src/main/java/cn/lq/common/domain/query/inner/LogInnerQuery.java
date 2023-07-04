package cn.lq.common.domain.query.inner;

import java.io.Serializable;

/**
 * @author liqian477
 * @date 2023/06/30 18:46
 */
public class LogInnerQuery extends BaseInnerQuery implements Serializable {

    /**
     * 产生的动作
     */
    private String action;

    /**
     * 产生的数据
     */
    private String data;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 日志产生的ip
     */
    private String ip;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "LogInnerQuery{" +
                "action='" + action + '\'' +
                ", data='" + data + '\'' +
                ", userId=" + userId +
                ", ip='" + ip + '\'' +
                '}';
    }
}
