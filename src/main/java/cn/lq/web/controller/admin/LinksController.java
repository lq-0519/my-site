package cn.lq.web.controller.admin;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.po.MetaPO;
import cn.lq.common.domain.query.inner.MetaInnerQuery;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.Response;
import cn.lq.service.meta.MetaService;
import cn.lq.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api("友链")
@Controller
@RequestMapping(value = "admin/links")
public class LinksController extends BaseController {

    @Resource
    private MetaService metaService;

    @ApiOperation("友链页面")
    @GetMapping(value = "")
    public String index(HttpServletRequest request) {

        MetaInnerQuery metaInnerQuery = new MetaInnerQuery();
        metaInnerQuery.setType(Types.LINK.getType());
        List<MetaPO> metas = metaService.getMetas(metaInnerQuery);
        request.setAttribute("links", metas);
        return "admin/links";
    }

    @ApiOperation("新增友链")
    @PostMapping(value = "save")
    @ResponseBody
    public Response<?> addLink(
            @ApiParam(name = "title", value = "标签", required = true) @RequestParam(name = "title") String title,
            @ApiParam(name = "url", value = "链接", required = true) @RequestParam(name = "url") String url,
            @ApiParam(name = "logo", value = "logo") @RequestParam(name = "logo", required = false) String logo,
            @ApiParam(name = "mid", value = "meta编号") @RequestParam(name = "mid", required = false) Long mid,
            @ApiParam(name = "sort", value = "sort") @RequestParam(name = "sort", required = false, defaultValue = "0") int sort) {
        try {
            MetaPO meta = new MetaPO();
            meta.setName(title);
            meta.setSlug(url);
            meta.setDescription(logo);
            meta.setSort(sort);
            meta.setType(Types.LINK.getType());
            if (null != mid) {
                meta.setId(mid);
                metaService.updateMeta(meta);
            } else {
                metaService.addMeta(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(Constant.Meta.ADD_META_FAIL);
        }
        return Response.success();
    }


    @ApiOperation("删除友链")
    @PostMapping(value = "delete")
    @ResponseBody
    public Response<?> delete(
            @ApiParam(name = "mid", value = "meta主键", required = true) @RequestParam(name = "mid") Long id) {
        try {
            metaService.deleteMetaById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(Constant.Meta.ADD_META_FAIL);
        }

        return Response.success();
    }
}
