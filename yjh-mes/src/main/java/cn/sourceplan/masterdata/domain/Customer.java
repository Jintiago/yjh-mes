package cn.sourceplan.masterdata.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 客户对象 md_customer
 *
 * @author jinzhong
 * @date 2022-09-29
 */
@Data
@TableName(value = "md_customer")
public class Customer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 简称 */
    @Excel(name = "简称")
    private String nameAbb;

    /** 英文名 */
    @Excel(name = "英文名")
    private String nameEn;

    /** 简介 */
    @Excel(name = "简介")
    private String cusDescribe;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** logo */
    @Excel(name = "logo")
    private String logo;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 官网 */
    @Excel(name = "官网")
    private String website;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 电话 */
    @Excel(name = "电话")
    private String phone;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contacts;

    /** 联系人电话 */
    @Excel(name = "联系人电话")
    private String contactsPhone;

    /** 联系人邮箱 */
    @Excel(name = "联系人邮箱")
    private String contactsEmail;

    /** 联系人2 */
    private String contactsb;

    /** 联系人2电话 */
    private String contactsbPhone;

    /** 联系人2邮箱 */
    private String contactsbEmail;

    /** 社会统一信用代码 */
    @Excel(name = "社会统一信用代码")
    private String creditCode;

    /** 状态 */
    @Excel(name = "状态")
    private String status;


}
