package cn.sourceplan.integration.domain;


import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 集成方案对象 inte_plan
 *
 * @author 靳忠
 * @date 2023-02-13
 */
public class IntegratePlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 数据源ID */
    private Long dataSourceId;

    /** 数据源名称 */
    @Excel(name = "数据源名称")
    private String dataSourceName;

    /** 源单名称 */
    @Excel(name = "源单名称")
    private String souceBill;

    /** 目标单据 */
    @Excel(name = "目标单据")
    private String targetBill;

    /** 状态 */
    private String status;

    /** 集成方案字段信息 */
    private List<IntePlanField> intePlanFieldList;

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
    public void setDataSourceId(Long dataSourceId)
    {
        this.dataSourceId = dataSourceId;
    }

    public Long getDataSourceId()
    {
        return dataSourceId;
    }
    public void setDataSourceName(String dataSourceName)
    {
        this.dataSourceName = dataSourceName;
    }

    public String getDataSourceName()
    {
        return dataSourceName;
    }
    public void setSouceBill(String souceBill)
    {
        this.souceBill = souceBill;
    }

    public String getSouceBill()
    {
        return souceBill;
    }
    public void setTargetBill(String targetBill)
    {
        this.targetBill = targetBill;
    }

    public String getTargetBill()
    {
        return targetBill;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public List<IntePlanField> getIntePlanFieldList()
    {
        return intePlanFieldList;
    }

    public void setIntePlanFieldList(List<IntePlanField> intePlanFieldList)
    {
        this.intePlanFieldList = intePlanFieldList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("number", getNumber())
            .append("name", getName())
            .append("dataSourceId", getDataSourceId())
            .append("dataSourceName", getDataSourceName())
            .append("souceBill", getSouceBill())
            .append("targetBill", getTargetBill())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("intePlanFieldList", getIntePlanFieldList())
            .toString();
    }
}
