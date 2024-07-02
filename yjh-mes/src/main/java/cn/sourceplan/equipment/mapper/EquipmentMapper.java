package cn.sourceplan.equipment.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.equipment.domain.Equipment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 设备信息Mapper接口
 *
 * @author jinzhong
 * @date 2023-04-06
 */
@Repository
public interface EquipmentMapper extends BaseMapper<Equipment>
{

    /**
     * 查询设备OEE稼动率
     *
     * @param map 过滤条件
     * @return 结果
     */
    List<Map<String, Object>> oeeUtilizationList(@Param("map") Map<String,Object> map);

    /**
     * 是否已存在设备状态记录
     *
     * @param map 过滤条件
     * @return 结果
     */
    String selectStatusRecord(@Param("map") Map<String,Object> map);

    /**
     * 插入设备状态记录
     *
     * @param map 插入数据
     * @return 结果
     */
    int insertStatusRecord(@Param("map") Map<String,Object> map);


}
