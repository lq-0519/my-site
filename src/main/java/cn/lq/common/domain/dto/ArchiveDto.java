package cn.lq.common.domain.dto;

import cn.lq.common.domain.po.es.ContentEsPO;

import java.util.List;

/**
 * 文章归档类
 *
 * @author winterchen
 * @date 2018/4/30
 */
public class ArchiveDto {

    private String date;
    private String count;
    private List<ContentEsPO> articles;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ContentEsPO> getArticles() {
        return articles;
    }

    public void setArticles(List<ContentEsPO> articles) {
        this.articles = articles;
    }
}
