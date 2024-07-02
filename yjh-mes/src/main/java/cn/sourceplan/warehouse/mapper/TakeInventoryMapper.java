package cn.sourceplan.warehouse.mapper;

import cn.sourceplan.warehouse.domain.TakeInventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 库存盘点Mapper接口
 *
 * @author jinzhong
 * @date 2023-07-20
 */
@Repository
public interface TakeInventoryMapper extends BaseMapper<TakeInventory>
{
    /**
     * 查询库存盘点
     *
     * @param id 库存盘点主键
     * @return 库存盘点
     */
    public TakeInventory selectTakeInventoryById(Long id);


}
