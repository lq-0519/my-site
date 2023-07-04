package cn.lq.service.site.impl;

import cn.lq.common.domain.constant.ErrorConstant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.dto.ArchiveDto;
import cn.lq.common.domain.dto.StatisticsDto;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.po.ContentPO;
import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.query.inner.AttachmentInnerQuery;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import cn.lq.common.domain.query.inner.ContentInnerQuery;
import cn.lq.common.domain.query.inner.MetaInnerQuery;
import cn.lq.common.domain.vo.ContentVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.BeanConverter;
import cn.lq.common.utils.DateKit;
import cn.lq.common.utils.PageUtils;
import cn.lq.manager.AttachmentManager;
import cn.lq.manager.CommentManager;
import cn.lq.manager.ContentManager;
import cn.lq.manager.MetaManager;
import cn.lq.service.site.SiteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点服务
 *
 * @author winterchen
 * @date 2018/4/30
 */
@Service
public class SiteServiceImpl implements SiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Resource
    private CommentManager commentManager;

    @Resource
    private ContentManager contentManager;

    @Resource
    private MetaManager metaManager;

    @Resource
    private AttachmentManager attachmentManager;

    @Override
    @Cacheable(value = "siteCache", key = "'comments_' + #p0")
    public List<CommentPO> getComments(int limit) {
        LOGGER.debug("Enter recentComments method:limit={}", limit);
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        PageHelper.startPage(1, limit);
        PageInfo<CommentPO> pageInfo = PageUtils.pack(1, limit, () -> commentManager.queryForList(new CommentInnerQuery()));
        LOGGER.debug("Exit recentComments method");
        return pageInfo.getList();
    }

    @Override
    @Cacheable(value = "siteCache", key = "'newArticles_' + #p0")
    public List<ContentVO> getNewArticles(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }

        List<ContentPO> rs = PageUtils.pack(1, limit, () -> contentManager.queryForList(new ContentInnerQuery())).getList();
        List<ContentVO> contentVOS = BeanConverter.convertToList(ContentVO.class, rs);
        //noinspection ConstantConditions
        for (ContentVO contentVO : contentVOS) {
            int count = commentManager.queryForCount(new CommentInnerQuery(contentVO.getId()));
            contentVO.setCommentsNum(count);
        }
        return contentVOS;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'comment_' + #p0")
    public CommentPO getComment(Long id) {
        LOGGER.debug("Enter recentComment method");
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }

        CommentPO comment = commentManager.queryForObject(id);
        LOGGER.debug("Exit recentComment method");
        return comment;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'statistics_'")
    public StatisticsDto getStatistics() {
        LOGGER.debug("Enter recentStatistics method");
        //文章总数
        int count = contentManager.queryForCount(new ContentInnerQuery());

        int comments = commentManager.queryForCount(new CommentInnerQuery());
        MetaInnerQuery metaInnerQuery = new MetaInnerQuery();
        metaInnerQuery.setType(Types.LINK.getType());
        int links = metaManager.queryForCount(metaInnerQuery);

        int atts = attachmentManager.queryForCount(new AttachmentInnerQuery());

        StatisticsDto rs = new StatisticsDto();
        rs.setArticles(count);
        rs.setAttachs(atts);
        rs.setComments(comments);
        rs.setLinks(links);

        LOGGER.debug("Exit recentStatistics method");
        return rs;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'archives_' + #p0")
    public List<ArchiveDto> getArchives(ContentInnerQuery contentInnerQuery) {
        LOGGER.debug("Enter getArchives method");
        List<ArchiveDto> archives = contentManager.getArchive(contentInnerQuery);
        parseArchives(archives, contentInnerQuery);
        LOGGER.debug("Exit getArchives method");
        return archives;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'metas_' + #p0")
    public List<MetaExtendPO> getMetas(String type, String orderBy, int limit) {
        LOGGER.debug("Enter metas method:type={},order={},limit={}", type, orderBy, limit);
        List<MetaExtendPO> retList = null;
        if (StringUtils.isNotBlank(type)) {
            if (StringUtils.isBlank(orderBy)) {
                orderBy = "count desc, a.mid desc";
            }
            if (limit < 1 || limit > WebConst.MAX_POSTS) {
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderBy);
            paraMap.put("limit", limit);
            retList = metaManager.selectFromSql(paraMap);
        }
        LOGGER.debug("Exit metas method");
        return retList;
    }

    private void parseArchives(List<ArchiveDto> archives, ContentInnerQuery contentInnerQuery) {
        if (null != archives) {
            archives.forEach(archive -> {
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
                Date end = DateKit.dateAdd(DateKit.INTERVAL_SECOND, DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1), -1);
                ContentInnerQuery cond = new ContentInnerQuery();
                cond.setStartTime(sd);
                cond.setEndTime(end);
                cond.setType(contentInnerQuery.getType());
                archive.setArticles(contentManager.queryForList(cond));
            });
        }
    }
}
