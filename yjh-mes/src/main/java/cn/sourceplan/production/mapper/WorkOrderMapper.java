package cn.sourceplan.production.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.production.domain.WorkOrder;
import cn.sourceplan.production.domain.WorkOrderEntry;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 工单Mapper接口
 *
 * @author jinzhong
 * @date 2022-12-12
 */
@Repository
public interface WorkOrderMapper extends BaseMapper<WorkOrder>
{
    /**
     * 查询工单
     *
     * @param id 工单主键
     * @return 工单
     */
    WorkOrder selectWorkOrderById(Long id);


    /**
     * 根据工单子表ID获取工单详细信息
     *  @param id  workOrderEntryId
     *  @return 结果
     */
    WorkOrder selectWorkOrderByEntryId (Long id);

    /**
     * 查询工单列表
     *
     * @param workOrder 工单
     * @return 工单集合
     */
    List<WorkOrder> selectWorkOrderList(WorkOrder workOrder);

    /**
     * 批量删除工单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteWorkOrderByIds(Long[] ids);

    /**
     * 批量删除工单分录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteWorkOrderEntryByWorkorderIds(Long[] ids);

    /**
     * 通过工单主键删除工单分录信息
     *
     * @param id 工单ID
     * @return 结果
     */
    int deleteWorkOrderEntryByWorkorderId(Long id);

    /**
     * 通过工单主键删除工单分录信息
     *
     * @param ids 工单ID
     *  @param proStatus 生产状态
     * @return 结果
     */
    int updateProStatus (@Param("ids") String ids, @Param("proStatus") String proStatus);


    /**
     * 查询工单(移动端)
     * 可自定义查询条件，后台也可用
     * @param map 查询参数
     * @return 工单列表
     */
    List<WorkOrder> selectWorkOrderListForMobile(@Param("map")Map<String, Object> map);


    /**
     * 传入where条件查询符合的工单条数
     *
     * @param whereSql where条件
     * @return 结果
     */
    int selectWorkOrderCount (@Param("whereSql") String whereSql);

    /**
     * 给工单进行自定义排序
     *
     * @param workOrder 工单
     * @return 结果
     */
    int updateCustomSort(@Param("workOrder") WorkOrder workOrder);

}
