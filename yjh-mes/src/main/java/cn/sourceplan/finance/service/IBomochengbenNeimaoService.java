package cn.sourceplan.finance.service;

import cn.sourceplan.finance.domain.Bomochengben;
import cn.sourceplan.finance.domain.BomochengbenNeimao;

import java.util.List;

/**
 * 薄膜核算Service接口
 *
 * @author jinzhong
 * @date 2023-07-04
 */
public interface IBomochengbenNeimaoService
{
    /**
     * 查询薄膜核算
     *
     * @param id 薄膜核算主键
     * @return 薄膜核算
     */
    public BomochengbenNeimao selectBomochengbenNeimaoById(Long id);

    /**
     * 查询薄膜核算列表
     *
     * @param bomochengben 薄膜核算
     * @return 薄膜核算集合
     */
    public List<BomochengbenNeimao> selectBomochengbenNeimaoList(BomochengbenNeimao bomochengben);

    /**
     * 新增薄膜核算
     *
     * @param bomochengben 薄膜核算
     * @return 结果
     */
    public int insertBomochengbenNeimao(BomochengbenNeimao bomochengben);

    /**
     * 修改薄膜核算
     *
     * @param bomochengben 薄膜核算
     * @return 结果
     */
    public int updateBomochengbenNeimao(BomochengbenNeimao bomochengben);

    /**
     * 批量删除薄膜核算
     *
     * @param ids 需要删除的薄膜核算主键集合
     * @return 结果
     */
    public int deleteBomochengbenNeimaoByIds(Long[] ids);

    /**
     * 删除薄膜核算信息
     *
     * @param id 薄膜核算主键
     * @return 结果
     */
    public int deleteBomochengbenNeimaoById(Long id);
}
