package cn.lq.web.controller.admin;

import cn.lq.common.constant.LogActions;
import cn.lq.common.constant.WebConst;
import cn.lq.common.model.OptionsDomain;
import cn.lq.common.utils.GsonUtils;
import cn.lq.common.utils.Response;
import cn.lq.service.log.LogService;
import cn.lq.service.option.OptionService;
import cn.lq.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author winterchen
 * @date 2018/4/30
 */
@Api("系统设置")
@Controller
@RequestMapping("/admin/setting")
public class SettingController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingController.class);

    @Resource
    private OptionService optionService;

    @Resource
    private LogService logService;


    @ApiOperation("进入设置页")
    @GetMapping(value = "")
    public String setting(HttpServletRequest request) {
        List<OptionsDomain> optionsList = optionService.getOptions();
        Map<String, String> options = new HashMap<>();
        optionsList.forEach((option) -> options.put(option.getName(), option.getValue()));
        request.setAttribute("options", options);
        return "admin/setting";
    }


    @ApiOperation("保存系统设置")
    @PostMapping(value = "")
    @ResponseBody
    public Response<?> saveSetting(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> queries = new HashMap<>();
            parameterMap.forEach((key, value) -> queries.put(key, join(value)));
            optionService.saveOptions(queries);

            //刷新设置
            List<OptionsDomain> options = optionService.getOptions();
            if (!CollectionUtils.isEmpty(options)) {
                WebConst.initConfig.clear();
                for (OptionsDomain option : options) {
                    WebConst.initConfig.put(option.getName(), option.getValue());
                }
            }

            logService.addLog(LogActions.SYS_SETTING.getAction(), GsonUtils.toJsonString(queries), request.getRemoteAddr(), this.getUid(request));
            return Response.success();
        } catch (Exception e) {
            LOGGER.error("saveSetting 接口异常", e);
            return Response.fail(e.getMessage());
        }
    }
}
