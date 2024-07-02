package cn.sourceplan.integration.domain;


import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 集成方案字段对象 inte_plan_field
 *
 * @author 靳忠
 * @date 2023-02-13
 */
public class IntePlanField extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 主表ID */
    private Long mainId;

    /** 序号 */
    @Excel(name = "序号")
    private Long sort;

    /** mes字段名称 */
    @Excel(name = "mes字段名称")
    private String mesName;

    /** mes字段 */
    @Excel(name = "mes字段")
    private String mesField;

    /** 源单字段名称 */
    @Excel(name = "源单字段名称")
    private String souceName;

    /** 源单字段 */
    @Excel(name = "源单字段")
    private String souceField;

    /** 是否查询 */
    @Excel(name = "是否查询")
    private String isQuery;

    /** 状态 */
    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMainId(Long mainId)
    {
        this.mainId = mainId;
    }

    public Long getMainId()
    {
        return mainId;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }
    public void setMesName(String mesName)
    {
        this.mesName = mesName;
    }

    public String getMesName()
    {
        return mesName;
    }
    public void setMesField(String mesField)
    {
        this.mesField = mesField;
    }

    public String getMesField()
    {
        return mesField;
    }
    public void setSouceName(String souceName)
    {
        this.souceName = souceName;
    }

    public String getSouceName()
    {
        return souceName;
    }
    public void setSouceField(String souceField)
    {
        this.souceField = souceField;
    }

    public String getSouceField()
    {
        return souceField;
    }
    public void setIsQuery(String isQuery)
    {
        this.isQuery = isQuery;
    }

    public String getIsQuery()
    {
        return isQuery;
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
            .append("mainId", getMainId())
            .append("sort", getSort())
            .append("mesName", getMesName())
            .append("mesField", getMesField())
            .append("souceName", getSouceName())
            .append("souceField", getSouceField())
            .append("isQuery", getIsQuery())
            .append("remark", getRemark())
            .append("status", getStatus())
            .toString();
    }
}
