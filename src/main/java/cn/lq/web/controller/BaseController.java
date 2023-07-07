package cn.lq.web.controller;

import cn.lq.common.domain.constant.Types;
import cn.lq.common.domain.constant.WebConst;
import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.po.UserPO;
import cn.lq.common.domain.query.inner.es.ContentEsInnerQuery;
import cn.lq.common.utils.MapCache;
import cn.lq.common.utils.TaleUtils;
import cn.lq.service.meta.MetaService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author winterchen
 * @date 2018/4/30
 */
public abstract class BaseController {

    protected MapCache cache = MapCache.single();
    @Resource
    private MetaService metaService;

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }


    /**
     * 获取blog页面需要的公共数据
     */
    public BaseController blogBaseData(HttpServletRequest request, ContentEsInnerQuery contentEsInnerQuery) {
//        List<MetaExtendPO> categories = metaService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
//        List<MetaExtendPO> tags = metaService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        List<MetaExtendPO> links = metaService.getMetaList(Types.LINK.getType(), null, WebConst.MAX_POSTS);
//        request.setAttribute("categories", categories);//分类
//        request.setAttribute("tags", tags);//标签
        request.setAttribute("links", links);
//        PageInfo<ContentDomain> recentlyArticles = contentService.getRecentlyArticle(1, 10);
//        ContentEsInnerQuery query = new ContentEsInnerQuery();
//        query.setType(contentEsInnerQuery.getType());
//        request.setAttribute("recentlyArticles", recentlyArticles);
//        List<ArchiveDto> archives = siteService.getArchivesSimple(query);
//        request.setAttribute("archives", archives);//归档数据

        return this;
    }

    /**
     * 获取请求绑定的登录对象
     */
    public UserPO user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Long getUid(HttpServletRequest request) {
        return this.user(request).getId();
    }

    /**
     * 数组转字符串
     */
    public String join(String[] arr) {
        StringBuilder ret = new StringBuilder();
        for (String item : arr) {
            ret.append(',').append(item);
        }

        return ret.length() > 0 ? ret.substring(1) : ret.toString();
    }
}
