package cn.lq.common.domain.query.inner;

import java.io.Serializable;

/**
 * @author liqian477
 * @date 2023/07/04 17:37
 */
public class AttachmentInnerQuery extends BaseInnerQuery implements Serializable {
    /**
     * 文件名称
     */
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "AttachmentInnerQuery{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
