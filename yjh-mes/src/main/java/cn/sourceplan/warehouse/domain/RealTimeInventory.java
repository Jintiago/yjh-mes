package cn.sourceplan.warehouse.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 即时库存对象 real_time_inventory
 *
 * @author jinzhong
 * @date 2022-11-29
 */
@Data
public class RealTimeInventory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    @Excel(name = "仓库")
    private String warehouseName;

    /** 物料ID */
    private Long materialId;

    /** 物料编号 */
    @Excel(name = "物料编号")
    private String materialNumber;

    /** 物料名称 */
    @Excel(name = "物料")
    private String materialName;

    /** 单位名称 */
    @Excel(name = "单位")
    private String materialUnitName;

    /** 单位名称 */
    @Excel(name = "规格型号")
    private String specification;

    @Excel(name = "数量")
    private BigDecimal quantity;

}
