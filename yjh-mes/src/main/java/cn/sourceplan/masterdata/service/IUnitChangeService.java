package cn.sourceplan.masterdata.service;

import java.util.List;
import cn.sourceplan.masterdata.domain.UnitChange;

/**
 * 计量单位转换Service接口
 * 
 * @author jinzhong
 * @date 2022-09-13
 */
public interface IUnitChangeService 
{
    /**
     * 查询计量单位转换
     * 
     * @param id 计量单位转换主键
     * @return 计量单位转换
     */
    public UnitChange selectUnitChangeById(Long id);

    /**
     * 查询计量单位转换列表
     * 
     * @param unitChange 计量单位转换
     * @return 计量单位转换集合
     */
    public List<UnitChange> selectUnitChangeList(UnitChange unitChange);

    /**
     * 新增计量单位转换
     * 
     * @param unitChange 计量单位转换
     * @return 结果
     */
    public int insertUnitChange(UnitChange unitChange);

    /**
     * 修改计量单位转换
     * 
     * @param unitChange 计量单位转换
     * @return 结果
     */
    public int updateUnitChange(UnitChange unitChange);

    /**
     * 批量删除计量单位转换
     * 
     * @param ids 需要删除的计量单位转换主键集合
     * @return 结果
     */
    public int deleteUnitChangeByIds(Long[] ids);

    /**
     * 删除计量单位转换信息
     * 
     * @param id 计量单位转换主键
     * @return 结果
     */
    public int deleteUnitChangeById(Long id);
}
