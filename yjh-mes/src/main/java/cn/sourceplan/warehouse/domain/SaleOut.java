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
 * 销售出库单主表对象 wm_sale_out
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@TableName("wm_sale_out")
@Data
public class SaleOut extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 物流信息 */
    @Excel(name = "物流信息")
    private String logisticsInfo;

    /** 开票标记 */
    @Excel(name = "开票标记")
    private String invoiceMark;

    /** 销售方式 */
    @Excel(name = "销售方式")
    private String method;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long customerId;

    /** 发货地点 */
    @Excel(name = "发货地点")
    private String shipLocation;

    /** 出库单编号 */
    @Excel(name = "出库单编号")
    private String number;

    /** 车间ID */
    @Excel(name = "车间ID")
    private Long workshopId;

    /** 车间名称 */
    @Excel(name = "车间名称")
    private String workshopName;



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

    /** 仓管员ID */
    @Excel(name = "仓管员ID")
    private Long keeperId;

    /** 仓管名称 */
    @Excel(name = "仓管名称")
    private String keeperName;

    /** 领料时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date outTime;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checker;

    /** 审核日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkDate;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** SN码数组 */
    @Excel(name = "SN码数组")
    private String snArray;

    /** 销售出库子表信息 */
    @TableField(exist = false)
    private List<SaleOutEntry> saleOutEntryList;

    /** 总金额 */
    @Excel(name = "总金额")
    private BigDecimal totalAmount;

    @TableField(exist = false)
    private String[] outTimeQuery;
}
