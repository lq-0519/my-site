package cn.lq.manager.impl;

import cn.lq.common.domain.enums.DeletedEnum;
import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.es.ContentEsInnerQuery;
import cn.lq.common.exception.ParamInvalidException;
import cn.lq.common.utils.EsIdGenerator;
import cn.lq.common.utils.EsUtils;
import cn.lq.dao.es.ContentRepository;
import cn.lq.dao.es.HighlightResultMapper;
import cn.lq.manager.ContentManager;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author liqian477
 * @date 2023/06/28 10:25
 */
@Service("contentManager")
public class ContentManagerImpl implements ContentManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentManagerImpl.class);
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private ContentRepository contentRepository;

    @Override
    public long insert(ContentEsPO contentEsPO) {
        if (contentEsPO == null) {
            throw new ParamInvalidException("insertEs 参数异常");
        }

        long id = EsIdGenerator.nextId();
        contentEsPO.setId(id);
        contentEsPO.setCreated(new Date());
        contentEsPO.setModified(new Date());
        contentEsPO.setDeleted(DeletedEnum.NOT_DELETE.getStatus());
        contentRepository.save(contentEsPO);
        return id;
    }

    /**
     * 根据编号删除文章
     * 删除es
     */
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new ParamInvalidException("deleteEs 参数异常");
        }

        ContentEsPO contentEsPO = new ContentEsPO();
        contentEsPO.setId(id);
        contentEsPO.setDeleted(DeletedEnum.DELETE.getStatus());
        update(contentEsPO);
    }

    @Override
    public int update(ContentEsPO contentEsPO) {
        if (contentEsPO == null || contentEsPO.getId() == null) {
            throw new ParamInvalidException("updateEs 参数异常");
        }

        IndexRequest indexRequest = EsUtils.getIndexRequest(contentEsPO, ContentRepository.DOC_CLASS);
        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withIndexName(ContentRepository.INDEX_NAME)
                .withType(ContentRepository.TYPE)
                .withId(String.valueOf(contentEsPO.getId()))
                .withClass(ContentRepository.DOC_CLASS)
                .withIndexRequest(indexRequest)
                .build();
        UpdateResponse updateResponse = elasticsearchTemplate.update(updateQuery);
        LOGGER.warn("update updateResponse:{}", JSON.toJSON(updateResponse));
        return updateResponse.status().getStatus();
    }

    @Override
    public ContentEsPO queryForObject(Long id) {
        if (id == null) {
            return null;
        }

        return contentRepository.findById(id)
                .orElse(null);
    }

    @Override
    public long queryForCount(ContentEsInnerQuery contentEsInnerQuery) {
        BoolQueryBuilder boolQueryBuilder = EsUtils.getBoolQueryBuilder(contentEsInnerQuery, ContentRepository.QUERY_CLASS);
        //内容查询特殊处理, 需要映射到两个字段上进行模糊查询
        if (StringUtils.isNotBlank(contentEsInnerQuery.getContent())) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(contentEsInnerQuery.getContent(), ContentRepository.FIELD_CONTENT, ContentRepository.FIELD_TITLE));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(ContentRepository.INDEX_NAME)
                .withTypes(ContentRepository.TYPE)
                .withQuery(boolQueryBuilder)
                .build();
        return elasticsearchTemplate.count(searchQuery, ContentRepository.DOC_CLASS);
    }

    @Override
    public Page<ContentEsPO> queryForPage(ContentEsInnerQuery contentEsInnerQuery, int page, int pageSize) {
        BoolQueryBuilder boolQueryBuilder = EsUtils.getBoolQueryBuilder(contentEsInnerQuery, ContentRepository.QUERY_CLASS);
        //内容查询特殊处理, 需要映射到两个字段上进行模糊查询
        if (StringUtils.isNotBlank(contentEsInnerQuery.getContent())) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(contentEsInnerQuery.getContent(), ContentRepository.FIELD_CONTENT, ContentRepository.FIELD_TITLE));
        }
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(ContentRepository.INDEX_NAME)
                .withTypes(ContentRepository.TYPE)
                .withQuery(boolQueryBuilder)
                .withHighlightBuilder(new HighlightBuilder()
                        .field(ContentRepository.FIELD_TITLE)
                        .field(ContentRepository.FIELD_CONTENT)
                        .preTags("<mark>")
                        .postTags("</mark>")
                        //注意加上高亮片数即可
                        .fragmentSize(800000)
                        .numOfFragments(0)
                        .requireFieldMatch(false)
                )
                .withPageable(PageRequest.of(page - 1, pageSize))
                .build();
        return elasticsearchTemplate.queryForPage(searchQuery, ContentEsPO.class, new HighlightResultMapper());
    }
}
