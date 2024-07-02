package cn.sourceplan.masterdata.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.TreeEntity;

/**
 * 物料分类对象 md_material_class
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Data
public class MaterialClass extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long sort;

}
