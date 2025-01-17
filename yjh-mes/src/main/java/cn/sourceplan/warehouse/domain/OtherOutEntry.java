package cn.sourceplan.warehouse.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 其他出库单子表对象 wm_other_out_entry
 *
 * @author jinzhong
 * @date 2023-09-28
 */
@TableName("wm_other_out_entry")
@Data
public class OtherOutEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 序号 */
    @Excel(name = "序号")
    private Long sort;

    /** 主表ID */
    @Excel(name = "主表ID")
    private Long mainId;

    /** 物料ID */
    @Excel(name = "物料ID")
    private Long materialId;

    /** 物料编号 */
    @Excel(name = "物料编号")
    private String materialNumber;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 单位ID */
    @Excel(name = "单位ID")
    private Long materialUnitId;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String materialUnitName;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal quantity;

    /** 批次编号 */
    @Excel(name = "批次编号")
    private String batchNumber;

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

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 扩展字段 */
    @Excel(name = "扩展字段")
    private String extendField;

    @TableField(exist = false)
    private OtherOut otherOut ;

}
