package cn.sourceplan.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.warehouse.domain.Warehouse;
import org.springframework.stereotype.Repository;

/**
 * 仓库信息Mapper接口
 *
 * @author jinzhong
 * @date 2022-11-03
 */
@Repository
public interface WarehouseMapper extends BaseMapper<Warehouse>
{
    /**
     * 删除仓库信息
     *
     * @param id 仓库信息主键
     * @return 结果
     */
    int deleteWarehouseById(Long id);

    /**
     * 批量删除仓库信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteWarehouseByIds(Long[] ids);
}
