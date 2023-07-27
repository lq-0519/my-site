package cn.lq.service.attach.impl;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.po.AttachmentPO;
import cn.lq.common.domain.po.AttachmentUserPO;
import cn.lq.common.domain.query.inner.AttachmentInnerQuery;
import cn.lq.common.domain.vo.PageVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.PageUtils;
import cn.lq.manager.AttachmentManager;
import cn.lq.service.attach.AttachmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 附件服务实现层
 *
 * @author winterchen
 * @date 2018/4/29
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Resource
    private AttachmentManager attachmentManager;

    @Override
    public void addAttAch(AttachmentPO attachmentPO) {
        if (null == attachmentPO) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        attachmentManager.insert(attachmentPO);

    }

    @Override
    public void deleteAttAch(Long id) {
        if (null == id) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }
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
    public PageVO<AttachmentUserPO> getAttachmentUserPage(int pageNum, int pageSize) {
        return PageUtils.pack(pageNum, pageSize, () -> attachmentManager.queryAttachmentUserList(new AttachmentInnerQuery()));
    }
}