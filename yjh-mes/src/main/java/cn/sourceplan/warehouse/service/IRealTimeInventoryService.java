package cn.sourceplan.warehouse.service;

import java.util.List;

import cn.sourceplan.warehouse.domain.RealTimeInventory;
import com.alibaba.fastjson2.JSONObject;

/**
 * 即时库存Service接口
 *
 * @author jinzhong
 * @date 2022-11-29
 */
public interface IRealTimeInventoryService
{


    /**
     * 查询即时库存列表
     *
     * @param realTimeInventory 即时库存
     * @return 即时库存集合
     */
    public List<JSONObject> selectRealTimeInventoryList(RealTimeInventory realTimeInventory);


    /**
     * 查询有库存的规格型号
     * 用于用户输入规格型号时给出建议，如现有即时库存
     * 1060x5.3x8000m库存100个
     * 1060x5.3x8000m库存100个
     * 1060x5.4x8000m库存100个
     * 用户输入1060x5.3时 返回数组1060x5.3x8000m，1060x5.3x8000m
     */
    public List<String> getSpecificationList(RealTimeInventory realTimeInventory);


}
