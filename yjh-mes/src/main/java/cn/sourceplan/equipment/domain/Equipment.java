package cn.sourceplan.equipment.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 设备信息对象 dm_equipment
 *
 * @author jinzhong
 * @date 2023-04-06
 */
@Data
@TableName("dm_equipment")
public class Equipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    @TableField(value = "number",condition = SqlCondition.LIKE)
    private String number;

    /** 名称 */
    @Excel(name = "名称")
    @TableField(value = "name",condition = SqlCondition.LIKE)
    private String name;

    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;

    /** 型号 */
    @Excel(name = "型号")
    private String specification;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String type;

    /** 工位ID */
    private Long stationId;

    /** 工位 */
    @Excel(name = "工位")
    private String stationName;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String equipmentStatus;

    /** 点检计划ID */
    private Long checkPlanId;

    /** 异常预警时间 */
    private Long abnormalWarn;

    /** 启用状态 */
    @Excel(name = "启用状态")
    private String status;

    /** 物联序列号 */
    @Excel(name = "物联序列号")
    private String iotSn;
}
