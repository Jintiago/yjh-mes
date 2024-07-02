package cn.sourceplan.warehouse.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 仓库工位绑定对象 wm_warehouse_station
 *
 * @author jinzhong
 * @date 2023-11-04
 */
@TableName("wm_warehouse_station")
@Data
public class WarehouseStation implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 仓库id */
    @Excel(name = "仓库id")
    private Long warehouseId;

    /** 工位id */
    @Excel(name = "工位id")
    private Long stationId;


    /** 仓库 */
    @TableField(exist = false)
    private Warehouse warehouse;

}
