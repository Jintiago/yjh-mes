package cn.sourceplan.common.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 用户工位绑定对象 sys_user_station
 *
 * @author jinzhong
 * @date 2023-02-07
 */
@Data
@TableName(value = "sys_user_station")
public class SysUserStation
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户名称 */
    @Excel(name = "用户名称")
    @TableField(condition = SqlCondition.LIKE)
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    @TableField(condition = SqlCondition.LIKE)
    private String nickName;

    /** 工位ID */
    @Excel(name = "工位ID")
    private Long stationId;

    /** 工位名称 */
    @Excel(name = "工位名称")
    private String stationName;

    /** 创建者 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /** 所属车间名称 */
    @Excel(name = "所属车间名称")
    @TableField(exist = false)
    private String workshopName;

    /** 设备ID */
    private Long equipmentId;

    /** 设备 */
    @Excel(name = "设备")
    private String equipmentName;

}
