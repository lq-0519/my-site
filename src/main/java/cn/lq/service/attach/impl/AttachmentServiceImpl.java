package cn.lq.service.attach.impl;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.po.AttachmentPO;
import cn.lq.common.domain.po.AttachmentUserPO;
import cn.lq.common.domain.po.UserPO;
import cn.lq.common.domain.query.inner.AttachmentInnerQuery;
import cn.lq.common.domain.vo.AttachmentVO;
import cn.lq.common.domain.vo.PageVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.BeanConverter;
import cn.lq.common.utils.CollectionUtils;
import cn.lq.common.utils.PageUtils;
import cn.lq.common.utils.TaleUtils;
import cn.lq.manager.AttachmentManager;
import cn.lq.service.api.QiniuOSSService;
import cn.lq.service.attach.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 附件服务实现层
 *
 * @author winterchen
 * @date 2018/4/29
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Resource
    private QiniuOSSService qiniuOSSService;
    @Resource
    private AttachmentManager attachmentManager;

    @Override
    public String addAttAch(MultipartFile file, UserPO sessionUser) {
        String key = qiniuOSSService.uploadFile(file);
        AttachmentPO attAch = new AttachmentPO();
        attAch.setUserId(sessionUser.getId());
        try {
            attAch.setFileType(TaleUtils.isImage(file.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        attAch.setFileName(key);
        String fileUrl = qiniuOSSService.getFileUrl(key);
        attAch.setFileKey(key);
        attachmentManager.insert(attAch);
        return fileUrl;
    }

    @Override
    public void deleteAttAch(Long id) {
        if (null == id) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        AttachmentPO attAch = attachmentManager.queryForObject(id);
        qiniuOSSService.deleteFile(attAch.getFileKey());
        attachmentManager.delete(id);
    }

    @Override
    public void updateAttAch(AttachmentPO attachmentPO) {
        if (null == attachmentPO || null == attachmentPO.getId()) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        attachmentManager.update(attachmentPO);
    }

    @Override
    public AttachmentUserPO getAttachmentUserById(Long id) {
        if (null == id) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        return attachmentManager.queryAttachmentUserById(id);
    }

    @Override
    public PageVO<AttachmentVO> getAttachmentUserPage(int pageNum, int pageSize) {
        PageVO<AttachmentUserPO> userPOPageVO = PageUtils.pack(pageNum, pageSize, () -> attachmentManager.queryAttachmentUserList(new AttachmentInnerQuery()));
        @SuppressWarnings("unchecked") PageVO<AttachmentVO> pageVO = BeanConverter.convert(PageVO.class, userPOPageVO);
        List<AttachmentVO> attachmentVOS = Optional.of(userPOPageVO)
                .map(PageVO::getList)
                .filter(CollectionUtils::isNotEmpty)
                .map(v -> BeanConverter.convertToList(AttachmentVO.class, v))
                .orElse(new ArrayList<>());
        for (AttachmentVO attachmentVO : attachmentVOS) {
            attachmentVO.setFileUrl(qiniuOSSService.getFileUrl(attachmentVO.getFileKey()));
        }
        pageVO.setList(attachmentVOS);
        return pageVO;
    }
}
