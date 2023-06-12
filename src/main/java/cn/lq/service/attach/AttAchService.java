package cn.lq.service.attach;

import cn.lq.common.dto.AttAchDto;
import cn.lq.common.model.AttAchDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 附件服务层
 * Created by winterchen on 2018/4/29.
 */
public interface AttAchService {

    /**
     * 添加单个附件信息
     */
    void addAttAch(AttAchDomain attAchDomain);

    /**
     * 批量添加附件信息
     */
    void batchAddAttAch(List<AttAchDomain> list);

    /**
     * 根据主键编号删除附件信息
     */
    void deleteAttAch(Integer id);

    /**
     * 更新附件信息
     */
    void updateAttAch(AttAchDomain attAchDomain);

    /**
     * 根据主键获取附件信息
     */
    AttAchDto getAttAchById(Integer id);

    /**
     * 获取所有的附件信息
     */
    PageInfo<AttAchDto> getAtts(int pageNum, int pageSize);
}
