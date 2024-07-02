package cn.sourceplan.buy.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 采购订单明细对象 buy_order_entry
 *
 * @author jinzhong
 * @date 2023-08-30
 */
@TableName("buy_order_entry")
@Data
public class BuyOrderEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;


    /** 紧急度 */
    @Excel(name = "紧急度")
    private Integer urgency;

    /** 主表id */
    @Excel(name = "主表id")
    private Long mainId;

    /** 物料id */
    @Excel(name = "物料id")
    private Long materialId;

    /** 物料编号 */
    @Excel(name = "物料编号")
    private String materialNumber;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String materialSpecification;

    /** 单位id */
    @Excel(name = "单位id")
    private Long unitId;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String unitName;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal quantity;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 含税单价 */
    @Excel(name = "含税单价")
    private BigDecimal taxPrice;

    /** 是否赠品 */
    @Excel(name = "是否赠品")
    private String isGift;

    /** 税率 */
    @Excel(name = "税率")
    private Long taxRate;

    /** 税额 */
    @Excel(name = "税额")
    private BigDecimal totalTax;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal totalPrice;

    /** 价税合计 */
    @Excel(name = "价税合计")
    private BigDecimal totalTaxPrice;

    /** 要货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "要货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date needDate;

    /** 状态 */
    private String status;

    /** 扩展字段 */
    @Excel(name = "扩展字段")
    private String extendField;


}
