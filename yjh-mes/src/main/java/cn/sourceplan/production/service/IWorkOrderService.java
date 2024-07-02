package cn.sourceplan.production.service;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.core.page.TableDataInfo;
import cn.sourceplan.production.domain.WorkOrder;
import cn.sourceplan.production.domain.WorkOrderEntry;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;

/**
 * 工单Service接口
 *
 * @author jinzhong
 * @date 2022-12-12
 */
public interface IWorkOrderService
{
    /**
     * 查询工单
     *
     * @param id 工单主键
     * @return 工单
     */
    WorkOrder selectWorkOrderById(Long id);

    /**
     * 查询工单列表
     *
     * @param workOrder 工单
     * @return 工单集合
     */
    TableDataInfo selectWorkOrderList(WorkOrder workOrder,Boolean isAll);

    /**
     * 新增工单
     *
     * @param workOrder 工单
     * @return 结果
     */
    int insertWorkOrder(WorkOrder workOrder);

    /**
     * 批量新增工单
     *
     * @param workOrderList 工单
     * @return 结果
     */
    int batchInsertWorkOrder(List<WorkOrder> workOrderList);

    /**
     * 修改工单
     *
     * @param workOrder 工单
     * @return 结果
     */
    AjaxResult updateWorkOrder(WorkOrder workOrder);

    /**
     * 批量删除工单
     *
     * @param ids 需要删除的工单主键集合
     * @return 结果
     */
    AjaxResult deleteWorkOrderByIds(Long[] ids);

    /**
     * 批量变更工单生产状态
     *
     * @param ids 需要变更的工单主键集合
     * @param ids 需要变更的状态
     * @return 结果
     */
    AjaxResult updateProStatus(Long[] ids,String proStatus);

    AjaxResult preview(String ids, String billType);


    /**
     * 移动端查询工单列表
     *
     * @param workOrder
     * @return 结果
     */
    TableDataInfo listForMobile(WorkOrder workOrder);

    /**
     * 根据工单子表ID获取工单详细信息
     *  @param id  workOrderEntryId
     *  @return 结果
     */
    WorkOrder selectWorkOrderByEntryId(Long id);

    AjaxResult batchAddByJson(JSONObject jsonObject);

    /**
     * 查询上一道工序的报工数据
     * @param  salOrderEntryIds 销售订单分录ids
     * @param  sort 当前工序顺序
     */
    List<WorkOrderEntry> getFrontProcessReport(Long[] salOrderEntryIds, int sort);

    int updateCustomSort(List<WorkOrder> workOrderList);
}
