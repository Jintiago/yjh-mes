package cn.sourceplan.finance.mapper;

import java.util.List;
import cn.sourceplan.finance.domain.SaleOutAccountBill;
import cn.sourceplan.finance.domain.SaleOutAccountEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 销售出库对账单Mapper接口
 *
 * @author jinzhong
 * @date 2023-08-01
 */
@Repository
public interface SaleOutAccountBillMapper extends BaseMapper<SaleOutAccountBill>
{
    /**
     * 查询销售出库对账单
     *
     * @param id 销售出库对账单主键
     * @return 销售出库对账单
     */
    public SaleOutAccountBill selectSaleOutAccountBillById(Long id);

    /**
     * 查询销售出库对账单列表
     *
     * @param saleOutAccountBill 销售出库对账单
     * @return 销售出库对账单集合
     */
    public List<SaleOutAccountEntry> selectSaleOutAccountBillList(SaleOutAccountBill saleOutAccountBill);








}
