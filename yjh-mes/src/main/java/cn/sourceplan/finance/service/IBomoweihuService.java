package cn.sourceplan.finance.service;

import java.util.List;
import cn.sourceplan.finance.domain.Bomoweihu;

/**
 * 天润薄膜核算维护Service接口
 *
 * @author jinzhong
 * @date 2023-07-04
 */
public interface IBomoweihuService
{
    /**
     * 查询天润薄膜核算维护
     *
     * @param priceType 天润薄膜核算维护主键
     * @return 天润薄膜核算维护
     */
    public Bomoweihu selectBomoweihuById(Long id);

    /**
     * 查询天润薄膜核算维护列表
     *
     * @param bomoweihu 天润薄膜核算维护
     * @return 天润薄膜核算维护集合
     */
    public List<Bomoweihu> selectBomoweihuList(Bomoweihu bomoweihu);

    /**
     * 新增天润薄膜核算维护
     *
     * @param bomoweihu 天润薄膜核算维护
     * @return 结果
     */
    public int insertBomoweihu(Bomoweihu bomoweihu);

    /**
     * 修改天润薄膜核算维护
     *
     * @param bomoweihu 天润薄膜核算维护
     * @return 结果
     */
    public int updateBomoweihu(Bomoweihu bomoweihu);

    /**
     * 批量删除天润薄膜核算维护
     *
     * @param priceTypes 需要删除的天润薄膜核算维护主键集合
     * @return 结果
     */
    public int deleteBomoweihuByIds(Long[] ids);

    /**
     * 删除天润薄膜核算维护信息
     *
     * @param priceType 天润薄膜核算维护主键
     * @return 结果
     */
    public int deleteBomoweihuById(Long id);
}
