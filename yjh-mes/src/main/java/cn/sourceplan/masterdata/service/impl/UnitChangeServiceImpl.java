package cn.sourceplan.masterdata.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.masterdata.mapper.UnitChangeMapper;
import cn.sourceplan.masterdata.domain.UnitChange;
import cn.sourceplan.masterdata.service.IUnitChangeService;

/**
 * 计量单位转换Service业务层处理
 * 
 * @author jinzhong
 * @date 2022-09-13
 */
@Service
public class UnitChangeServiceImpl implements IUnitChangeService 
{
    @Autowired
    private UnitChangeMapper unitChangeMapper;

    /**
     * 查询计量单位转换
     * 
     * @param id 计量单位转换主键
     * @return 计量单位转换
     */
    @Override
    public UnitChange selectUnitChangeById(Long id)
    {
        return unitChangeMapper.selectUnitChangeById(id);
    }

    /**
     * 查询计量单位转换列表
     * 
     * @param unitChange 计量单位转换
     * @return 计量单位转换
     */
    @Override
    public List<UnitChange> selectUnitChangeList(UnitChange unitChange)
    {
        return unitChangeMapper.selectUnitChangeList(unitChange);
    }

    /**
     * 新增计量单位转换
     * 
     * @param unitChange 计量单位转换
     * @return 结果
     */
    @Override
    public int insertUnitChange(UnitChange unitChange)
    {
        return unitChangeMapper.insertUnitChange(unitChange);
    }

    /**
     * 修改计量单位转换
     * 
     * @param unitChange 计量单位转换
     * @return 结果
     */
    @Override
    public int updateUnitChange(UnitChange unitChange)
    {
        return unitChangeMapper.updateUnitChange(unitChange);
    }

    /**
     * 批量删除计量单位转换
     * 
     * @param ids 需要删除的计量单位转换主键
     * @return 结果
     */
    @Override
    public int deleteUnitChangeByIds(Long[] ids)
    {
        return unitChangeMapper.deleteUnitChangeByIds(ids);
    }

    /**
     * 删除计量单位转换信息
     * 
     * @param id 计量单位转换主键
     * @return 结果
     */
    @Override
    public int deleteUnitChangeById(Long id)
    {
        return unitChangeMapper.deleteUnitChangeById(id);
    }
}
