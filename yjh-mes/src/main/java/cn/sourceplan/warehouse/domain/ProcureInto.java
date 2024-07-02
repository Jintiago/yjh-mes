package cn.sourceplan.warehouse.domain;

import java.math.BigDecimal;
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
 * 采购入库单对象 wm_procure_into
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@TableName("wm_procure_into")
@Data
public class ProcureInto extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 入库单编号 */
    @Excel(name = "入库单编号")
    private String number;

    /** 供应商ID */
    @Excel(name = "供应商ID")
    private Long supplierId;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 采购方式 */
    @Excel(name = "采购方式")
    private String method;

    /** 仓库ID */
    @Excel(name = "仓库ID")
    private Long warehouseId;

    /** 仓库编号 */
    @Excel(name = "仓库编号")
    private String warehouseNumber;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 库区ID */
    @Excel(name = "库区ID")
    private Long regionId;

    /** 库区编号 */
    @Excel(name = "库区编号")
    private String regionNumber;

    /** 库区名称 */
    @Excel(name = "库区名称")
    private String regionName;

    /** 库位ID */
    @Excel(name = "库位ID")
    private Long areaId;

    /** 库位编号 */
    @Excel(name = "库位编号")
    private String areaNumber;

    /** 库位名称 */
    @Excel(name = "库位名称")
    private String areaName;

    /** 入库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入库日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date intoDate;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 仓管员 */
    @Excel(name = "仓管员")
    private String keeper;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checker;


    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal price;

    /** 审核日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkDate;

    /** SN码数组 */
    @Excel(name = "SN码数组")
    private String snArray;

    /** 采购入库单子表信息 */
    @TableField(exist = false)
    private List<ProcureIntoEntry> procureIntoEntryList;


}
