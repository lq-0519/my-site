package cn.lq.web.controller.admin;

import cn.lq.common.utils.Response;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author winterchen
 * @date 2018/4/20
 */
@Api("用户管理类")
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @GetMapping("/docker")
    @ResponseBody
    public Response<Object> dockerTest() {
        return Response.success("hello docker");
    }

}
