package cn.sourceplan.warehouse.service;

import java.util.List;
import cn.sourceplan.warehouse.domain.OtherOut;

/**
 * 其他出库单主表Service接口
 * 
 * @author jinzhong
 * @date 2023-09-28
 */
public interface IOtherOutService 
{
    /**
     * 查询其他出库单主表
     * 
     * @param id 其他出库单主表主键
     * @return 其他出库单主表
     */
    public OtherOut selectOtherOutById(Long id);

    /**
     * 查询其他出库单主表列表
     * 
     * @param otherOut 其他出库单主表
     * @return 其他出库单主表集合
     */
    public List<OtherOut> selectOtherOutList(OtherOut otherOut);

    /**
     * 新增其他出库单主表
     * 
     * @param otherOut 其他出库单主表
     * @return 结果
     */
    public int insertOtherOut(OtherOut otherOut);

    /**
     * 修改其他出库单主表
     * 
     * @param otherOut 其他出库单主表
     * @return 结果
     */
    public int updateOtherOut(OtherOut otherOut);

    /**
     * 批量删除其他出库单主表
     * 
     * @param ids 需要删除的其他出库单主表主键集合
     * @return 结果
     */
    public int deleteOtherOutByIds(Long[] ids);

    /**
     * 删除其他出库单主表信息
     * 
     * @param id 其他出库单主表主键
     * @return 结果
     */
    public int deleteOtherOutById(Long id);
}
