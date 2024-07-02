package cn.sourceplan.buy.domain;

import java.math.BigDecimal;
import java.util.List;
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
 * 采购订单对象 buy_order
 *
 * @author jinzhong
 * @date 2023-08-30
 */
@TableName("buy_order")
@Data
public class BuyOrder extends BaseEntity
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

    /** 采购部门id */
    @Excel(name = "采购部门id")
    private Long deptId;

    /** 采购部门 */
    @Excel(name = "采购部门")
    private String deptName;

    /** 采购员id */
    @Excel(name = "采购员id")
    private Long userId;

    /** 采购员 */
    @Excel(name = "采购员")
    private String userName;

    /** 采购日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采购日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date buyDate;

    /** 供应商ID */
    @Excel(name = "供应商ID")
    private Long supplierId;

    /** 供应商 */
    @Excel(name = "供应商")
    private String supplierName;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checker;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 采购订单明细信息 */
    private List<BuyOrderEntry> buyOrderEntryList;


}
