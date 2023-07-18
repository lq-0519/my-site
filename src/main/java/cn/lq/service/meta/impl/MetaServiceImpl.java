package cn.lq.service.meta.impl;

import cn.lq.common.domain.constant.Constant;
import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.po.ContentMetaBindPO;
import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.po.MetaPO;
import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.ContentMetaBindInnerQuery;
import cn.lq.common.domain.query.inner.MetaInnerQuery;
import cn.lq.common.exception.BusinessException;
import cn.lq.manager.ContentMetaBindManager;
import cn.lq.manager.MetaManager;
import cn.lq.service.content.ContentService;
import cn.lq.service.meta.MetaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MetaServiceImpl
 *
 * @author winterchen
 * @date 2018/4/29
 */
@Service
@Transactional
public class MetaServiceImpl implements MetaService {

    @Resource
    private MetaManager metaManager;

    @Resource
    private ContentMetaBindManager contentMetaBindManager;


    @Resource
    private ContentService contentService;

    @Override
    public void addMeta(MetaPO meta) {
        if (null == meta) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }
        metaManager.insert(meta);

    }

    @Override
    public void saveMeta(String type, String name, Long metaId) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            MetaInnerQuery metaInnerQuery = new MetaInnerQuery();
            metaInnerQuery.setName(name);
            metaInnerQuery.setType(type);
            List<MetaPO> metas = metaManager.queryForList(metaInnerQuery);
            if (null == metas || metas.size() == 0) {
                MetaPO metaPO = new MetaPO();
                metaPO.setName(name);
                if (null != metaId) {
                    MetaPO meta = metaManager.queryForObject(metaId);
                    if (null != meta) {
                        metaPO.setId(metaId);
                    }

                    metaManager.update(metaPO);
                    //更新原有的文章分类
                    if (meta != null) {
                        contentService.updateCategory(meta.getName(), name);
                    }
                } else {
                    metaPO.setType(type);
                    metaManager.insert(metaPO);
                }
            } else {
                throw BusinessException.withErrorCode(Constant.Meta.META_IS_EXIST);

            }

        }
    }

    @Override
    public void addMetas(Long cid, String names, String type) {
        if (null == cid) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    @Override
    public void saveOrUpdate(Long cid, String name, String type) {
        MetaInnerQuery metaInnerQuery = new MetaInnerQuery();
        metaInnerQuery.setName(name);
        metaInnerQuery.setType(type);
        List<MetaPO> metas = this.getMetas(metaInnerQuery);

        Long mid;
        MetaPO metaPO;
        if (metas.size() == 1) {
            MetaPO meta = metas.get(0);
            mid = meta.getId();
        } else if (metas.size() > 1) {
            throw BusinessException.withErrorCode(Constant.Meta.NOT_ONE_RESULT);
        } else {
            metaPO = new MetaPO();
            metaPO.setSlug(name);
            metaPO.setName(name);
            metaPO.setType(type);
            this.addMeta(metaPO);
            mid = metaPO.getId();
        }
        if (mid != 0) {
            ContentMetaBindInnerQuery contentMetaBindInnerQuery = new ContentMetaBindInnerQuery();
            contentMetaBindInnerQuery.setContentId(cid);
            contentMetaBindInnerQuery.setMetaId(mid);
            int count = contentMetaBindManager.queryForCount(contentMetaBindInnerQuery);
            if (count == 0) {
                ContentMetaBindPO relationShip = new ContentMetaBindPO();
                relationShip.setContentId(cid);
                relationShip.setMetaId(mid);
                contentMetaBindManager.insert(relationShip);
            }
        }
    }

    @Override
    public void deleteMetaById(Long mid) {
        if (null == mid) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        MetaPO meta = metaManager.queryForObject(mid);
        if (null != meta) {
            String type = meta.getType();
            String name = meta.getName();
            metaManager.delete(mid);
            //需要把相关的数据删除
            ContentMetaBindInnerQuery contentMetaBindInnerQuery = new ContentMetaBindInnerQuery();
            contentMetaBindInnerQuery.setMetaId(mid);
            List<ContentMetaBindPO> relationShips = contentMetaBindManager.queryForList(contentMetaBindInnerQuery);
            if (null != relationShips && relationShips.size() > 0) {
                for (ContentMetaBindPO relationShip : relationShips) {
                    ContentEsPO article = contentService.getArticleById(relationShip.getContentId());
                    if (null != article) {
                        ContentEsPO temp = new ContentEsPO();
                        temp.setId(relationShip.getContentId());
                        if (type.equals(Types.CATEGORY.getType())) {
                            temp.setCategories(reMeta(name, article.getCategories()));
                        }
                        if (type.equals(Types.TAG.getType())) {
                            temp.setTags(reMeta(name, article.getTags()));
                        }
                        //将删除的资源去除
                        contentService.updateArticleById(temp);
                    }
                }
                ContentMetaBindInnerQuery delQuery = new ContentMetaBindInnerQuery();
                delQuery.setMetaId(mid);
                contentMetaBindManager.deleteByQuery(delQuery);
            }
        }


    }

    @Override
    public void updateMeta(MetaPO meta) {
        if (null == meta || null == meta.getId()) {
            throw BusinessException.withErrorCode(Constant.Common.PARAM_IS_EMPTY);
        }

        metaManager.update(meta);
    }

    @Override
    public List<MetaPO> getMetas(MetaInnerQuery metaInnerQuery) {
        return metaManager.queryForList(metaInnerQuery);
    }


    @Override
    public List<MetaExtendPO> getMetaList(String type, String orderby, int limit) {
        if (StringUtils.isNotBlank(type)) {
            if (StringUtils.isBlank(orderby)) {
                orderby = "count desc, a.mid desc";
            }
            if (limit < 1 || limit > WebConst.MAX_POSTS) {
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderby);
            paraMap.put("limit", limit);
            return metaManager.selectFromSql(paraMap);
        }
        return null;
    }

    private String reMeta(String name, String metas) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }
}
