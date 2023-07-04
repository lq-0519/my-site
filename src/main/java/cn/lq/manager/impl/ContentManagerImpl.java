package cn.lq.manager.impl;

import cn.lq.common.domain.dto.ArchiveDto;
import cn.lq.common.domain.po.ContentPO;
import cn.lq.common.domain.query.inner.ContentInnerQuery;
import cn.lq.dao.ContentDao;
import cn.lq.manager.ContentManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liqian477
 * @date 2023/06/28 10:25
 */
@Service("contentManager")
public class ContentManagerImpl implements ContentManager {
    @Resource
    private ContentDao contentDao;

    /**
     * 添加文章
     */
    @Override
    public int insert(ContentPO contentPO) {
        return contentDao.insert(contentPO);
    }

    /**
     * 根据编号删除文章
     */
    @Override
    public int delete(Long id) {
        return contentDao.delete(id);
    }

    /**
     * 更新文章
     */
    @Override
    public int update(ContentPO contentPO) {
        return contentDao.update(contentPO);
    }

    /**
     * 根据编号获取文章
     */
    @Override
    public ContentPO queryForObject(Long id) {
        return contentDao.queryForObject(id);
    }

    /**
     * 根据条件获取文章列表
     */
    @Override
    public List<ContentPO> queryForList(ContentInnerQuery contentInnerQuery) {
        return contentDao.queryForList(contentInnerQuery);
    }

    /**
     * 获取文章总数量
     */
    @Override
    public int queryForCount(ContentInnerQuery contentInnerQuery) {
        return contentDao.queryForCount(contentInnerQuery);
    }

    /**
     * 获取归档数据
     *
     * @param contentInnerQuery 查询条件（只包含开始时间和结束时间）
     */
    @Override
    public List<ArchiveDto> getArchive(ContentInnerQuery contentInnerQuery) {
        return contentDao.getArchive(contentInnerQuery);
    }
}
