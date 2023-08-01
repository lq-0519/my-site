package cn.lq.web.controller.admin;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.LogActions;
import cn.lq.common.domain.dto.StatisticsDto;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.po.LogPO;
import cn.lq.common.domain.po.UserPO;
import cn.lq.common.domain.vo.ContentVO;
import cn.lq.common.domain.vo.PageVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.GsonUtils;
import cn.lq.common.utils.Response;
import cn.lq.common.utils.TaleUtils;
import cn.lq.service.log.LogService;
import cn.lq.service.site.SiteService;
import cn.lq.service.user.UserService;
import cn.lq.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author winterchen
 * @date 2018/4/30
 */
@Api("后台首页")
@Controller("adminIndexController")
@RequestMapping(value = "/admin")
public class IndexController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private SiteService siteService;

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;


    @ApiOperation("进入首页")
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        LOGGER.info("Enter admin index method");
        List<CommentPO> comments = siteService.getComments(5);
        List<ContentVO> contents = siteService.getNewArticles(5);
        StatisticsDto statistics = siteService.getStatistics();
        // 取最新的20条日志
        PageVO<LogPO> logs = logService.getLogs(1, 5);
        List<LogPO> list = logs.getList();
        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        request.setAttribute("logs", list);
        LOGGER.info("Exit admin index method");
        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping(value = "/profile")
    public String profile() {
        return "admin/profile";
    }

    /**
     * 保存个人信息
     */
    @PostMapping(value = "/profile")
    @ResponseBody
    public Response<?> saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
        UserPO users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            UserPO temp = new UserPO();
            temp.setId(users.getId());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UserPO original = (UserPO) session.getAttribute(Constant.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(Constant.LOGIN_SESSION_KEY, original);
        }
        return Response.success();
    }

    /**
     * 修改密码
     */
    @PostMapping(value = "/password")
    @ResponseBody
    public Response<?> upPwd(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request, HttpSession session) {
        UserPO users = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            return Response.fail("请确认信息输入完整");
        }

        if (!users.getPassword().equals(TaleUtils.MD5encode(users.getUsername() + oldPassword))) {
            return Response.fail("旧密码错误");
        }
        if (password.length() < 6 || password.length() > 64) {
            return Response.fail("请输入6-64位密码");
        }

        try {
            UserPO temp = new UserPO();
            temp.setId(users.getId());
            String pwd = TaleUtils.MD5encode(users.getUsername() + password);
            temp.setPassword(pwd);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_PWD.getAction(), null, request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UserPO original = (UserPO) session.getAttribute(Constant.LOGIN_SESSION_KEY);
            original.setPassword(pwd);
            session.setAttribute(Constant.LOGIN_SESSION_KEY, original);
            return Response.success();
        } catch (Exception e) {
            String msg = "密码修改失败";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return Response.fail(msg);
        }
    }
}
