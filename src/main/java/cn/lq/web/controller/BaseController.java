package cn.lq.web.controller;

import cn.lq.common.cond.ContentCond;
import cn.lq.common.constant.Types;
import cn.lq.common.constant.WebConst;
import cn.lq.common.dto.MetaDto;
import cn.lq.common.model.UserDomain;
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
     *
     * @param request
     * @return
     */
    public BaseController blogBaseData(HttpServletRequest request, ContentCond contentCond) {
//        List<MetaDto> categories = metaService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
//        List<MetaDto> tags = metaService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        List<MetaDto> links = metaService.getMetaList(Types.LINK.getType(), null, WebConst.MAX_POSTS);
//        request.setAttribute("categories", categories);//分类
//        request.setAttribute("tags", tags);//标签
        request.setAttribute("links", links);
//        PageInfo<ContentDomain> recentlyArticles = contentService.getRecentlyArticle(1, 10);
//        ContentCond cond = new ContentCond();
//        cond.setType(contentCond.getType());
//        request.setAttribute("recentlyArticles", recentlyArticles);
//        List<ArchiveDto> archives = siteService.getArchivesSimple(cond);
//        request.setAttribute("archives", archives);//归档数据

        return this;
    }

    /**
     * 获取请求绑定的登录对象
     *
     * @param request
     * @return
     */
    public UserDomain user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request) {
        return this.user(request).getUid();
    }

    /**
     * 数组转字符串
     *
     * @param arr
     * @return
     */
    public String join(String[] arr) {
        StringBuilder ret = new StringBuilder();
        String[] var3 = arr;
        int var4 = arr.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String item = var3[var5];
            ret.append(',').append(item);
        }

        return ret.length() > 0 ? ret.substring(1) : ret.toString();
    }
}
