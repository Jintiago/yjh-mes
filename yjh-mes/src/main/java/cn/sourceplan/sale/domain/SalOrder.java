package cn.sourceplan.sale.domain;

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
 * 销售订单对象 sal_order
 *
 * @author jinzhong
 * @date 2022-10-08
 */
@TableName("sal_order")
@Data
public class SalOrder extends BaseEntity
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

    /** 源单信息 */
    private String sourceInfo;

    /** 物流单号 */
    private String logisticsNumber;

    /** 销售部门id */
    private Long deptId;

    /** 销售部门 */
    @Excel(name = "销售部门")
    private String deptName;

    /** 销售员id */
    private Long userId;

    /** 销售员 */
    @Excel(name = "销售员")
    private String userName;

    /** 结算币别 */
    private String currency;

    /** 销售日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "销售日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date saleDate;

    @TableField(exist = false)
    private String[] saleDateQuery;

    @TableField(exist = false)
    private String[] deliveryDateQuery;

    /** 客户id */
    private Long customerId;

    /** 客户 */
    @Excel(name = "客户")
    private String customerName;

    /** 收货人 */
    private String consignee;

    /** 收货电话 */
    private String consigneePhone;

    /** 收货单位 */
    private String consigneeDepartment;

    /** 区 */
    private String area;

    /** 详细地址 */
    private String address;


    /** 分录信息 */
    @TableField(exist = false)
    private List<SalOrderEntry> salOrderEntryList;

    /** 附件 */
    private String annex;

    /** 审核人 */
    private String checker;

    /** 开票标记 */
    private String invoiceMark;

}
