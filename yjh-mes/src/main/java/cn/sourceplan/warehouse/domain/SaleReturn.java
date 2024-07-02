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
 * 销售退货单对象 wm_sale_return
 *
 * @author jinzhong
 * @date 2023-08-04
 */
@TableName("wm_sale_return")
@Data
public class SaleReturn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 客户ID */
    private Long customerId;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库编号 */
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入库日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date intoDate;

    /** 状态 */
    private String status;

    /** 仓管员 */
    private String keeper;

    /** 审核人 */
    private String checker;

    /** 审核日期 */
    private Date checkDate;

    /** SN码数组 */
    private String snArray;

    /** 销售退货单子表信息 */
    @TableField(exist = false)
    private List<SaleReturnEntry> saleReturnEntryList;


}
