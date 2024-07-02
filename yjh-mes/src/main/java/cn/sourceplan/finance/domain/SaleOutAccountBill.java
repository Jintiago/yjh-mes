package cn.sourceplan.finance.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 销售出库对账单对象 fm_sale_out_account_bill
 *
 * @author jinzhong
 * @date 2023-08-01
 */
@Data
@TableName("fm_sale_out_account_bill")
public class SaleOutAccountBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 单号 */
    @Excel(name = "单号")
    private String number;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 收货车间 */
    @Excel(name = "收货车间")
    private String shipLocation;

    /** 销售方式 */
    private String method;

    /** 客户ID */
    private Long customerId;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 开票标记 */
    @Excel(name = "开票标记")
    private String invoiceMark;

    /** 物流信息 */
    private String logisticsInfo;

    /** 仓管员ID */
    private Long keeperId;

    /** 仓管名称 */
    private String keeperName;

    /** 审核人 */
    private String checker;

    /** 审核日期 */
    private Date checkDate;

    /** 出库日期 */
    private Date outTime;

    /** 标准金额 */
    @Excel(name = "标准金额")
    private BigDecimal standardAmount;

    /** 销售金额 */
    @Excel(name = "销售金额")
    private BigDecimal saleAmount;

    /** 折扣金额 */
    @Excel(name = "折扣金额")
    private BigDecimal discountAmount;

    /** 折扣率 */
    @Excel(name = "折扣率")
    private Double discountRate;

    /** 出库单编号 */
    @Excel(name = "出库单编号")
    private String saleOutNumber;

    /** 出库单ID */
    private Long saleOutId;

    /** 销售出库对账明细信息 */
    @TableField(exist = false)
    private List<SaleOutAccountEntry> saleOutAccountEntryList;


}
