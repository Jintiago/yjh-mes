package cn.sourceplan.warehouse.domain;

import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 完工入库单对象 wm_manufacture_into
 *
 * @author jinzhong
 * @date 2022-11-10
 */
@TableName("wm_manufacture_into")
@Data
public class ManufactureInto extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 入库单编号 */
    @Excel(name = "入库单编号")
    private String number;

    /** 来料检验单ID */
    private Long iqcId;

    /** 来料检验单编号 */
    private String iqcNumber;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库编号 */
    @Excel(name = "仓库编号")
    private String warehouseNumber;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 库区ID */
    private Long regionId;

    /** 库区编号 */
    private String regionNumber;

    /** 库区名称 */
    private String regionName;

    /** 库位ID */
    private Long areaId;

    /** 库位编号 */
    private String areaNumber;

    /** 库位名称 */
    private String areaName;

    /** 入库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入库日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date intoDate;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 交货人ID */
    @Excel(name = "交货人ID")
    private Long delivererId;

    /** 交货人名称 */
    @Excel(name = "交货人名称")
    private String delivererName;

    /** 仓管员 */
    @Excel(name = "仓管员")
    private String keeper;

    /** 工单子表ID */
    private Long workOrderEntryId;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checker;
    /** 审核日期 */
    @Excel(name = "审核日期")
    private Date checkDate;

    /** SN码数组 */
    @Excel(name = "SN码数组")
    private String snArray;

    /** 生产入库单子表信息 */
    @TableField(exist = false)
    private List<ManufactureIntoEntry> manufactureIntoEntryList;


}
