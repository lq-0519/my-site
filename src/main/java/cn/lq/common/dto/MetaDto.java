package cn.lq.common.dto;

import cn.lq.common.model.MetaDomain;

/**
 * 标签、分类列表
 *
 * @author winterchen
 * @date 2018/4/30
 */
public class MetaDto extends MetaDomain {

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
