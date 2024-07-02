package cn.sourceplan.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.masterdata.domain.Workshop;
import org.springframework.stereotype.Repository;

/**
 * 车间Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-21
 */
@Repository
public interface WorkshopMapper extends BaseMapper<Workshop>
{

    /**
     * 删除车间
     *
     * @param id 车间主键
     * @return 结果
     */
    public int deleteWorkshopById(Long id);

    /**
     * 批量删除车间
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkshopByIds(Long[] ids);
}
