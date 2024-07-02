package cn.sourceplan.warehouse.mapper;

import java.util.List;

import cn.sourceplan.warehouse.domain.SaleOut;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 销售出库单主表Mapper接口
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@Repository
public interface SaleOutMapper extends BaseMapper<SaleOut>
{
    /**
     * 查询销售出库单主表
     *
     * @param id 销售出库单主表主键
     * @return 销售出库单主表
     */
    public SaleOut selectSaleOutById(Long id);

    /**
     * 查询销售出库单主表列表
     *
     * @param saleOut 销售出库单主表
     * @return 销售出库单主表集合
     */
    public List<SaleOut> selectSaleOutList(SaleOut saleOut);

    /**
     * 删除销售出库单主表
     *
     * @param id 销售出库单主表主键
     * @return 结果
     */
    public int deleteSaleOutById(Long id);

    /**
     * 批量删除销售出库单主表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSaleOutByIds(Long[] ids);

    /**
     * 批量删除销售出库子表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSaleOutEntryByMainIds(Long[] ids);

    /**
     * 通过销售出库单主表主键删除销售出库子表信息
     *
     * @param id 销售出库单主表ID
     * @return 结果
     */
    public int deleteSaleOutEntryByMainId(Long id);
}
