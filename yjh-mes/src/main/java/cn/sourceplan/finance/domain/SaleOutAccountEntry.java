package cn.sourceplan.finance.domain;

import java.math.BigDecimal;

import cn.sourceplan.common.annotation.Excels;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 销售出库对账明细对象 fm_sale_out_account_entry
 *
 * @author jinzhong
 * @date 2023-08-01
 */
@Data
@TableName("fm_sale_out_account_entry")
public class SaleOutAccountEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    // 多个字段导出
    @Excels({
            @Excel(name = "出库单号", targetAttr = "saleOutNumber", type = Excel.Type.EXPORT),
            @Excel(name = "客户名称", targetAttr = "customerName", type = Excel.Type.EXPORT),
            @Excel(name = "收货车间", targetAttr = "shipLocation", type = Excel.Type.EXPORT)

    })
    private SaleOutAccountBill saleOutAccountBill;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 排序 */
    @Excel(name = "排序")
    private int sort;

    /** 主表ID */
    private Long mainId;

    /** 物料ID */
    private Long materialId;

    /** 物料编号 */
    private String materialNumber;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 单位ID */
    private Long materialUnitId;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String materialUnitName;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal quantity;

    /** 副单位 */
    private String deputyUnit;

    /** 副单位数量 */
    private BigDecimal deputyUnitQuantity;

    /** 标准单价 */
    @Excel(name = "标准单价")
    private BigDecimal standardPrice;

    /** 标准金额 */
    @Excel(name = "标准金额")
    private BigDecimal standardAmount;

    /** 销售单价 */
    @Excel(name = "销售单价")
    private BigDecimal salePrice;

    /** 销售金额 */
    @Excel(name = "销售金额")
    private BigDecimal saleAmount;

    /** 折扣金额 */
    @Excel(name = "折扣金额")
    private BigDecimal discountAmount;

    /** 折扣率 */
    @Excel(name = "折扣率")
    private Double discountRate;

    /** 扩展字段 */
    @Excel(name = "扩展字段")
    private String extendField;

    public void setExtendField(String extendField) {
        this.extendField = extendField;
        this.extendFieldJson = JSONObject.parseObject(extendField);
    }

    /** 扩展字段 String转json*/
    @TableField(exist = false)
    private JSONObject extendFieldJson;

}
