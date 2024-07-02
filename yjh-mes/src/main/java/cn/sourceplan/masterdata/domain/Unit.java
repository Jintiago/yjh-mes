package cn.sourceplan.masterdata.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 计量单位对象 md_unit
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Data
@TableName("md_unit")
public class Unit
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 英文名称 */
    @Excel(name = "英文名称")
    private String nameEn;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

}
