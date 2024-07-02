package cn.sourceplan.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 轮播图对象 sys_carousel
 *
 * @author jinzhong
 * @date 2023-04-19
 */
@Data
@TableName("sys_carousel")
public class Carousel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imagePath;

    /** 类型 */
    @Excel(name = "类型")
    private String type;


}
