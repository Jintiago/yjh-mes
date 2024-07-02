package cn.sourceplan.finance.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 天润薄膜核算维护对象 tr_bomoweihu
 *
 * @author jinzhong
 * @date 2023-07-04
 */
@Data
@TableName("tr_bomoweihu")
public class Bomoweihu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 核算方式 */
    @Excel(name = "核算方式")
    private String priceType;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 原料价格 */
    @Excel(name = "原料价格")
    private BigDecimal yuanliaojg;

    /** 胶水价格 */
    @Excel(name = "胶水价格")
    private BigDecimal jiaoshuijg;

    /** 特殊胶水价格 */
    @Excel(name = "特殊胶水价格")
    private BigDecimal jiaoshuijgB;

}
