package cn.lq.manager.impl;

import cn.lq.common.domain.po.AttachmentPO;
import cn.lq.common.domain.po.AttachmentUserPO;
import cn.lq.common.domain.query.inner.AttachmentInnerQuery;
import cn.lq.dao.mysql.AttachmentDao;
import cn.lq.manager.AttachmentManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liqian477
 * @date 2023/07/04 18:27
 */
@Service("attachmentManager")
public class AttachmentManagerImpl implements AttachmentManager {
    @Resource
    private AttachmentDao attachmentDao;

    /**
     * 添加单个附件信息
     */
    @Override
    public int insert(AttachmentPO attachmentPO) {
        return attachmentDao.insert(attachmentPO);
    }

    /**
     * 根据主键编号删除附件信息
     */
    @Override
    public int delete(Long id) {
        return attachmentDao.delete(id);
    }

    /**
     * 更新附件信息
     */
    @Override
    public int update(AttachmentPO attachmentPO) {
        return attachmentDao.update(attachmentPO);
    }

    /**
     * 根据主键获取附件信息
     */
    @Override
    public AttachmentUserPO queryAttachmentUserById(Long id) {
        return attachmentDao.queryAttachmentUserById(id);
    }

    /**
     * 获取所有的附件信息
     */
    @Override
    public List<AttachmentUserPO> queryAttachmentUserList(AttachmentInnerQuery attachmentInnerQuery) {
        return attachmentDao.queryAttachmentUserList(attachmentInnerQuery);
    }

    /**
     * 查找附件的数量
     */
    @Override
    public int queryForCount(AttachmentInnerQuery attachmentInnerQuery) {
        return attachmentDao.queryForCount(attachmentInnerQuery);
    }
}
