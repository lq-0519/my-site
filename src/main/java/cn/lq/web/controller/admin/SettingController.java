package cn.lq.web.controller.admin;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.LogActions;
import cn.lq.common.domain.po.ConfigPO;
import cn.lq.common.utils.CollectionUtils;
import cn.lq.common.utils.GsonUtils;
import cn.lq.common.utils.Response;
import cn.lq.service.log.LogService;
import cn.lq.service.option.OptionService;
import cn.lq.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
 * 系统设置
 *
 * @author winterchen
 * @date 2018/4/30
 */
@Controller
@RequestMapping("/admin/setting")
public class SettingController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingController.class);

    @Resource
    private OptionService optionService;

    @Resource
    private LogService logService;

    /**
     * 进入设置页
     */
    @GetMapping(value = "")
    public String setting(HttpServletRequest request) {
        List<ConfigPO> optionsList = optionService.getOptions();
        Map<String, String> options = new HashMap<>();
        optionsList.forEach((option) -> options.put(option.getCode(), option.getValue()));
        request.setAttribute("options", options);
        return "admin/setting";
    }

    /**
     * 保存系统设置
     */
    @PostMapping(value = "")
    @ResponseBody
    public Response<?> saveSetting(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> queries = new HashMap<>();
            parameterMap.forEach((key, value) -> queries.put(key, join(value)));
            optionService.saveOptions(queries);

            //刷新设置
            List<ConfigPO> options = optionService.getOptions();
            if (!CollectionUtils.isEmpty(options)) {
                Constant.INIT_CONFIG.clear();
                for (ConfigPO option : options) {
                    Constant.INIT_CONFIG.put(option.getCode(), option.getValue());
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
