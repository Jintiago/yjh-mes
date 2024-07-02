package cn.sourceplan.warehouse.domain;

import java.math.BigDecimal;
import java.util.Date;

import cn.sourceplan.common.annotation.Excels;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 采购入库单子表对象 wm_procure_into_entry
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@TableName("wm_procure_into_entry")
@Data
public class ProcureIntoEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 序号 */
    private Long sort;

    /** 主表ID */
    private Long mainId;

    /** 物料ID */
    private Long materialId;

    // 多个字段导出
    @Excels({
            @Excel(name = "单号", targetAttr = "number", type = Excel.Type.EXPORT),
            @Excel(name = "入库日期", targetAttr = "intoDate", type = Excel.Type.EXPORT,dateFormat = "yyyy-MM-dd"),
            @Excel(name = "仓库", targetAttr = "warehouseName", type = Excel.Type.EXPORT),
            @Excel(name = "供应商", targetAttr = "supplierName", type = Excel.Type.EXPORT),
    })
    @TableField(exist = false)
    private ProcureInto procureInto;

    /** 物料编号 */
    @Excel(name = "物料编号")
    private String materialNumber;

    /** 批次编号 */
//    @Excel(name = "批次编号")
    private String batchNumber;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 单位ID */
    private Long materialUnitId;

    /** 单位名称 */
    @Excel(name = "单位")
    private String materialUnitName;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal quantity;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库编号 */
    private String warehouseNumber;

    /** 仓库名称 */
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

    /** 生产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date manufactureDate;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal totalPrice;

    public void setQuantity(BigDecimal quantity) {
        this.quantity = new BigDecimal(quantity.stripTrailingZeros().toPlainString());
    }

    public void setPrice(BigDecimal price) {
        this.price = new BigDecimal(price.stripTrailingZeros().toPlainString());
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = new BigDecimal(totalPrice.stripTrailingZeros().toPlainString());
    }
}
