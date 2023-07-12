package cn.lq.common.domain.query;

import java.io.Serializable;

/**
 * @author liqian477
 * @date 2023/07/12 11:14
 */
public class ContentQuery implements Serializable {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ContentQuery{" +
                "content='" + content + '\'' +
                '}';
    }
}
