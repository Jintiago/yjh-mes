package cn.sourceplan.warehouse.mapper;

import java.util.List;

import cn.sourceplan.warehouse.domain.Warehouse;
import cn.sourceplan.warehouse.domain.WarehouseStation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 仓库工位绑定Mapper接口
 *
 * @author jinzhong
 * @date 2023-11-04
 */
@Repository
public interface WarehouseStationMapper extends BaseMapper<WarehouseStation>
{

}
