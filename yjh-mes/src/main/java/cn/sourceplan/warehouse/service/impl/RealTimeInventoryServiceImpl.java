package cn.sourceplan.warehouse.service.impl;

import java.util.List;

import cn.sourceplan.warehouse.domain.RealTimeInventory;
import cn.sourceplan.warehouse.mapper.RealTimeInventoryMapper;
import cn.sourceplan.warehouse.service.IRealTimeInventoryService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 即时库存Service业务层处理
 *
 * @author jinzhong
 * @date 2022-11-29
 */
@Service
public class RealTimeInventoryServiceImpl implements IRealTimeInventoryService
{
    @Autowired
    private RealTimeInventoryMapper realTimeInventoryMapper;



    /**
     * 查询即时库存列表
     *
     * @param realTimeInventory 即时库存
     * @return 即时库存
     */
    @Override
    public List<JSONObject> selectRealTimeInventoryList(RealTimeInventory realTimeInventory)
    {

        return realTimeInventoryMapper.selectRealTimeInventoryList(realTimeInventory);
    }

    @Override
    public List<String> getSpecificationList(RealTimeInventory realTimeInventory) {

        return realTimeInventoryMapper.getSpecificationList(realTimeInventory);
    }

}
