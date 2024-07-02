package cn.sourceplan.integration.domain;


import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 集成数据源对象 inte_data_source
 *
 * @author 靳忠
 * @date 2023-02-09
 */
public class IntegrateDataSource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 单号 */
    @Excel(name = "单号")
    private String number;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddress;

    /** 集成方式 */
    @Excel(name = "集成方式")
    private String integrationMode;

    /** AppID */
    @Excel(name = "AppID")
    private String appId;

    /** 令牌 */
    @Excel(name = "令牌")
    private String token;

    /** 密钥 */
    @Excel(name = "密钥")
    private String secretKey;

    /** 账套码 */
    @Excel(name = "账套码")
    private String bookCode;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 源单名称 */
    @Excel(name = "源单名称")
    private String sourceBillName;

    /** 源单内码 */
    @Excel(name = "源单内码")
    private String sourceBillCode;

    /** 请求参数9 */
    private String parameter9;

    /** 请求参数8 */
    private String parameter8;

    /** 请求参数7 */
    private String parameter7;

    /** 请求参数6 */
    private String parameter6;

    /** 请求参数5 */
    private String parameter5;

    /** 请求参数4 */
    private String parameter4;

    /** 请求参数3 */
    private String parameter3;

    /** 请求参数2 */
    private String parameter2;

    /** 请求参数1 */
    private String parameter1;

    /** 请求地址 */
    @Excel(name = "请求地址")
    private String requestAddress;

    /** 请求方式 */
    @Excel(name = "请求方式")
    private String requestMethod;

    /** 集成厂商 */
    private String manufacturer;

    /** 集成系统名称 */
    private String systemName;

    /** 集成系统版本 */
    private String systemVersion;

    /** 数据库类型 */
    @Excel(name = "数据库类型")
    private String databaseType;

    /** JAVA解析类 */
    private String sqlSentence;

    /** 类函数 */
    private String javaClass;

    /** SQL语句 */
    private String javaFunction;

    /** 状态 */
    private String status;

    /** 数据源字段信息 */
    private List<InteDataSourceField> inteDataSourceFieldList;

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public String getJavaFunction() {
        return javaFunction;
    }

    public void setJavaFunction(String javaFunction) {
        this.javaFunction = javaFunction;
    }

    public List<InteDataSourceField> getInteDataSourceFieldList() {
        return inteDataSourceFieldList;
    }

    public void setInteDataSourceFieldList(List<InteDataSourceField> inteDataSourceFieldList) {
        this.inteDataSourceFieldList = inteDataSourceFieldList;
    }

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
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }
    public void setIntegrationMode(String integrationMode)
    {
        this.integrationMode = integrationMode;
    }

    public String getIntegrationMode()
    {
        return integrationMode;
    }
    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppId()
    {
        return appId;
    }
    public void setToken(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }
    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public String getSecretKey()
    {
        return secretKey;
    }
    public void setBookCode(String bookCode)
    {
        this.bookCode = bookCode;
    }

    public String getBookCode()
    {
        return bookCode;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setSourceBillName(String sourceBillName)
    {
        this.sourceBillName = sourceBillName;
    }

    public String getSourceBillName()
    {
        return sourceBillName;
    }
    public void setSourceBillCode(String sourceBillCode)
    {
        this.sourceBillCode = sourceBillCode;
    }

    public String getSourceBillCode()
    {
        return sourceBillCode;
    }
    public void setParameter9(String parameter9)
    {
        this.parameter9 = parameter9;
    }

    public String getParameter9()
    {
        return parameter9;
    }
    public void setParameter8(String parameter8)
    {
        this.parameter8 = parameter8;
    }

    public String getParameter8()
    {
        return parameter8;
    }
    public void setParameter7(String parameter7)
    {
        this.parameter7 = parameter7;
    }

    public String getParameter7()
    {
        return parameter7;
    }
    public void setParameter6(String parameter6)
    {
        this.parameter6 = parameter6;
    }

    public String getParameter6()
    {
        return parameter6;
    }
    public void setParameter5(String parameter5)
    {
        this.parameter5 = parameter5;
    }

    public String getParameter5()
    {
        return parameter5;
    }
    public void setParameter4(String parameter4)
    {
        this.parameter4 = parameter4;
    }

    public String getParameter4()
    {
        return parameter4;
    }
    public void setParameter3(String parameter3)
    {
        this.parameter3 = parameter3;
    }

    public String getParameter3()
    {
        return parameter3;
    }
    public void setParameter2(String parameter2)
    {
        this.parameter2 = parameter2;
    }

    public String getParameter2()
    {
        return parameter2;
    }
    public void setParameter1(String parameter1)
    {
        this.parameter1 = parameter1;
    }

    public String getParameter1()
    {
        return parameter1;
    }
    public void setRequestAddress(String requestAddress)
    {
        this.requestAddress = requestAddress;
    }

    public String getRequestAddress()
    {
        return requestAddress;
    }
    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }
    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }
    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }

    public String getSystemName()
    {
        return systemName;
    }
    public void setSystemVersion(String systemVersion)
    {
        this.systemVersion = systemVersion;
    }

    public String getSystemVersion()
    {
        return systemVersion;
    }
    public void setDatabaseType(String databaseType)
    {
        this.databaseType = databaseType;
    }

    public String getDatabaseType()
    {
        return databaseType;
    }
    public void setSqlSentence(String sqlSentence)
    {
        this.sqlSentence = sqlSentence;
    }

    public String getSqlSentence()
    {
        return sqlSentence;
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
            .append("ipAddress", getIpAddress())
            .append("integrationMode", getIntegrationMode())
            .append("appId", getAppId())
            .append("token", getToken())
            .append("secretKey", getSecretKey())
            .append("bookCode", getBookCode())
            .append("userName", getUserName())
            .append("password", getPassword())
            .append("sourceBillName", getSourceBillName())
            .append("sourceBillCode", getSourceBillCode())
            .append("parameter9", getParameter9())
            .append("parameter8", getParameter8())
            .append("parameter7", getParameter7())
            .append("parameter6", getParameter6())
            .append("parameter5", getParameter5())
            .append("parameter4", getParameter4())
            .append("parameter3", getParameter3())
            .append("parameter2", getParameter2())
            .append("parameter1", getParameter1())
            .append("requestAddress", getRequestAddress())
            .append("requestMethod", getRequestMethod())
            .append("manufacturer", getManufacturer())
            .append("systemName", getSystemName())
            .append("systemVersion", getSystemVersion())
            .append("databaseType", getDatabaseType())
            .append("sqlSentence", getSqlSentence())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
