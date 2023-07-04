package cn.lq.service.meta;

import cn.lq.common.domain.po.MetaExtendPO;
import cn.lq.common.domain.po.MetaPO;
import cn.lq.common.domain.query.inner.MetaInnerQuery;

import java.util.List;

/**
 * 项目服务层
 * Created by winterchen on 2018/4/29.
 */
public interface MetaService {
    /**
     * 添加项目
     *
     * @param meta
     * @return
     */
    void addMeta(MetaPO meta);

    /**
     * 添加
     */
    void saveMeta(String type, String name, Long metaId);


    /**
     * 批量添加
     *
     * @param cid
     * @param names
     * @param type
     */
    void addMetas(Long cid, String names, String type);


    /**
     * 添加或者更新
     *
     * @param cid
     * @param name
     * @param type
     */
    void saveOrUpdate(Long cid, String name, String type);

    /**
     * 删除项目
     *
     * @param mid
     * @return
     */
    void deleteMetaById(Long mid);

    /**
     * 更新项目
     *
     * @param meta
     * @return
     */
    void updateMeta(MetaPO meta);

    /**
     * 获取所有的项目
     *
     * @param metaInnerQuery 查询条件
     * @return
     */
    List<MetaPO> getMetas(MetaInnerQuery metaInnerQuery);

    /**
     * 根据类型查询项目列表，带项目下面的文章数
     */
    List<MetaExtendPO> getMetaList(String type, String orderby, int limit);
}
