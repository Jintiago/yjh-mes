package cn.sourceplan.warehouse.domain;

import java.math.BigDecimal;

import cn.sourceplan.common.annotation.Excels;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 生产领料单子表对象
 *
 * @author jinzhong
 * @date 2023-02-23
 */
@TableName("wm_production_pick_entry")
@Data
public class ProPickEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 领料入库主表 */
    @TableField(exist = false)
    // 多个字段导出
    @Excels({
            @Excel(name = "单号", targetAttr = "number", type = Excel.Type.EXPORT),
            @Excel(name = "领料日期", targetAttr = "pickTime", type = Excel.Type.EXPORT,dateFormat = "yyyy-MM-dd"),
            @Excel(name = "领料仓库", targetAttr = "warehouseName", type = Excel.Type.EXPORT),
            @Excel(name = "领料人", targetAttr = "pickUserName", type = Excel.Type.EXPORT),
    })
    public ProPick proPick;

    /** 序号 */
    private Long sort;

    /** 主表ID */
    private Long mainId;

    /** 物料ID */
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
    private Long materialUnitId;

    /** 批次编号 */
    private String batchNumber;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal quantity;

    public void setQuantity(BigDecimal quantity) {
        this.quantity = new BigDecimal(quantity.stripTrailingZeros().toPlainString());
    }

    /** 单位名称 */
    @Excel(name = "单位")
    private String materialUnitName;


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




}
