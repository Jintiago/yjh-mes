package cn.sourceplan.finance.service;

import java.util.List;
import cn.sourceplan.finance.domain.Bomochengben;

/**
 * 薄膜核算Service接口
 * 
 * @author jinzhong
 * @date 2023-07-04
 */
public interface IBomochengbenService 
{
    /**
     * 查询薄膜核算
     * 
     * @param id 薄膜核算主键
     * @return 薄膜核算
     */
    public Bomochengben selectBomochengbenById(Long id);

    /**
     * 查询薄膜核算列表
     * 
     * @param bomochengben 薄膜核算
     * @return 薄膜核算集合
     */
    public List<Bomochengben> selectBomochengbenList(Bomochengben bomochengben);

    /**
     * 新增薄膜核算
     * 
     * @param bomochengben 薄膜核算
     * @return 结果
     */
    public int insertBomochengben(Bomochengben bomochengben);

    /**
     * 修改薄膜核算
     * 
     * @param bomochengben 薄膜核算
     * @return 结果
     */
    public int updateBomochengben(Bomochengben bomochengben);

    /**
     * 批量删除薄膜核算
     * 
     * @param ids 需要删除的薄膜核算主键集合
     * @return 结果
     */
    public int deleteBomochengbenByIds(Long[] ids);

    /**
     * 删除薄膜核算信息
     * 
     * @param id 薄膜核算主键
     * @return 结果
     */
    public int deleteBomochengbenById(Long id);
}
