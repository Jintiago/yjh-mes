package cn.sourceplan.integration.domain;

import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据源字段对象 inte_data_source_field
 *
 * @author 靳忠
 * @date 2023-02-11
 */
public class InteDataSourceField extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 主表ID */
    private Long mainId;

    /** 序号 */
    @Excel(name = "序号")
    private Long sort;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 字段 */
    @Excel(name = "字段")
    private String field;

    /** 是否隐藏 */
    @Excel(name = "是否隐藏")
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
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setField(String field)
    {
        this.field = field;
    }

    public String getField()
    {
        return field;
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
            .append("name", getName())
            .append("field", getField())
            .append("remark", getRemark())
            .append("status", getStatus())
            .toString();
    }
}
