package cn.sourceplan.equipment.service;

import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import cn.sourceplan.equipment.domain.Equipment;

import javax.servlet.http.HttpServletRequest;

/**
 * 设备信息Service接口
 *
 * @author jinzhong
 * @date 2023-04-06
 */
public interface IEquipmentService
{
    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    public Equipment selectEquipmentById(Long id);

    /**
     * 查询设备信息列表
     *
     * @param equipment 设备信息
     * @return 设备信息集合
     */
    public List<Equipment> selectEquipmentList(Equipment equipment);

    /**
     * 新增设备信息
     *
     * @param equipment 设备信息
     * @return 结果
     */
    public int insertEquipment(Equipment equipment);

    /**
     * 修改设备信息
     *
     * @param equipment 设备信息
     * @return 结果
     */
    public int updateEquipment(Equipment equipment);

    /**
     * 批量删除设备信息
     *
     * @param ids 需要删除的设备信息主键集合
     * @return 结果
     */
    public int deleteEquipmentByIds(Long[] ids);

    /**
     * 删除设备信息信息
     *
     * @param id 设备信息主键
     * @return 结果
     */
    public int deleteEquipmentById(Long id);

    /**
     * 查询设备OEE稼动率
     *
     * @param httpServletRequest 查询条件
     * @return 结果
     */
    JSONObject oeeUtilizationList(HttpServletRequest httpServletRequest);
}
