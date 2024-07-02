package cn.sourceplan.production.domain;

import com.baomidou.mybatisplus.annotation.*;
import cn.sourceplan.common.domain.SysFileInfo;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 工序对象 pro_process
 *
 * @author jinzhong
 * @date 2022-09-22
 */
@TableName("pro_process")
@Data
public class Process extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 工序名 */
    @Excel(name = "工序名")
    private String name;

    /** 作业指导书 */
    @Excel(name = "作业指导书")
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    private String guidance;

    /** 注意事项 */
    @Excel(name = "注意事项")
    private String attention;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    @TableField(exist = false)
    private List<SysFileInfo> sysFileInfoList;

    /** 领料审核 */
    @Excel(name = "领料审核")
    private String pickChek;

}
