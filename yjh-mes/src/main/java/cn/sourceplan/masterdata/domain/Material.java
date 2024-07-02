package cn.sourceplan.masterdata.domain;


import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 物料对象 md_material
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Data
@TableName(value = "md_material")
public class Material extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type=IdType.AUTO)
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    @TableField(value = "name",condition = SqlCondition.LIKE)
    private  String name;

    /** 编号 */
    @Excel(name = "编号")
    @TableField(value = "number",condition = SqlCondition.LIKE)
    private String number;

    /** 规格型号 */
    @Excel(name = "规格型号")
    @TableField(value = "specification",condition = SqlCondition.LIKE)
    private String specification;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private Long unitId;

    /** 物料分类 */
    @Excel(name = "物料分类")
    private Long classId;

    /** 物料类型 */
    @Excel(name = "物料类型")
    private String typeId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    @TableField(exist = false)
    private  Unit unit;

    /** 工序路线 */
    private Long routeId;



}
