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
 * 供应商对象 md_supplier
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@Data
@TableName(value = "md_supplier")
public class Supplier extends BaseEntity
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
    private String nameAbb;

    /** 英文名 */
    private String nameEn;

    /** 简介 */
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
    private String website;

    /** 邮箱 */
    private String email;

    /** 电话 */
    private String phone;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contacts;

    /** 联系人电话 */
    @Excel(name = "联系人电话")
    private String contactsPhone;

    /** 联系人邮箱 */
    private String contactsEmail;

    /** 联系人2 */
    private String contactsb;

    /** 联系人2电话 */
    private String contactsbPhone;

    /** 联系人2邮箱 */
    private String contactsbEmail;

    /** 社会统一信用代码 */
    private String creditCode;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getNumber()
    {
        return number;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setNameAbb(String nameAbb)
    {
        this.nameAbb = nameAbb;
    }

    public String getNameAbb()
    {
        return nameAbb;
    }
    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }

    public String getNameEn()
    {
        return nameEn;
    }
    public void setCusDescribe(String cusDescribe)
    {
        this.cusDescribe = cusDescribe;
    }

    public String getCusDescribe()
    {
        return cusDescribe;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public String getLogo()
    {
        return logo;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getWebsite()
    {
        return website;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setContacts(String contacts)
    {
        this.contacts = contacts;
    }

    public String getContacts()
    {
        return contacts;
    }
    public void setContactsPhone(String contactsPhone)
    {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsPhone()
    {
        return contactsPhone;
    }
    public void setContactsEmail(String contactsEmail)
    {
        this.contactsEmail = contactsEmail;
    }

    public String getContactsEmail()
    {
        return contactsEmail;
    }
    public void setContactsb(String contactsb)
    {
        this.contactsb = contactsb;
    }

    public String getContactsb()
    {
        return contactsb;
    }
    public void setContactsbPhone(String contactsbPhone)
    {
        this.contactsbPhone = contactsbPhone;
    }

    public String getContactsbPhone()
    {
        return contactsbPhone;
    }
    public void setContactsbEmail(String contactsbEmail)
    {
        this.contactsbEmail = contactsbEmail;
    }

    public String getContactsbEmail()
    {
        return contactsbEmail;
    }
    public void setCreditCode(String creditCode)
    {
        this.creditCode = creditCode;
    }

    public String getCreditCode()
    {
        return creditCode;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("number", getNumber())
            .append("name", getName())
            .append("nameAbb", getNameAbb())
            .append("nameEn", getNameEn())
            .append("cusDescribe", getCusDescribe())
            .append("type", getType())
            .append("logo", getLogo())
            .append("address", getAddress())
            .append("website", getWebsite())
            .append("email", getEmail())
            .append("phone", getPhone())
            .append("contacts", getContacts())
            .append("contactsPhone", getContactsPhone())
            .append("contactsEmail", getContactsEmail())
            .append("contactsb", getContactsb())
            .append("contactsbPhone", getContactsbPhone())
            .append("contactsbEmail", getContactsbEmail())
            .append("creditCode", getCreditCode())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
