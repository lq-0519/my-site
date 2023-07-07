package cn.lq.common.domain.po;

import java.io.Serializable;

/**
 * @author liqian477
 * @date 2023/07/05 21:27
 */
public class ArchivePO implements Serializable {
    private String date;
    private String count;

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

    @Override
    public String toString() {
        return "ArchivePO{" +
                "date='" + date + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
