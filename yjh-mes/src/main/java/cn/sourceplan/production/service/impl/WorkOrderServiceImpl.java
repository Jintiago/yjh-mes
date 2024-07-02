package cn.sourceplan.production.service.impl;

import java.math.BigDecimal;
import java.util.*;

import cn.hutool.core.date.DateUtil;
import cn.sourceplan.sale.domain.SalOrder;
import cn.sourceplan.sale.mapper.SalOrderMapper;
import cn.sourceplan.sale.service.ISalOrderService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import cn.sourceplan.common.constant.HttpStatus;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.core.page.TableDataInfo;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.domain.SysUserStation;
import cn.sourceplan.common.mapper.SysUserStationMapper;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.common.utils.*;
import cn.sourceplan.masterdata.domain.Bom;
import cn.sourceplan.masterdata.domain.Material;
import cn.sourceplan.masterdata.domain.Station;
import cn.sourceplan.masterdata.mapper.BomMapper;
import cn.sourceplan.masterdata.mapper.MaterialMapper;
import cn.sourceplan.masterdata.mapper.StationMapper;
import cn.sourceplan.production.domain.Route;
import cn.sourceplan.production.domain.RouteProcess;
import cn.sourceplan.production.mapper.RouteMapper;
import cn.sourceplan.production.mapper.WorkOrderEntryMapper;
import cn.sourceplan.sale.domain.SalOrderEntry;
import cn.sourceplan.sale.mapper.SalOrderEntryMapper;
import cn.sourceplan.warehouse.domain.RealTimeInventory;
import cn.sourceplan.warehouse.mapper.RealTimeInventoryMapper;
import javafx.beans.binding.BooleanBinding;
import org.jeecgframework.minidao.annotation.Sql;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.production.domain.WorkOrderEntry;
import cn.sourceplan.production.mapper.WorkOrderMapper;
import cn.sourceplan.production.domain.WorkOrder;
import cn.sourceplan.production.service.IWorkOrderService;

import static cn.sourceplan.common.utils.PageUtils.startPage;


/**
 * 工单Service业务层处理
 *
 * @author jinzhong
 * @date 2022-12-12
 */
@Service
public class WorkOrderServiceImpl implements IWorkOrderService
{
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private WorkOrderEntryMapper workOrderEntryMapper;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private RealTimeInventoryMapper realTimeInventoryMapper;
    @Autowired
    private SalOrderEntryMapper salOrderEntryMapper;

    @Autowired
    private SalOrderMapper salOrderMapper;

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private BomMapper bomMapper;

    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private SysUserStationMapper sysUserStationMapper;

    @Autowired
    private ISalOrderService salOrderService;

    int parentIndex =-1;

    /**
     * 查询工单
     *
     * @param id 工单主键
     * @return 工单
     */
    @Override
    public WorkOrder selectWorkOrderById(Long id)
    {
        WorkOrder workOrder = workOrderMapper.selectWorkOrderById(id);
        //查询对应销售订单
        JSONObject j =  JSONObject.parseObject(workOrder.getSourceInfo());
        try {
            JSONArray saleOrderEntryId = j.getJSONArray("saleOrderEntryId");
            if(saleOrderEntryId!=null&&saleOrderEntryId.size()>0){
                SalOrderEntry salOrderEntry = salOrderEntryMapper.selectById(saleOrderEntryId.getLong(0));
                if(salOrderEntry!=null){
                    SalOrder salOrder = salOrderService.selectSalOrderById(salOrderEntry.getMainId());
                    workOrder.setSalOrder(salOrder);
                }
            }
            return workOrder;
        }catch (JSONException e) {
            Long saleOrderEntryId = j.getLong("saleOrderEntryId");
            SalOrderEntry salOrderEntry = salOrderEntryMapper.selectById(saleOrderEntryId);
            SalOrder salOrder = salOrderService.selectSalOrderById(salOrderEntry.getMainId());
            workOrder.setSalOrder(salOrder);
            return workOrder;
        }


    }

