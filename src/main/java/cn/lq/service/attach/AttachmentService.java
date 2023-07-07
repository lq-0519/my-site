package cn.lq.service.attach;

import cn.lq.common.domain.po.AttachmentPO;
import cn.lq.common.domain.po.AttachmentUserPO;
import cn.lq.common.domain.vo.PageVO;

/**
 * 附件服务层
 *
 * @author winterchen
 * @date 2018/4/29
 */
public interface AttachmentService {

    /**
     * 添加单个附件信息
     */
    void addAttAch(AttachmentPO attachmentPO);

    /**
     * 根据主键编号删除附件信息
     */
    void deleteAttAch(Long id);

    /**
     * 更新附件信息
     */
    void updateAttAch(AttachmentPO attachmentPO);

    /**
     * 根据主键获取附件信息
     */
    AttachmentUserPO getAttachmentUserById(Long id);

    /**
     * 获取所有的附件信息
     */
    PageVO<AttachmentUserPO> getAttachmentUserPage(int pageNum, int pageSize);
}
