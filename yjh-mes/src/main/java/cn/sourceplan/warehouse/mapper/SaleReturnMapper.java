package cn.sourceplan.warehouse.mapper;

import java.util.List;

import cn.sourceplan.warehouse.domain.SaleReturn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 销售退货单Mapper接口
 *
 * @author jinzhong
 * @date 2023-08-04
 */
@Repository
public interface SaleReturnMapper extends BaseMapper<SaleReturn>
{
    /**
     * 查询销售退货单
     *
     * @param id 销售退货单主键
     * @return 销售退货单
     */
    public SaleReturn selectSaleReturnById(Long id);

    /**
     * 查询销售退货单列表
     *
     * @param saleReturn 销售退货单
     * @return 销售退货单集合
     */
    public List<SaleReturn> selectSaleReturnList(SaleReturn saleReturn);


}
