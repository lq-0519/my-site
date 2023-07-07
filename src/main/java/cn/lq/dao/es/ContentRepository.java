package cn.lq.dao.es;

import cn.lq.common.domain.po.es.ContentEsPO;
import cn.lq.common.domain.query.inner.es.ContentEsInnerQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author LQ
 * @create 2021-05-06 20:42
 */
public interface ContentRepository extends ElasticsearchRepository<ContentEsPO, Long> {
    String INDEX_NAME = "content";
    String TYPE = "content";
    Class<ContentEsPO> DOC_CLASS = ContentEsPO.class;
    Class<ContentEsInnerQuery> QUERY_CLASS = ContentEsInnerQuery.class;
    String FIELD_TITLE = "title";
    String FIELD_CONTENT = "content";
}
