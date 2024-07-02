package cn.sourceplan.finance.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.finance.mapper.PieceWagePlanMapper;
import cn.sourceplan.finance.domain.PieceWagePlan;
import cn.sourceplan.finance.service.IPieceWagePlanService;

/**
 * 计件工资方案Service业务层处理
 *
 * @author jinzhong
 * @date 2023-03-09
 */
@Service
public class PieceWagePlanServiceImpl implements IPieceWagePlanService
{
    @Autowired
    private PieceWagePlanMapper pieceWagePlanMapper;

    /**
     * 查询计件工资方案
     *
     * @param id 计件工资方案主键
     * @return 计件工资方案
     */
    @Override
    public PieceWagePlan selectPieceWagePlanById(String id)
    {
        return pieceWagePlanMapper.selectById(id);
    }

    /**
     * 查询计件工资方案列表
     *
     * @param pieceWagePlan 计件工资方案
     * @return 计件工资方案
     */
    @Override
    public List<PieceWagePlan> selectPieceWagePlanList(PieceWagePlan pieceWagePlan)
    {
        QueryWrapper<PieceWagePlan> qw = new QueryWrapper<>(pieceWagePlan);
        return pieceWagePlanMapper.selectList(qw);
    }

    /**
     * 新增计件工资方案
     *
     * @param pieceWagePlan 计件工资方案
     * @return 结果
     */
    @Override
    public int insertPieceWagePlan(PieceWagePlan pieceWagePlan)
    {
        return pieceWagePlanMapper.insert(pieceWagePlan);
    }

    /**
     * 修改计件工资方案
     *
     * @param pieceWagePlan 计件工资方案
     * @return 结果
     */
    @Override
    public int updatePieceWagePlan(PieceWagePlan pieceWagePlan)
    {
        return pieceWagePlanMapper.updateById(pieceWagePlan);
    }

    /**
     * 批量删除计件工资方案
     *
     * @param ids 需要删除的计件工资方案主键
     * @return 结果
     */
    @Override
    public int deletePieceWagePlanByIds(String[] ids)
    {
        return pieceWagePlanMapper.deletePieceWagePlanByIds(ids);
    }

    /**
     * 删除计件工资方案信息
     *
     * @param id 计件工资方案主键
     * @return 结果
     */
    @Override
    public int deletePieceWagePlanById(String id)
    {
        return pieceWagePlanMapper.deleteById(id);
    }
}
