package cn.lq.service.site;

import cn.lq.common.domain.dto.StatisticsDto;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.vo.ContentVO;

import java.util.List;

/**
 * 站点服务
 *
 * @author winterchen
 * @date 2018/4/30
 */
public interface SiteService {

    /**
     * 获取评论列表
     */
    List<CommentPO> getComments(int limit);

    /**
     * 获取最新的文章
     */
    List<ContentVO> getNewArticles(int limit);

    /**
     * 获取单条评论
     */
    CommentPO getComment(Long id);

    /**
     * 获取 后台统计数据
     */
    StatisticsDto getStatistics();

    /**
     * 获取分类/标签列表
     */
    List<MetaExtendPO> getMetas(String type, String orderBy, int limit);
}