    /**
     * 查询工单列表
     *
     * @param workOrder 工单
     * @return 工单
     */
    @Override
    public TableDataInfo selectWorkOrderList(WorkOrder workOrder,Boolean isAll)
    {
        String proStatus = "";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(workOrder.getProStatus())){
            String[] split = workOrder.getProStatus().split(",");
            for (int i = 0; i < split.length; i++) {
                proStatus+="'"+split[i]+"'";
                if(i!=split.length-1){
                    proStatus+=",";
                }
            }
        }
        workOrder.setProStatus(proStatus);

        Map<String, Object> map = workOrder.getParams();
        //拼接子表查询语句
        String entryWhereSql = "";
        //查询工序
        Boolean isSort = false;
        String processIds = (String)map.get("processIds");
        if(StringUtils.isNotBlank(processIds)){
            entryWhereSql+="and b.process_id in ("+processIds+")";
            isSort=true;
        }
        //查询设备
        String equipmentIds = (String)map.get("equipmentIds");
        if(StringUtils.isNotBlank(equipmentIds)){
            entryWhereSql+="and b.machine_id in ("+equipmentIds+")";
            isSort=true;
        }
        map.put("entryWhereSql",entryWhereSql);
        if(isSort){
            map.put("sortSql"," custom_sort asc, ");
        }
        //根据销售编号查询
        String saleOrderNumber = (String)map.get("saleOrderNumber");
        //根据销售订单客户名称查询
        String customerName = (String)map.get("saleOrderCustomerName");
        if(StringUtils.isNotBlank(saleOrderNumber)||StringUtils.isNotBlank(customerName)){
            QueryWrapper<SalOrder> qwSalOrder = new QueryWrapper<>();
            if(StringUtils.isNotBlank(saleOrderNumber)){
                qwSalOrder.eq("number",saleOrderNumber);
            }
            if(StringUtils.isNotBlank(customerName)){
                qwSalOrder.like("customer_name",customerName);
            }
            List<SalOrder> salOrders = salOrderMapper.selectList(qwSalOrder);
            if(salOrders!=null&&salOrders.size()>0){
                Long[] lmArr = new Long[salOrders.size()];
                for (int i = 0; i < salOrders.size(); i++) {
                    SalOrder salOrder = salOrders.get(i);
                    lmArr[i]=salOrder.getId();
                }
                QueryWrapper<SalOrderEntry> qwSalOrderEntry = new QueryWrapper<>();
                qwSalOrderEntry.in("main_id",lmArr);
                List<SalOrderEntry> salOrderEntryList = salOrderEntryMapper.selectList(qwSalOrderEntry);
                Long[] lArr = new Long[salOrderEntryList.size()];
                for (int j = 0; j < salOrderEntryList.size(); j++) {
                    SalOrderEntry salOrderEntry = salOrderEntryList.get(j);
                    lArr[j]=salOrderEntry.getId();
                }
                if(lArr.length>0){
                    workOrder.setSourceInfo(SqlCheckUtil.querySourceInfo("saleOrderEntryId",lArr));
                }
            }else{
                workOrder.setSourceInfo(" and 1=-1 ");
            }
        }

        workOrder.setParams(map);
        //先查所有
        List<WorkOrder> workOrderListALl = workOrderMapper.selectWorkOrderList(workOrder);
        //初始化自定义排序
        if(isSort){
            workOrderListALl =initCustomSort(workOrderListALl);
        }

