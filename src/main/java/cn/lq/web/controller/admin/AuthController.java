package cn.lq.web.controller.admin;

import cn.lq.common.constant.LogActions;
import cn.lq.common.constant.WebConst;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.model.UserDomain;
import cn.lq.common.utils.IPKit;
import cn.lq.common.utils.Response;
import cn.lq.common.utils.TaleUtils;
import cn.lq.service.log.LogService;
import cn.lq.service.user.UserService;
import cn.lq.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author winterchen
 * @date 2018/4/30
 */
@Api("登录相关接口")
@Controller
@RequestMapping(value = "/admin")
public class AuthController extends BaseController {

    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @ApiOperation("跳转登录页")
    @GetMapping(value = "/login")
    public String login() {
        return "admin/login";
    }

    @ApiOperation("登录")
    @PostMapping(value = "/login")
    @ResponseBody
    public Response<?> toLogin(HttpServletRequest request,
                               HttpServletResponse response,
                               @ApiParam(name = "username", value = "用户名", required = true) @RequestParam(name = "username") String username,
                               @ApiParam(name = "password", value = "密码", required = true) @RequestParam(name = "password") String password,
                               @ApiParam(name = "remeber_me", value = "记住我") @RequestParam(name = "remeber_me", required = false) String remeber_me) {

        // 获取ip并过滤登录时缓存的bug
        String ip = IPKit.getIpAddrByRequest(request);
        Integer error_count = cache.hget("login_error_count", ip);
        try {
            UserDomain userInfo = userService.login(username, password);
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, userInfo);
            if (StringUtils.isNotBlank(remeber_me)) {
                TaleUtils.setCookie(response, userInfo.getUid());
            }
            logService.addLog(LogActions.LOGIN.getAction(), null, request.getRemoteAddr(), userInfo.getUid());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            error_count = null == error_count ? 1 : error_count + 1;
            if (error_count > 3) {
                return Response.fail("您输入密码已经错误超过3次，请10分钟后尝试");
            }
            // 加入ip的过滤
            cache.hset("login_error_count", ip, error_count, 10 * 60);
            String msg = "登录失败";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return Response.fail(msg);
        }

        return Response.success();

    }

    /**
     * 注销
     */
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, org.apache.catalina.servlet4preview.http.HttpServletRequest request) {
        session.removeAttribute(WebConst.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, "");
        cookie.setValue(null);
        // 立即销毁cookie
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect("/admin/login");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("注销失败", e);
        }
    }
}
