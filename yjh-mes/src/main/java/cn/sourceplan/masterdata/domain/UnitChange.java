package cn.sourceplan.masterdata.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 计量单位转换对象 md_unit_change
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Data
public class UnitChange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 主单位 */
    @Excel(name = "主单位")
    private Long mainUnit;

    /** 副单位 */
    @Excel(name = "副单位")
    private Long deputyUnit;

    /** 比例 */
    @Excel(name = "比例")
    private BigDecimal proportion;

}
