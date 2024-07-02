package cn.sourceplan.warehouse.service;

import java.util.List;

import cn.sourceplan.warehouse.domain.ProcureInto;
import cn.sourceplan.warehouse.domain.ProcureIntoEntry;

/**
 * 采购入库单Service接口
 *
 * @author jinzhong
 * @date 2023-06-26
 */
public interface IProcureIntoService
{
    /**
     * 查询采购入库单
     *
     * @param id 采购入库单主键
     * @return 采购入库单
     */
    public ProcureInto selectProcureIntoById(Long id);

    /**
     * 查询采购入库单列表
     *
     * @param procureInto 采购入库单
     * @return 采购入库单集合
     */
    public List<ProcureIntoEntry> selectProcureIntoList(ProcureInto procureInto);

    /**
     * 新增采购入库单
     *
     * @param procureInto 采购入库单
     * @return 结果
     */
    public int insertProcureInto(ProcureInto procureInto);

    /**
     * 修改采购入库单
     *
     * @param procureInto 采购入库单
     * @return 结果
     */
    public int updateProcureInto(ProcureInto procureInto);

    /**
     * 批量删除采购入库单
     *
     * @param ids 需要删除的采购入库单主键集合
     * @return 结果
     */
    public int deleteProcureIntoByIds(Long[] ids);

    /**
     * 删除采购入库单信息
     *
     * @param id 采购入库单主键
     * @return 结果
     */
    public int deleteProcureIntoById(Long id);
}
