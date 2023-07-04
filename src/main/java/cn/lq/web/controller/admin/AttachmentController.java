package cn.lq.web.controller.admin;

import cn.lq.common.domain.constant.ErrorConstant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.po.AttachmentPO;
import cn.lq.common.domain.po.AttachmentUserPO;
import cn.lq.common.domain.po.UserPO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.Commons;
import cn.lq.common.utils.Response;
import cn.lq.common.utils.TaleUtils;
import cn.lq.service.api.QiniuCloudService;
import cn.lq.service.attach.AttachmentService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 附件控制器
 *
 * @author winterchen
 * @date 2018/4/30
 */
@Api("附件相关接口")
@Controller
@RequestMapping("admin/attach")
public class AttachmentController {

    public static final String CLASSPATH = TaleUtils.getUplodFilePath();
    @Resource
    private AttachmentService attachmentService;
    @Resource
    private QiniuCloudService qiniuCloudService;


    @ApiOperation("文件管理首页")
    @GetMapping(value = "")
    public String index(
            @ApiParam(name = "page", value = "页数") @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @ApiParam(name = "limit", value = "条数") @RequestParam(name = "limit", required = false, defaultValue = "12") int limit,
            HttpServletRequest request) {
        PageInfo<AttachmentUserPO> atts = attachmentService.getAttachmentUserPage(page, limit);
        request.setAttribute("attachs", atts);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType(), Commons.site_url()));
        request.setAttribute("max_file_size", WebConst.MAX_FILE_SIZE / 1024);
        return "admin/attach";
    }


    @ApiOperation("markdown文件上传")
    @PostMapping("/uploadfile")
    public void fileUpLoadToTencentCloud(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @ApiParam(name = "editormd-image-file", value = "文件数组", required = true) @RequestParam(name = "editormd-image-file", required = true) MultipartFile file) {
        //文件上传
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            String fileName = TaleUtils.getFileKey(file.getOriginalFilename()).replaceFirst("/", "");
            qiniuCloudService.upload(file, fileName);
            AttachmentPO attAch = new AttachmentPO();
            HttpSession session = request.getSession();
            UserPO sessionUser = (UserPO) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            attAch.setUserId(sessionUser.getId());
            attAch.setFileType(TaleUtils.isImage(file.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType());
            attAch.setFileName(fileName);
            String baseUrl = qiniuCloudService.QINIU_UPLOAD_SITE.endsWith("/") ? qiniuCloudService.QINIU_UPLOAD_SITE : qiniuCloudService.QINIU_UPLOAD_SITE + "/";
            attAch.setFileKey(baseUrl + fileName);
            attachmentService.addAttAch(attAch);
            response.getWriter().write("{\"success\": 1, \"message\":\"上传成功\",\"url\":\"" + attAch.getFileKey() + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.getWriter().write("{\"success\":0}");
            } catch (IOException e1) {
                throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL).withErrorMessageArguments(e.getMessage());
            }
            throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL).withErrorMessageArguments(e.getMessage());
        }
    }

    @ApiOperation("多文件上传")
    @PostMapping(value = "upload")
    @ResponseBody
    public Response<?> uploadFilesUploadToCloud(HttpServletRequest request, HttpServletResponse response,
                                                @ApiParam(name = "file", value = "文件数组", required = true) @RequestParam(name = "file") MultipartFile[] files) {
        //文件上传
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");

            for (MultipartFile file : files) {

                String fileName = TaleUtils.getFileKey(file.getOriginalFilename()).replaceFirst("/", "");

                qiniuCloudService.upload(file, fileName);
                AttachmentPO attAch = new AttachmentPO();
                HttpSession session = request.getSession();
                UserPO sessionUser = (UserPO) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
                attAch.setUserId(sessionUser.getId());
                attAch.setFileType(TaleUtils.isImage(file.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType());
                attAch.setFileName(fileName);
                String baseUrl = qiniuCloudService.QINIU_UPLOAD_SITE.endsWith("/") ? qiniuCloudService.QINIU_UPLOAD_SITE : qiniuCloudService.QINIU_UPLOAD_SITE + "/";
                attAch.setFileKey(baseUrl + fileName);
                attachmentService.addAttAch(attAch);
            }
            return Response.success();
        } catch (IOException e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL)
                    .withErrorMessageArguments(e.getMessage());
        }
    }

    @ApiOperation("删除文件记录")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Response<?> deleteFileInfo(
            @ApiParam(name = "id", value = "文件主键", required = true) @RequestParam(name = "id") Long id) {
        try {
            AttachmentUserPO attAch = attachmentService.getAttachmentUserById(id);
            if (null == attAch) {
                throw BusinessException.withErrorCode(ErrorConstant.Att.DELETE_ATT_FAIL + ": 文件不存在");
            }

            attachmentService.deleteAttAch(id);
            return Response.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(e.getMessage());
        }
    }
}
