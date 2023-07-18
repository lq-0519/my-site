package cn.lq.service.site.impl;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.dto.StatisticsDto;
import cn.lq.common.domain.po.CommentPO;
import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.AttachmentInnerQuery;
import cn.lq.common.domain.query.inner.CommentInnerQuery;
import cn.lq.common.domain.query.inner.MetaInnerQuery;
import cn.lq.common.domain.query.inner.es.ContentEsInnerQuery;
import cn.lq.common.domain.vo.ContentVO;
import cn.lq.common.domain.vo.PageVO;
import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.BeanConverter;
import cn.lq.common.utils.PageUtils;
import cn.lq.manager.AttachmentManager;
import cn.lq.manager.CommentManager;
import cn.lq.manager.ContentManager;
import cn.lq.manager.MetaManager;
import cn.lq.service.site.SiteService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public List<CommentPO> getComments(int limit) {
        LOGGER.debug("Enter recentComments method:limit={}", limit);
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        PageHelper.startPage(1, limit);
        PageVO<CommentPO> pageInfo = PageUtils.pack(1, limit, () -> commentManager.queryForList(new CommentInnerQuery()));
        LOGGER.debug("Exit recentComments method");
        return pageInfo.getList();
    }

    @Override
    public List<ContentVO> getNewArticles(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }

        List<ContentEsPO> rs = contentManager.queryForPage(new ContentEsInnerQuery(), 1, limit).getContent();
        List<ContentVO> contentVOS = BeanConverter.convertToList(ContentVO.class, rs);
        for (ContentVO contentVO : contentVOS) {
            int count = commentManager.queryForCount(new CommentInnerQuery(contentVO.getId()));
            contentVO.setCommentsNum(count);
        }
        return contentVOS;
    }

    @Override
    public CommentPO getComment(Long id) {
        LOGGER.debug("Enter recentComment method");
        if (null == id) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        CommentPO comment = commentManager.queryForObject(id);
        LOGGER.debug("Exit recentComment method");
        return comment;
    }

    @Override
    public StatisticsDto getStatistics() {
        LOGGER.debug("Enter recentStatistics method");
        //文章总数
        long count = contentManager.queryForCount(new ContentEsInnerQuery());

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

}
