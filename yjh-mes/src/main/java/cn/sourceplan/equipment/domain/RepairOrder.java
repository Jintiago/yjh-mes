package cn.sourceplan.equipment.domain;

import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 设备维修单对象 dm_repair_order
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@Data
@TableName("dm_repair_order")
public class RepairOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 设备ID */
    private Long equipmentId;

    /** 设备编码 */
    private String equipmentNumber;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String equipmentName;

    /** 品牌 */
    private String equipmentBrand;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String equipmentSpecification;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String equipmentType;

    /** 报修时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报修时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reportRepairTime;

    /** 维修完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "维修完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String repairResult;

    /** 维修人ID */
    private Long repairUserId;

    /** 维修人 */
    @Excel(name = "维修人")
    private String repairUserName;

    /** 验收人ID */
    private Long confirmUserId;

    /** 验收人 */
    private String confirmUserName;

    /** 验收日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "验收日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    /** 单据状态 */
    @Excel(name = "单据状态")
    @TableField(condition = "%s in (${%s})")
    private String status;

    /** 设备维修单明细信息 */
    @TableField(exist = false)
    private List<RepairOrderEntry> repairOrderEntryList;




}
