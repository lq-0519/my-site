package cn.lq.web.controller;

import cn.lq.common.domain.po.UserPO;
import cn.lq.common.utils.MapCache;
import cn.lq.common.utils.TaleUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author winterchen
 * @date 2018/4/30
 */
public abstract class BaseController {

    protected MapCache cache = MapCache.single();

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    /**
     * 获取请求绑定的登录对象
     */
    public UserPO user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Long getUid(HttpServletRequest request) {
        return this.user(request).getId();
    }

    /**
     * 数组转字符串
     */
    public String join(String[] arr) {
        StringBuilder ret = new StringBuilder();
        for (String item : arr) {
            ret.append(',').append(item);
        }

        return ret.length() > 0 ? ret.substring(1) : ret.toString();
    }
}
