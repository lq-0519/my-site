package cn.lq.web.controller.admin;

import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.Response;
import cn.lq.service.meta.MetaService;
import cn.lq.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.List;

/**
 * @author winterchen
 * @date 2018/5/1
 */
@Api("分类和标签")
@Controller
@RequestMapping("admin/category")
public class CategoryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Resource
    private MetaService metaService;

    @ApiOperation("进入分类和标签页")
    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        List<MetaExtendPO> categories = metaService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
        List<MetaExtendPO> tags = metaService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        request.setAttribute("categories", categories);
        request.setAttribute("tags", tags);
        return "admin/category";
    }

    @ApiOperation("保存分类")
    @PostMapping(value = "save")
    @ResponseBody
    public Response addCategory(
            @ApiParam(name = "cname", value = "分类名", required = true) @RequestParam(name = "cname") String cname,
            @ApiParam(name = "mid", value = "meta编号") @RequestParam(name = "mid", required = false) Long metaId) {
        try {
            metaService.saveMeta(Types.CATEGORY.getType(), cname, metaId);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "分类保存失败";
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                msg = ex.getErrorCode();
            }
            LOGGER.error(msg, e);
            return Response.fail(msg);
        }

        return Response.success();
    }

    @ApiOperation("删除分类")
    @PostMapping(value = "delete")
    @ResponseBody
    public Response delete(
            @ApiParam(name = "mid", value = "主键", required = true) @RequestParam(name = "mid") Long id) {
        try {
            metaService.deleteMetaById(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return Response.fail(e.getMessage());
        }

        return Response.success();
    }
}
