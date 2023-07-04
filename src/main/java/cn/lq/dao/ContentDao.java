package cn.lq.dao;

import cn.lq.common.domain.dto.ArchiveDto;
import cn.lq.common.domain.po.ContentPO;
import cn.lq.common.domain.query.inner.ContentInnerQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章持久层
 *
 * @author winterchen
 * @date 2018/4/29
 */
public interface ContentDao {

    /**
     * 添加文章
     */
    int insert(ContentPO contentPO);

    /**
     * 根据编号删除文章
     */
    int delete(@Param("id") Long id);

    /**
     * 更新文章
     */
    int update(ContentPO contentPO);

    /**
     * 根据编号获取文章
     */
    ContentPO queryForObject(@Param("id") Long id);

    /**
     * 根据条件获取文章列表
     */
    List<ContentPO> queryForList(ContentInnerQuery contentInnerQuery);

    /**
     * 获取文章总数量
     */
    int queryForCount(ContentInnerQuery contentInnerQuery);

    /**
     * 获取归档数据
     *
     * @param contentInnerQuery 查询条件（只包含开始时间和结束时间）
     */
    List<ArchiveDto> getArchive(ContentInnerQuery contentInnerQuery);

}
