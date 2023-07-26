package cn.lq.web.controller;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.es.ContentEsInnerQuery;
import cn.lq.common.domain.vo.ContentVO;
import cn.lq.common.domain.vo.PageVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.NetKit;
import cn.lq.common.utils.PageUtils;
import cn.lq.common.utils.Response;
import cn.lq.common.utils.TaleUtils;
import cn.lq.service.comment.CommentService;
import cn.lq.service.content.ContentService;
import com.alibaba.fastjson.JSON;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

/**
 * 首页和关于我的页面控制器
 *
 * @author winterchen
 * @date 2018/4/28
 */
@Controller
public class HomeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    @Resource
    private ContentService contentService;
    @Resource
    private CommentService commentService;


    /**
     * blog首页
     */
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "11") int limit) {
        return this.blogIndex(request, 1, limit, null);
    }

    /**
     * blog首页-分页
     */
    @GetMapping(value = "/blog/page")
    public String blogIndex(
            HttpServletRequest request,
            int page,
            @RequestParam(value = "limit", required = false, defaultValue = "11") int limit,
            String content) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        ContentEsInnerQuery contentEsInnerQuery = new ContentEsInnerQuery();
        contentEsInnerQuery.setType(Types.ARTICLE.getType());
        contentEsInnerQuery.setContent("null".equals(content) || StringUtils.isBlank(content) ? null : content);
        PageVO<ContentEsPO> articles = contentService.queryContentPage(contentEsInnerQuery, page, limit);
        //文章列表
        request.setAttribute("articles", articles);
        request.setAttribute("type", "articles");
        request.setAttribute("active", "blog");
        request.setAttribute("query", contentEsInnerQuery);
        return "site/blog";
    }

    /**
     * 文章内容页
     */
    @GetMapping(value = "/blog/article")
    public String getArticleDetail(ContentEsInnerQuery query, HttpServletRequest request) {
        ContentVO article = Optional.ofNullable(contentService.queryContentPageWithComment(query, 1, 1))
                .filter(PageUtils::isNotEmpty)
                .map(v -> v.getList().get(0))
                .orElse(new ContentVO());
        contentService.updateArticleHit(article.getId(), article.getHits());
        List<CommentPO> comments = commentService.getCommentsByContentId(query.getId());
        request.setAttribute("article", article);
        request.setAttribute("comments", comments);
        request.setAttribute("active", "blog");
        return "site/blog-details";
    }

    /**
     * 搜索文章
     */
    @RequestMapping(value = "/blog/search")
    public String search(ContentEsInnerQuery query,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int pageSize,
                         HttpServletRequest request) {
        PageVO<ContentEsPO> contentEsPOPageVO = contentService.queryContentPage(query, page, pageSize);
        LOGGER.info("search contentEsPOPageVO:{}", JSON.toJSON(contentEsPOPageVO));
        //文章列表
        request.setAttribute("articles", contentEsPOPageVO);
        request.setAttribute("type", "articles");
        request.setAttribute("active", "blog");
        request.setAttribute("query", query);
        return "site/blog";
    }

    /**
     * work首页
     */
    @GetMapping(value = {"/work/", "/work/index"})
    public String workIndex(HttpServletRequest request,
                            @ApiParam(name = "limit", value = "页数") @RequestParam(name = "limit", required = false, defaultValue = "12") int limit) {
        return this.workIndex(1, limit, request);
    }

    @GetMapping(value = {"/about", "/about/index"})
    public String getAbout(HttpServletRequest request) {
        //获取友链
        this.blogBaseData(request, null);
        request.setAttribute("active", "about");
        return "site/about";
    }

    /**
     * 分类
     */
    @GetMapping(value = "/blog/categories/{category}")
    public String categories(
            @ApiParam(name = "category", value = "分类名", required = true) @PathVariable("category") String category,
            HttpServletRequest request) {
        return this.categories(category, 1, 10, request);
    }

    /**
     * 分类-分页
     */
    @GetMapping(value = "/blog/categories/{category}/page/{page}")
    public String categories(
            @ApiParam(name = "category", value = "分类名", required = true) @PathVariable("category") String category,
            @ApiParam(name = "page", value = "页数", required = true) @PathVariable("page") int page,
            @ApiParam(name = "limit", value = "条数", required = true) @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            HttpServletRequest request) {
        ContentEsInnerQuery contentEsInnerQuery = new ContentEsInnerQuery();
        contentEsInnerQuery.setType(Types.ARTICLE.getType());
        contentEsInnerQuery.setCategory(category);
        PageVO<ContentEsPO> articles = contentService.queryContentPage(contentEsInnerQuery, page, limit);
//        this.blogBaseData(request,contentEsInnerQuery);//获取公共分类标签等数据
        request.setAttribute("articles_list", articles);
        request.setAttribute("type", "categories");
        request.setAttribute("param_name", category);
        return "blog/categories";
    }

    /**
     * 标签页
     */
    @GetMapping(value = "/blog/tag/{tag}")
    public String tags(
            @ApiParam(name = "tag", value = "标签名", required = true) @PathVariable("tag") String tag,
            HttpServletRequest request) {
        return this.tags(tag, 1, 10, request);
    }

    @ApiOperation("标签页-分页")
    @GetMapping(value = "/blog/tag/{tag}/page/{page}")
    public String tags(
            @ApiParam(name = "tag", value = "标签名", required = true) @PathVariable("tag") String tag,
            @ApiParam(name = "page", value = "页数", required = true) @PathVariable("page") int page,
            @ApiParam(name = "limit", value = "条数") @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            HttpServletRequest request) {
        ContentEsInnerQuery contentEsInnerQuery = new ContentEsInnerQuery();
        contentEsInnerQuery.setTag(tag);
        contentEsInnerQuery.setType(Types.ARTICLE.getType());
        PageVO<ContentEsPO> articles = contentService.queryContentPage(contentEsInnerQuery, page, limit);
//        this.blogBaseData(request,contentEsInnerQuery);//获取公共分类标签等数据
        request.setAttribute("articles_list", articles);
        request.setAttribute("type", "tag");
        request.setAttribute("param_name", tag);
        return "blog/categories";
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Response<?> comment(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(name = "cid") Long cid,
                               @RequestParam(name = "coid", required = false) Long commentId,
                               @RequestParam(name = "author", required = false) String author,
                               @RequestParam(name = "mail", required = false) String mail,
                               @RequestParam(name = "text") String text,
                               @RequestParam(name = "_csrf_token") String csrfToken) {

        String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(csrfToken)) {
            return Response.fail("访问失败");
        }

        String token = cache.hget(Types.CSRF_TOKEN.getType(), csrfToken);
        if (StringUtils.isBlank(token)) {
            return Response.fail("访问失败");
        }

        if (null == cid || StringUtils.isBlank(text)) {
            return Response.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return Response.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
            return Response.fail("请输入正确的邮箱格式");
        }

        String val = NetKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return Response.fail("您发表评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        CommentPO comments = new CommentPO();
        comments.setAuthor(author);
        comments.setContentId(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(commentId);
        try {
            commentService.addComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(), val, 1, 60);

            return Response.success();
        } catch (Exception e) {
            LOGGER.error("comment 评论出现异常", e);
            throw BusinessException.withErrorCode(Constant.Comment.ADD_NEW_COMMENT_FAIL);
        }
    }

    /**
     * 注销
     */
    @RequestMapping("/blog/logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        TaleUtils.logout(session, response);
    }

    @ApiOperation("作品主页-分页")
    @GetMapping(value = "/photo/page/{p}")
    public String workIndex(
            @ApiParam(name = "page", value = "页数") @PathVariable(name = "p") int page,
            @ApiParam(name = "limit", value = "条数") @RequestParam(name = "limit", required = false, defaultValue = "9999") int limit,
            HttpServletRequest request) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        ContentEsInnerQuery contentEsInnerQuery = new ContentEsInnerQuery();
        contentEsInnerQuery.setType(Types.PHOTO.getType());
        PageVO<ContentEsPO> articles = contentService.queryContentPage(contentEsInnerQuery, page, limit);
        request.setAttribute("archives", articles);
        request.setAttribute("active", "work");
        return "site/index";
    }

    @ApiOperation("作品内容")
    @GetMapping(value = "/photo/article/{cid}")
    public String article(@PathVariable("cid") Long cid, HttpServletRequest request) {
        ContentEsPO article = contentService.getArticleById(cid);
        contentService.updateArticleHit(article.getId(), article.getHits());
        request.setAttribute("archive", article);
        request.setAttribute("active", "work");
        return "site/works-details";
    }

    /**
     * 设置cookie
     */
    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }
}
