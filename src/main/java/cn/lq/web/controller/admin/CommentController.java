package cn.lq.web.controller.admin;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import cn.lq.common.domain.vo.PageVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.Response;
import cn.lq.service.comment.CommentService;
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

/**
 * 评论
 *
 * @author winterchen
 * @date 2018/4/30
 */
@Api("评论相关接口")
@Controller
@RequestMapping("/admin/comments")
public class CommentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    @Resource
    private CommentService commentService;

    @ApiOperation("进入评论列表页")
    @GetMapping(value = "")
    public String index(
            @ApiParam(name = "page", value = "页数") @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @ApiParam(name = "limit", value = "每页条数") @RequestParam(name = "limit", required = false, defaultValue = "15") int limit,
            HttpServletRequest request) {
        PageVO<CommentPO> comments = commentService.getCommentsByCond(new CommentInnerQuery(), page, limit);
        request.setAttribute("comments", comments);
        return "admin/comment_list";
    }

    @ApiOperation("删除一条评论")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Response deleteComment(
            @ApiParam(name = "coid", value = "评论编号", required = true) @RequestParam(name = "coid") Long commentId) {

        try {
            CommentPO comment = commentService.getCommentById(commentId);
            if (null == comment) {
                throw BusinessException.withErrorCode(Constant.Comment.COMMENT_NOT_EXIST);
            }

            commentService.deleteComment(commentId);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return Response.fail(e.getMessage());
        }
        return Response.success();
    }

    @ApiOperation("更改评论状态")
    @PostMapping(value = "/status")
    @ResponseBody
    public Response<?> changeStatus(
            @ApiParam(name = "coid", value = "评论主键", required = true) @RequestParam(name = "coid") Long commentId,
            @ApiParam(name = "status", value = "状态", required = true) @RequestParam(name = "status") String status) {
        try {
            CommentPO comment = commentService.getCommentById(commentId);
            if (null != comment) {
                commentService.updateCommentStatus(commentId, status);
            } else {
                return Response.fail("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return Response.fail(e.getMessage());
        }
        return Response.success();
    }
}
