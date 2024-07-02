package cn.sourceplan.finance.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.finance.mapper.BomoweihuMapper;
import cn.sourceplan.finance.domain.Bomoweihu;
import cn.sourceplan.finance.service.IBomoweihuService;

/**
 * 天润薄膜核算维护Service业务层处理
 *
 * @author jinzhong
 * @date 2023-07-04
 */
@Service
public class BomoweihuServiceImpl implements IBomoweihuService
{
    @Autowired
    private BomoweihuMapper bomoweihuMapper;

    /**
     * 查询天润薄膜核算维护
     *
     * @param id 天润薄膜核算维护主键
     * @return 天润薄膜核算维护
     */
    @Override
    public Bomoweihu selectBomoweihuById(Long id)
    {

        return bomoweihuMapper.selectById(id);
    }

    /**
     * 查询天润薄膜核算维护列表
     *
     * @param bomoweihu 天润薄膜核算维护
     * @return 天润薄膜核算维护
     */
    @Override
    public List<Bomoweihu> selectBomoweihuList(Bomoweihu bomoweihu)
    {
        QueryWrapper<Bomoweihu>  qw =  new QueryWrapper<>(bomoweihu);
        return bomoweihuMapper.selectList(qw);
    }

    /**
     * 新增天润薄膜核算维护
     *
     * @param bomoweihu 天润薄膜核算维护
     * @return 结果
     */
    @Override
    public int insertBomoweihu(Bomoweihu bomoweihu)
    {

        return bomoweihuMapper.insert(bomoweihu);
    }

    /**
     * 修改天润薄膜核算维护
     *
     * @param bomoweihu 天润薄膜核算维护
     * @return 结果
     */
    @Override
    public int updateBomoweihu(Bomoweihu bomoweihu)
    {

        return bomoweihuMapper.updateById(bomoweihu);
    }

    /**
     * 批量删除天润薄膜核算维护
     *
     * @param ids 需要删除的天润薄膜核算维护主键
     * @return 结果
     */
    @Override
    public int deleteBomoweihuByIds(Long[] ids)
    {
        return bomoweihuMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除天润薄膜核算维护信息
     *
     * @param id 天润薄膜核算维护主键
     * @return 结果
     */
    @Override
    public int deleteBomoweihuById(Long id)
    {
        return bomoweihuMapper.deleteById(id);
    }
}
