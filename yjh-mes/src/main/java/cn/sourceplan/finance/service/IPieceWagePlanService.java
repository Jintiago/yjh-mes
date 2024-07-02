package cn.sourceplan.finance.service;

import java.util.List;
import cn.sourceplan.finance.domain.PieceWagePlan;

/**
 * 计件工资方案Service接口
 * 
 * @author jinzhong
 * @date 2023-03-09
 */
public interface IPieceWagePlanService 
{
    /**
     * 查询计件工资方案
     * 
     * @param id 计件工资方案主键
     * @return 计件工资方案
     */
    public PieceWagePlan selectPieceWagePlanById(String id);

    /**
     * 查询计件工资方案列表
     * 
     * @param pieceWagePlan 计件工资方案
     * @return 计件工资方案集合
     */
    public List<PieceWagePlan> selectPieceWagePlanList(PieceWagePlan pieceWagePlan);

    /**
     * 新增计件工资方案
     * 
     * @param pieceWagePlan 计件工资方案
     * @return 结果
     */
    public int insertPieceWagePlan(PieceWagePlan pieceWagePlan);

    /**
     * 修改计件工资方案
     * 
     * @param pieceWagePlan 计件工资方案
     * @return 结果
     */
    public int updatePieceWagePlan(PieceWagePlan pieceWagePlan);

    /**
     * 批量删除计件工资方案
     * 
     * @param ids 需要删除的计件工资方案主键集合
     * @return 结果
     */
    public int deletePieceWagePlanByIds(String[] ids);

    /**
     * 删除计件工资方案信息
     * 
     * @param id 计件工资方案主键
     * @return 结果
     */
    public int deletePieceWagePlanById(String id);
}
