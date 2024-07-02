package cn.sourceplan.masterdata.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.sourceplan.equipment.domain.Equipment;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 工位对象 md_station
 *
 * @author jinzhong
 * @date 2022-09-27
 */
@Data
@TableName(value = "md_station")
public class Station extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 工位编号 */
    @Excel(name = "工位编号")
    private String number;

    /** 工位名称 */
    @Excel(name = "工位名称")
    private String name;

    /** 所属车间ID */
    @Excel(name = "所属车间ID")
    private Long workshopId;

    /** 所属车间名称 */
    @Excel(name = "所属车间名称")
    private String workshopName;

    /** 工序id */
    @Excel(name = "工序id")
    private String processIds;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String machineIds;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    @TableField(exist = false)
    private List<Equipment> EquipmentList;

}