        List<WorkOrder> workOrderList;
        int size = workOrderListALl.size();
        if(!isAll){
            //分页查询
            Integer pageNum = Integer.valueOf(ServletUtils.getParameter("pageNum"))  ;
            Integer pageSize = Integer.valueOf(ServletUtils.getParameter("pageSize")) ;
            //如果时最后一页
            if(size<(pageNum-1)*pageSize+pageSize){
                workOrderList = workOrderListALl.subList((pageNum-1)*pageSize,size);
            }else{
                workOrderList = workOrderListALl.subList((pageNum-1)*pageSize,(pageNum-1)*pageSize+pageSize);
            }

        }else{
            //查所有
            workOrderList=workOrderListALl;
        }
        workOrderList =setSaleOrder(workOrderList);
        TableDataInfo dataTable =getDataTable(workOrderList);
        dataTable.setTotal(size);
        return dataTable;
    }

    @Override
    public TableDataInfo listForMobile(WorkOrder workOrder) {
        Map<String, Object> map =new HashMap<>(64);
        //拼接主表查询语句
        String mainWhereSq =
                //已关闭的不返回
                "and a.pro_status !='E' ";
        map.put("mainWhereSq",mainWhereSq);

        String mobileStatus = workOrder.getMobileStatus();
        String xBind ="X";
        if(xBind.equals(mobileStatus)){
            //未绑定工位的返回mobileStatus标记为X
            //根据当前工位所绑定的工序查工单子表，工单子表中未绑定工位的
            //拼接子表查询语句
            String entryWhereSql =
                    //根据工位ID查工单子表,
                    "and b.station_id = null or  trim(b.station_id)='' order by a.number desc ";
            map.put("entryWhereSql",entryWhereSql);

            List<WorkOrder> workOrderList = workOrderMapper.selectWorkOrderListForMobile(map);
            TableDataInfo dataTable =getDataTable(workOrderList);
            return dataTable;
        }

        //根据当前工位所绑定的工序查工单子表主表
        //如果具有超级管理员角色,可查看并操作所有工位的订单
        Long userId = SecurityUtils.getUserId();
        SysUserStation sysUserStation = sysUserStationMapper.getInfoByUserId(userId);
        if(sysUserStation==null){
            return null;
        }
        //获取当前工位ID
        Long stationId = sysUserStation.getStationId();
        //拼接子表查询语句
        String entryWhereSql =
                //根据工位ID查工单子表,
                " and  b.station_id =  "+stationId ;
                //根据设备ID查工单子表
                if(StringUtils.isNotBlank(String.valueOf(sysUserStation.getEquipmentId()))){
                    entryWhereSql+= " and  b.machine_id =  "+sysUserStation.getEquipmentId() ;
                }

        //有可能工单主表处于未完成状态，但是明细已经完成
        if(mobileStatus.equals("B")){
            entryWhereSql+= " AND  ( b.`STATUS` is null or  b. `STATUS`!= 'B'  )  "
                    +" and a.pro_status = 'B' order by a.custom_sort, a.priority asc";
        }
        if(mobileStatus.equals("D")){
            entryWhereSql+= " and (a.pro_status = 'D' or    b. `STATUS`= 'B'  ) order by a.number desc ";
        }
        map.put("entryWhereSql",entryWhereSql);
        List<WorkOrder> workOrderList = workOrderMapper.selectWorkOrderListForMobile(map);

        //查询对应销售订单
        workOrderList =setSaleOrder(workOrderList);

        TableDataInfo dataTable =getDataTable(workOrderList);
        return dataTable;
    }

    @Override
    public WorkOrder selectWorkOrderByEntryId(Long entryId) {

        return workOrderMapper.selectWorkOrderByEntryId(entryId);
    }

    @Override
    public AjaxResult batchAddByJson(JSONObject jsonObject) {

        JSONArray workOrderList = jsonObject.getJSONArray("workOrderList");
        JSONObject workOrderJson =  workOrderList.getJSONObject(0);
        WorkOrder workOrder = JSON.toJavaObject(workOrderJson, WorkOrder.class);
        JSONObject woJB = workOrder.getExtendFieldJson();
        if(woJB!=null){
            workOrder.setExtendField(woJB.toJSONString());
        }
        List<WorkOrderEntry>  entryList = new ArrayList<>();
        JSONArray pickEntryListJ = workOrderJson.getJSONArray("pickEntryList");
        for (int j = 0; j < pickEntryListJ.size(); j++) {
            JSONObject jbA = pickEntryListJ.getJSONObject(j);
            WorkOrderEntry woeA = JSON.toJavaObject(jbA, WorkOrderEntry.class);
            entryList.add(woeA);
        }
        JSONArray reportEntryListJ = workOrderJson.getJSONArray("reportEntryList");
        for (int g = 0; g < reportEntryListJ.size(); g++) {
            JSONObject jbB  = reportEntryListJ.getJSONObject(g);
            WorkOrderEntry woeB = JSON.toJavaObject(jbB, WorkOrderEntry.class);
            JSONObject extendFieldJson = woeB.getExtendFieldJson();
            if(extendFieldJson!=null){
                woeB.setExtendField(extendFieldJson.toJSONString());
            }
            entryList.add(woeB);
        }
        List<WorkOrder> workOrderL = new ArrayList<>();
        workOrder.setWorkOrderEntryList(entryList);
        BigDecimal num = new BigDecimal(0.0);
        StringBuilder sb = new StringBuilder();

        JSONArray jArr = new JSONArray();
        String annex ="";
        for (int i = 0; i < workOrderList.size(); i++) {
            JSONObject woJson =  workOrderList.getJSONObject(i);
            JSONObject sourceInfo = woJson.getJSONObject("sourceInfo");
            Long saleOrderEntryId = sourceInfo.getLong("saleOrderEntryId");
            jArr.add(saleOrderEntryId);
            WorkOrder wo = JSON.toJavaObject(woJson, WorkOrder.class);
            sb.append(wo.getSpecification()).append(",");
            num=num.add(wo.getQuantity());
            if(StringUtils.isNotBlank(wo.getAnnex())){
                annex+=wo.getAnnex()+",";
            }
        }
        JSONObject j = new JSONObject();
        j.put("saleOrderEntryId",jArr);
        workOrder.setSourceInfo(j.toString());
        workOrder.setSpecification(sb.toString());
        workOrder.setQuantity(num);
        workOrder.setAnnex(annex);
        workOrderL.add(workOrder);
        batchInsertWorkOrder(workOrderL);
        return AjaxResult.success();
    }

    /**
     * 新增工单
     *
     * @param workOrder 工单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertWorkOrder(WorkOrder workOrder)
    {
        //如果编号为null,则自动为其生成编号
        if ( StringUtils.isBlank(workOrder.getNumber()) ){
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("WorkOrder");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
            workOrder.setNumber(number);
        }

        int rows = workOrderMapper.insert(workOrder);
        insertWorkOrderEntry(workOrder);
        return rows;
    }

    @Override
    public int batchInsertWorkOrder(List<WorkOrder> workOrderList) {
        int rows =0;
        for (int i = 0; i < workOrderList.size(); i++) {
            WorkOrder workOrder = workOrderList.get(i);
            if("false".equals(workOrder.getSearchValue())){
                continue;
            }
            rows+= insertWorkOrder(workOrder);
            List<WorkOrder> childrenList = workOrder.getChildren();
            if(childrenList!=null&&childrenList.size()>0){
                for (int j = 0; j < childrenList.size(); j++) {
                    WorkOrder children = childrenList.get(j);
                    if("false".equals(children.getSearchValue())){
                        continue;
                    }
                    rows+= insertWorkOrder(children);
                }
            }
        }
        return rows;
    }

    /**
     * 修改工单
     *
     * @param workOrder 工单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult updateWorkOrder(WorkOrder workOrder)
    {

        //校验数据引用关系
        //根据当前工单ID查旧工单
        WorkOrder workOrderOld = workOrderMapper.selectWorkOrderById(workOrder.getId());
        List<WorkOrderEntry> workOrderEntryList = workOrderOld.getWorkOrderEntryList();
        //获取旧工单EntryList
        StringBuilder sb = new StringBuilder();
        //将旧工单EntryList的id 拼接成逗号分割字符串
        for (int i = 0; i < workOrderEntryList.size(); i++) {
            sb.append(workOrderEntryList.get(i).getId());
            if(i!=workOrderEntryList.size()-1){
                sb.append(",");
            }
        }
        //报工单
//        String reportCheck = SqlCheckUtil.sqlCheck("number","pro_report ",
//                "where work_order_entry_id in( "+sb+") and status ='A'");
//        if(reportCheck!=null){
//            return AjaxResult.error("该数据在报工单["+ reportCheck +"]中已被引用，无法修改");
//        }
        workOrder.setUpdateTime(DateUtils.getNowDate());
        workOrderMapper.deleteWorkOrderEntryByWorkorderId(workOrder.getId());
        insertWorkOrderEntry(workOrder);

        int i = workOrderMapper.updateById(workOrder);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 批量删除工单
     *
     * @param ids 需要删除的工单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult deleteWorkOrderByIds(Long[] ids)
    {
        //校验数据引用关系
        //根据当前工单ID查旧工单
        QueryWrapper<WorkOrderEntry> qw = new QueryWrapper<>();
        qw.in("workorder_id",ids);
        List<WorkOrderEntry> workOrderEntries = workOrderEntryMapper.selectList(qw);
        StringBuilder sb = new StringBuilder();
        //将旧工单EntryList的id 拼接成逗号分割字符串
        for (int j = 0; j < workOrderEntries.size(); j++) {
            sb.append(workOrderEntries.get(j).getId());
            if(j!=workOrderEntries.size()-1){
                sb.append(",");
            }
        }
        //报工单
        String reportCheck = SqlCheckUtil.sqlCheck("number","pro_report ",
                "where work_order_entry_id in( "+sb+") and status ='A'");
        if(reportCheck!=null){
            return AjaxResult.error("所选数据在报工单["+ reportCheck +"]中已被引用，无法修改");
        }
        workOrderMapper.deleteWorkOrderEntryByWorkorderIds(ids);
        int i = workOrderMapper.deleteWorkOrderByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    @Override
    public AjaxResult updateProStatus(Long[] ids,String proStatus) {
        //开始排产
        if( "B".equals(proStatus)){
            //只有生产状态为A或者C的工单才能开始排产
            for (int i = 0; i < ids.length; i++) {
                WorkOrder workOrder = workOrderMapper.selectWorkOrderById(ids[i]);
                //如果来源信息sourceInfo中saleOrderEntryId的不为空 {"saleOrderEntryId":42}
                String sourceInfo = workOrder.getSourceInfo();
                if(org.apache.commons.lang3.StringUtils.isNotBlank(sourceInfo)){
                    //将对应销售订单分录的状态改为生产中
                    JSONObject sourceInfoJ = JSONObject.parseObject(sourceInfo);
                    JSONArray saleOrderEntryIdArr = sourceInfoJ.getJSONArray("saleOrderEntryId");
                    //得到的可能是数组
                    for (int j = 0; j < saleOrderEntryIdArr.size(); j++) {
                        Long saleOrderEntryId = saleOrderEntryIdArr.getLong(j);
                        if(saleOrderEntryId!=null){
                            SalOrderEntry salOrderEntry = salOrderEntryMapper.selectById(saleOrderEntryId);
                            salOrderEntry.setStatus("B");
                            salOrderEntryMapper.updateById(salOrderEntry);
                        }
                    }



                }
                if(!("A".equals(workOrder.getProStatus())  ||"C".equals(workOrder.getProStatus()))){
                    return AjaxResult.error("只有待排产或已暂停的工单才可以进行排产操作!请重新勾选!");
                }
            }
        }
        //工单暂停
        if("C".equals(proStatus)){
            //只有生产状态为B的工单才能暂停
            for (int i = 0; i < ids.length; i++) {
                WorkOrder workOrder = workOrderMapper.selectWorkOrderById(ids[i]);
                if(!"B".equals(workOrder.getProStatus())  ){
                    return AjaxResult.error("只有排产中的工单才可以进行暂停操作!请重新勾选!");
                }
            }
        }
        String idsStr ="";
        for (int i = 0; i < ids.length; i++) {
            idsStr+=ids[i];
            if(i!=ids.length-1){
                idsStr+=",";
            }
        }
        int i = workOrderMapper.updateProStatus(idsStr, proStatus);

        return AjaxResult.success("执行成功:"+i+"条");
    }

    @Override
    public AjaxResult preview(String ids, String billType) {
        List<WorkOrder> workOrderList = new ArrayList<>();
        if("saleOrder".equals(billType)){
            //根据订单明细ID查询出所有订单明细
            QueryWrapper<SalOrderEntry> qw =new QueryWrapper<>();
            qw.inSql("id",ids);
            List<SalOrderEntry> entryList = salOrderEntryMapper.selectList(qw);
            if(entryList==null||entryList.size()<1){
                return AjaxResult.error("所选单据ID有异常，请联系管理员处理");
            }

            Set<Long> materialIdSet = new HashSet<>();
            List<SalOrderEntry> entrySumList = new ArrayList<>();

            for (int i = 0; i < entryList.size(); i++) {
                //获取所选中的订单明细
                SalOrderEntry entry = entryList.get(i);
                Long materialId = entry.getMaterialId();
                //去重,为了接下来将同样产品ID的订单合并为一个
                materialIdSet.add(materialId);
            }
            for (Long materialId : materialIdSet) {
                List<SalOrderEntry> collect = entryList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                SalOrderEntry tmp = new SalOrderEntry();
                BeanUtils.copyProperties(collect.get(0),tmp);
                BigDecimal quantitySum = new BigDecimal("0.0");
                for (int i = 0; i < collect.size(); i++) {
                    BigDecimal quantity = collect.get(i).getQuantity();
                    quantitySum=quantitySum.add(quantity);
                }
                tmp.setQuantity(quantitySum);
                entrySumList.add(tmp);
            }
            for (int i = 0; i < entrySumList.size(); i++) {
                SalOrderEntry entry = entrySumList.get(i);
                WorkOrder wo = previewRecursion(entry);
                if(wo==null){
                    return  AjaxResult.error("产品：'"+entry.getMaterialName()+"'已被删除，无法生成排产计划。");
                }
                workOrderList.add(wo);
            }
            System.out.println(entrySumList);
        }
        List<WorkOrder> workOrderList1 = generateIndex(workOrderList, true);
        parentIndex=-1;
        return AjaxResult.success(  workOrderList1 );
    }

    /**
     * 新增工单分录信息
     *
     * @param workOrder 工单对象
     */
    public void insertWorkOrderEntry(WorkOrder workOrder)
    {
        List<WorkOrderEntry> workOrderEntryList = workOrder.getWorkOrderEntryList();
        Long id = workOrder.getId();
        if (StringUtils.isNotNull(workOrderEntryList))
        {
            List<WorkOrderEntry> list = new ArrayList<WorkOrderEntry>();
            for (WorkOrderEntry workOrderEntry : workOrderEntryList)
            {
                JSONObject extendFieldJson = workOrderEntry.getExtendFieldJson();
                if(extendFieldJson==null){
                    extendFieldJson=JSONObject.parseObject("{}");
                }
                workOrderEntry.setExtendField(extendFieldJson.toJSONString());
                workOrderEntry.setWorkorderId(id);
                list.add(workOrderEntry);
            }
            if (list.size() > 0)
            {
                workOrderEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }

    public WorkOrder previewRecursion(SalOrderEntry entry){
        Long materialId = entry.getMaterialId();
        //取得生产工单所需要的字段
        WorkOrder wo =new WorkOrder();
        wo.setSourceType("SalOrder");
        wo.setSourceSystem("Self");
        wo.setMaterialId(materialId);
        wo.setMaterialNumber(entry.getMaterialNumber());
        wo.setMaterialName(entry.getMaterialName());
        wo.setMaterialUnitId(entry.getUnitId());
        wo.setMaterialUnitName(entry.getUnitName());
        wo.setQuantity(entry.getQuantity());
        //开始生产日期
        wo.setBeginProDate(new Date());
        //计划完成日期
        wo.setPlanFinishDate(DateUtil.nextWeek());
        //实际完成日期
        wo.setProStatus("A");
        wo.setStatus("A");
        //查出库存
        RealTimeInventory queryRti = new RealTimeInventory();
        queryRti.setMaterialId(materialId);
        List<JSONObject> rtiList = realTimeInventoryMapper.selectRealTimeInventoryList(queryRti);
        BigDecimal rti = new BigDecimal("0.0");
        for (int i = 0; i < rtiList.size(); i++) {
            JSONObject realTimeInventory = rtiList.get(i);
            rti=rti.add(new BigDecimal(realTimeInventory.getFloat("quantity")) );
        }
        wo.setInventory(rti.doubleValue());
        //取得生产工单明细所需要的字段
        Material material = materialMapper.selectById(materialId);
        if(material==null){
            return null;
        }
        //查工艺路线
        Route route = routeMapper.selectRouteById(material.getRouteId());
        List<RouteProcess> routeProcessList = route.getRouteProcessList();
        List<WorkOrderEntry> woeList = new ArrayList<>();
        for (int j = 0; j < routeProcessList.size(); j++) {
            WorkOrderEntry workOrderEntry = new WorkOrderEntry();
            RouteProcess routeProcess = routeProcessList.get(j);
            //查工位
            Station station = new Station();
            station.setProcessIds(String.valueOf(routeProcess.getProcessId()));
            List<Station> stations = stationMapper.selectStationList(station);
            //只查找符合条件的第一个
            BeanUtils.copyProperties(stations.size()>0?stations.get(0):new Station(),station);
            workOrderEntry.setFirstQcQuantity(routeProcess.getFirstQcQuantity());
            workOrderEntry.setQcType(routeProcess.getQcType());
            workOrderEntry.setProcessId(routeProcess.getProcessId());
            workOrderEntry.setProcessName(routeProcess.getProcessName());
            workOrderEntry.setWorkshopId(station.getWorkshopId());
            workOrderEntry.setWorkshopName(station.getWorkshopName());
            workOrderEntry.setStationId(station.getId());
            workOrderEntry.setStationName(station.getName());
            workOrderEntry.setSort((long) j+1);
            woeList.add(workOrderEntry);
        }
        wo.setWorkOrderEntryList(woeList);
        //查BOM清单
        Bom queryBom = new Bom();
        queryBom.setMaterialId(materialId);
        queryBom.setStatus("0");
        List<Bom> boms = bomMapper.selectBomList(queryBom);
        if(boms!=null&&boms.size()>0){
            //只查找符合条件的第一个BOM
            BeanUtils.copyProperties(new Bom(),queryBom);
            queryBom.setParentId(boms.get(0).getId());
            List<Bom> childrenBoms = bomMapper.selectBomList(queryBom);
            List<WorkOrder> childrenList = new ArrayList<>();
            //递归查询
            for (int j = 0; j < childrenBoms.size(); j++) {
                SalOrderEntry soeChildren = new SalOrderEntry();
                Bom bom = childrenBoms.get(j);
                BeanUtils.copyProperties(entry, soeChildren);
                soeChildren.setMaterialId(bom.getMaterialId());
                soeChildren.setMaterialNumber(bom.getMaterial().getNumber());
                soeChildren.setMaterialName(bom.getMaterialName());
                soeChildren.setUnitId(bom.getMaterial().getUnitId());
                soeChildren.setUnitName(bom.getMaterial().getUnit().getName());
                soeChildren.setQuantity(entry.getQuantity().multiply(bom.getProportion()));
                childrenList.add(previewRecursion(soeChildren));
            }
            wo.setChildren(childrenList);
        }
        return wo;
    }

    private List<WorkOrder> generateIndex(List<WorkOrder> list,Boolean isParent){
        //需要修改数据结构为
        //  [
        //      { index:0,children:
        //          [{index:0,parentIndex:0},
        //           {index:1,parentIndex:0}]
        //      },
        //      { index:1,children:
        //          [{index:0,parentIndex:1},
        //          {index:1,parentIndex:1}]
        //      }
        //  ]
        for (int i = 0; i < list.size(); i++) {
            WorkOrder workOrder = list.get(i);
            if(isParent){
                workOrder.setIndex(parentIndex+1);
                parentIndex+=1;
            }else{
                workOrder.setIndex(i);
                workOrder.setParentIndex(parentIndex);
            }
            List<WorkOrder> childrenList = workOrder.getChildren();
            if(childrenList!=null&&childrenList.size()>0){
                generateIndex(childrenList,false);
            }
        }
        return list;

    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }


    private List<WorkOrder> setSaleOrder(List<WorkOrder> workOrderList){

        //查询对应销售订单
        for (int i = 0; i < workOrderList.size(); i++) {
            WorkOrder workOrder1 = workOrderList.get(i);
            JSONObject j =  JSONObject.parseObject(workOrder1.getSourceInfo());
            try {
                JSONArray saleOrderEntryId = j.getJSONArray("saleOrderEntryId");
                if(saleOrderEntryId==null||saleOrderEntryId.size()==0){
                    continue;
                }
                SalOrderEntry salOrderEntry = salOrderEntryMapper.selectById(saleOrderEntryId.getLong(0));
                if(salOrderEntry!=null){
                    SalOrder salOrder = salOrderService.selectSalOrderById(salOrderEntry.getMainId());
                    workOrder1.setSalOrder(salOrder);
                }

            }catch (JSONException e){
                Long saleOrderEntryId = j.getLong("saleOrderEntryId");
                if(saleOrderEntryId==null){
                    continue;
                }
                SalOrderEntry salOrderEntry = salOrderEntryMapper.selectById(saleOrderEntryId);
                if(salOrderEntry!=null){
                    SalOrder salOrder = salOrderService.selectSalOrderById(salOrderEntry.getMainId());
                    workOrder1.setSalOrder(salOrder);
                }
            }
        }
        return workOrderList;
    }
    @Override
    public List<WorkOrderEntry> getFrontProcessReport(Long[] salOrderEntryIds, int sort) {

        String sql = SqlCheckUtil.querySourceInfo("saleOrderEntryId", salOrderEntryIds);

        QueryWrapper<WorkOrder> qw = new QueryWrapper<>();
        qw.apply(" pro_status != 'E'  "+sql);
        List<WorkOrder> workOrderList = workOrderMapper.selectList(qw);
        //去重
        workOrderList= workOrderList.stream().distinct().collect(Collectors.toList());
        List<WorkOrderEntry> resList = new ArrayList<>();
        for (int i = 0; i < workOrderList.size(); i++) {
            WorkOrder workOrder = workOrderList.get(i);
            QueryWrapper<WorkOrderEntry> woeQw = new QueryWrapper<>();
            woeQw.eq("workorder_id",workOrder.getId());
            List<WorkOrderEntry> workOrderEntryList = workOrderEntryMapper.selectList(woeQw);
            //查找上一个
            for (int j = 0; j < workOrderEntryList.size(); j++) {
                WorkOrderEntry itemA = workOrderEntryList.get(j);
                itemA.setPickMaterialId(itemA.getReportMaterialId());
                itemA.setPickMaterialName(itemA.getReportMaterialName());
                itemA.setPickQuantity(itemA.getReportQuantity());
                itemA.setPickSpecification(itemA.getReportSpecification());
                itemA.setId(null);
                itemA.setWorkorderId(null);
                itemA.setStationId(null);
                itemA.setStationName(null);
                itemA.setMachineId(null);
                itemA.setMachineName(null);
                if((itemA.getProcessSort()==null?-1:itemA.getProcessSort())== sort-1 && itemA.getType().equals("report")){
                    itemA.setType("pick");
                    resList.add(itemA);
                }
            }
        }
        //上一个没有再找上一级，最多找两级
        if(resList.size()==0){
            for (int i = 0; i < workOrderList.size(); i++) {
                WorkOrder workOrder = workOrderList.get(i);
                QueryWrapper<WorkOrderEntry> woeQw = new QueryWrapper<>();
                woeQw.eq("workorder_id",workOrder.getId());
                List<WorkOrderEntry> workOrderEntryList = workOrderEntryMapper.selectList(woeQw);
                //查找上一个
                for (int j = 0; j < workOrderEntryList.size(); j++) {
                    WorkOrderEntry itemA = workOrderEntryList.get(j);
                    itemA.setPickMaterialId(itemA.getReportMaterialId());
                    itemA.setPickMaterialName(itemA.getReportMaterialName());
                    itemA.setPickQuantity(itemA.getReportQuantity());
                    itemA.setPickSpecification(itemA.getReportSpecification());
                    itemA.setId(null);
                    itemA.setWorkorderId(null);
                    itemA.setStationId(null);
                    itemA.setStationName(null);
                    itemA.setMachineId(null);
                    itemA.setMachineName(null);
                    if(itemA.getProcessSort()== sort-2 && itemA.getType().equals("report")){
                        itemA.setType("pick");
                        resList.add(itemA);
                    }
                }
            }
        }

        return resList;
    }

    private List<WorkOrder> initCustomSort(List<WorkOrder> workOrderListALl) {

        for (int i = 0; i < workOrderListALl.size(); i++) {
            WorkOrder workOrder = workOrderListALl.get(i);
            workOrder.setCustomSort(i+1);
            workOrderMapper.updateById(workOrder);
        }
        return workOrderListALl;
    }

    @Override
    public int updateCustomSort(List<WorkOrder> workOrderList) {
        int l =0;
        for (int i = 0; i < workOrderList.size(); i++) {
            WorkOrder workOrder = workOrderList.get(i);
            l+=workOrderMapper.updateCustomSort(workOrder);;
        }
        return l;
    }
}
