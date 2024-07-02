package cn.sourceplan.sale.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * ${subTable.functionName}对象 sal_order_entry
 *
 * @author jinzhong
 * @date 2022-10-08
 */
@TableName("sal_order_entry")
@Data
public class SalOrderEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

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

    /** 物料分类 */
    @Excel(name = "物料分类")
    private String materialClass;

    /** 物料类型 */
    @Excel(name = "物料类型")
    private String materialType;

    /** 单位id */
    @Excel(name = "单位id")
    private Long unitId;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String unitName;

    /** 销售数量 */
    @Excel(name = "销售数量")
    private BigDecimal quantity;

    public void setQuantity(BigDecimal quantity) {
        quantity= quantity.stripTrailingZeros();
        quantity = quantity.scale() < 0 ? quantity.setScale(0) : quantity;
        this.quantity = quantity;
    }

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 含税单价 */
    @Excel(name = "含税单价")
    private BigDecimal taxPrice;

    /** 折扣率 */
    @Excel(name = "折扣率")
    private Double discount;

    /** 是否赠品
     * 字典sys_yes_no
     * */
    @Excel(name = "是否赠品",readConverterExp = "Y=是,N=否")
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

    /** 要货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    @TableField(exist = false)
    private SalOrder salOrder;

    /** 状态
     * 字典salorder_status
     * */
    @Excel(name = "订单明细状态", readConverterExp = "A=待处理,B=生产中,C=已发货,D=已关闭")
    private String status;

    /** 扩展字段 */
    private String extendField;

    public void setExtendField(String extendField) {
        this.extendField = extendField;
        this.extendFieldJson = JSONObject.parseObject(extendField);
    }

    /** 扩展字段 String转json*/
    @TableField(exist = false)
    private JSONObject extendFieldJson;

    /** 已出库数量 */
    @TableField(exist = false)
    private Float saleOutQuantity;

}
