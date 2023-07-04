package cn.lq.web.controller.admin;

import cn.lq.common.domain.bo.ContentBO;
import cn.lq.common.domain.constant.LogActions;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.po.ContentPO;
import cn.lq.common.domain.po.MetaPO;
import cn.lq.common.domain.query.inner.ContentInnerQuery;
import cn.lq.common.domain.query.inner.MetaInnerQuery;
import cn.lq.common.domain.vo.ContentVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.Response;
import cn.lq.service.content.ContentService;
import cn.lq.service.log.LogService;
import cn.lq.service.meta.MetaService;
import cn.lq.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章管理
 *
 * @author winterchen
 * @date 2018/4/30
 */
@Api("文章管理")
@Controller
@RequestMapping("/admin/article")
@Transactional(rollbackFor = BusinessException.class)
public class ArticleController extends BaseController {

    @Resource
    private ContentService contentService;

    @Resource
    private MetaService metaService;

    @Resource
    private LogService logService;


    @ApiOperation("文章页")
    @GetMapping(value = "")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "页数") @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @ApiParam(name = "limit", value = "每页数量") @RequestParam(name = "limit", required = false, defaultValue = "15") int limit) {
        PageInfo<ContentVO> articles = contentService.queryContentPage(new ContentInnerQuery(), page, limit);
        request.setAttribute("articles", articles);
        return "admin/article_list";
    }


    @ApiOperation("发布文章页")
    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {
        MetaInnerQuery metaInnerQuery = new MetaInnerQuery();
        metaInnerQuery.setType(Types.CATEGORY.getType());
        List<MetaPO> metas = metaService.getMetas(metaInnerQuery);
        request.setAttribute("categories", metas);
        return "admin/article_edit";
    }

    @ApiOperation("发布新文章")
    @PostMapping(value = "/publish")
    @ResponseBody
    public Response<?> publishArticle(
            @ApiParam(name = "title", value = "标题", required = true) @RequestParam(name = "title") String title,
            @ApiParam(name = "titlePic", value = "标题图片") @RequestParam(name = "titlePic", required = false) String titlePic,
            @ApiParam(name = "slug", value = "内容缩略名") @RequestParam(name = "slug", required = false) String slug,
            @ApiParam(name = "content", value = "内容", required = true) @RequestParam(name = "content") String content,
            @ApiParam(name = "type", value = "文章类型", required = true) @RequestParam(name = "type") String type,
            @ApiParam(name = "status", value = "文章状态", required = true) @RequestParam(name = "status") String status,
            @ApiParam(name = "tags", value = "标签") @RequestParam(name = "tags", required = false) String tags,
            @ApiParam(name = "categories", value = "分类") @RequestParam(name = "categories", required = false, defaultValue = "默认分类") String categories,
            @ApiParam(name = "allowComment", value = "是否允许评论", required = true) @RequestParam(name = "allowComment") Boolean allowComment) {
        ContentBO contentBO = new ContentBO();
        contentBO.setTitle(title);
        contentBO.setTitlePic(titlePic);
        contentBO.setSlug(slug);
        contentBO.setContent(content);
        contentBO.setType(type);
        contentBO.setStatus(status);
        contentBO.setTags(type.equals(Types.ARTICLE.getType()) ? tags : null);
        //只允许博客文章有分类，防止作品被收入分类
        contentBO.setCategories(type.equals(Types.ARTICLE.getType()) ? categories : null);
        contentBO.setAllowComment(allowComment ? 1 : 0);

        contentService.addArticle(contentBO);
        return Response.success();
    }

    @ApiOperation("文章编辑页")
    @GetMapping(value = "/{cid}")
    public String editArticle(
            @ApiParam(name = "cid", value = "文章编号", required = true) @PathVariable Long cid,
            HttpServletRequest request) {
        ContentPO content = contentService.getArticleById(cid);
        request.setAttribute("contents", content);
        MetaInnerQuery metaInnerQuery = new MetaInnerQuery();
        metaInnerQuery.setType(Types.CATEGORY.getType());
        List<MetaPO> categories = metaService.getMetas(metaInnerQuery);
        request.setAttribute("categories", categories);
        request.setAttribute("active", "article");
        return "admin/article_edit";
    }

    @ApiOperation("编辑保存文章")
    @PostMapping("/modify")
    @ResponseBody
    public Response<?> modifyArticle(@ApiParam(name = "cid", value = "文章主键", required = true) @RequestParam(name = "cid") Long cid,
                                     @ApiParam(name = "title", value = "标题", required = true) @RequestParam(name = "title") String title,
                                     @ApiParam(name = "titlePic", value = "标题图片") @RequestParam(name = "titlePic", required = false) String titlePic,
                                     @ApiParam(name = "slug", value = "内容缩略名") @RequestParam(name = "slug", required = false) String slug,
                                     @ApiParam(name = "content", value = "内容", required = true) @RequestParam(name = "content") String content,
                                     @ApiParam(name = "type", value = "文章类型", required = true) @RequestParam(name = "type") String type,
                                     @ApiParam(name = "status", value = "文章状态", required = true) @RequestParam(name = "status") String status,
                                     @ApiParam(name = "tags", value = "标签") @RequestParam(name = "tags", required = false) String tags,
                                     @ApiParam(name = "categories", value = "分类") @RequestParam(name = "categories", required = false, defaultValue = "默认分类") String categories,
                                     @ApiParam(name = "allowComment", value = "是否允许评论", required = true) @RequestParam(name = "allowComment") Boolean allowComment) {
        ContentBO contentBO = new ContentBO();
        contentBO.setId(cid);
        contentBO.setTitle(title);
        contentBO.setTitlePic(titlePic);
        contentBO.setSlug(slug);
        contentBO.setContent(content);
        contentBO.setType(type);
        contentBO.setStatus(status);
        contentBO.setTags(tags);
        contentBO.setCategories(categories);
        contentBO.setAllowComment(allowComment ? 1 : 0);

        contentService.updateArticleById(contentBO);
        return Response.success();
    }

    @ApiOperation("删除文章")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Response<?> deleteArticle(
            @ApiParam(name = "cid", value = "文章主键", required = true) @RequestParam(name = "cid") Long cid,
            HttpServletRequest request) {
        contentService.deleteArticleById(cid);
        logService.addLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        return Response.success();
    }
}
