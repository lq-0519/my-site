package cn.lq.dao.mysql;

import cn.lq.common.domain.po.AttachmentPO;
import cn.lq.common.domain.po.AttachmentUserPO;
import cn.lq.common.domain.query.inner.AttachmentInnerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author winterchen
 * @date 2018/4/29
 */
public interface AttachmentDao {

    /**
     * 添加单个附件信息
     */
    int insert(AttachmentPO attachmentPO);

    /**
     * 根据主键编号删除附件信息
     */
    int delete(Long id);

    /**
     * 更新附件信息
     */
    int update(AttachmentPO attachmentPO);

    /**
     * 根据主键获取附件信息
     */
    AttachmentUserPO queryAttachmentUserById(@Param("id") Long id);

    /**
     * 获取所有的附件信息
     */
    List<AttachmentUserPO> queryAttachmentUserList(AttachmentInnerQuery attachmentInnerQuery);

    /**
     * 查找附件的数量
     */
    int queryForCount(AttachmentInnerQuery attachmentInnerQuery);

    /**
     * queryForObject
     */
    AttachmentPO queryForObject(Long id);
}
