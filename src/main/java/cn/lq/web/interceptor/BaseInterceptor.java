package cn.lq.web.interceptor;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.po.ConfigPO;
import cn.lq.common.domain.po.UserPO;
import cn.lq.common.utils.AdminCommons;
import cn.lq.common.utils.Commons;
import cn.lq.common.utils.MapCache;
import cn.lq.common.utils.NetKit;
import cn.lq.common.utils.TaleUtils;
import cn.lq.common.utils.UUID;
import cn.lq.service.option.OptionService;
import cn.lq.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 自定义拦截器
 *
 * @author liqian477
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Resource
    private UserService userService;

    @Resource
    private OptionService optionService;

    @Resource
    private Commons commons;

    @Resource
    private AdminCommons adminCommons;

    private final MapCache cache = MapCache.single();
    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        LOGGER.info("UserAgent: {}", request.getHeader(USER_AGENT));
        LOGGER.info("用户访问地址: {}, 来路地址: {}", uri, NetKit.getIpAddrByRequest(request));
        //请求拦截处理
        UserPO user = TaleUtils.getLoginUser(request);
        if (Constant.Env.DEV.equals(env)) {
            //测试环境免登录
            user = userService.queryByUsername("admin");
            request.getSession().setAttribute(Constant.LOGIN_SESSION_KEY, user);
        }

        if (user == null) {
            Long uid = TaleUtils.getCookieUid(request);
            if (uid != null) {
                //这里还是有安全隐患,cookie是可以伪造的
                user = userService.getUserInfoById(uid);
                request.getSession().setAttribute(Constant.LOGIN_SESSION_KEY, user);
            }
        }

        boolean noLogin = user == null
                && uri.startsWith("/admin")
                && !uri.startsWith("/admin/login")
                && !uri.startsWith("/admin/css")
                && !uri.startsWith("/admin/images")
                && !uri.startsWith("/admin/js")
                && !uri.startsWith("/admin/plugins")
                && !uri.startsWith("/admin/editormd");
        if (noLogin) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }

        //设置get请求的token
        if ("GET".equals(request.getMethod())) {
            String csrf_token = UUID.UU64();
            // 默认存储30分钟
            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
            request.setAttribute("_csrf_token", csrf_token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        ConfigPO ov = optionService.getOptionByCode("site_record");
        //一些工具类和公共方法
        httpServletRequest.setAttribute("commons", commons);
        httpServletRequest.setAttribute("option", ov);
        httpServletRequest.setAttribute("adminCommons", adminCommons);
        initSiteConfig();
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

    private void initSiteConfig() {
        if (Constant.INIT_CONFIG.isEmpty()) {
            List<ConfigPO> options = optionService.getOptions();
            options.forEach(option -> {
                Constant.INIT_CONFIG.put(option.getCode(), option.getValue());
            });
        }
    }
}
