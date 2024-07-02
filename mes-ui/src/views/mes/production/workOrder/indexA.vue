<!-- 根据不同工序定制排产的生产工单列表 -->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工单编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单编号" prop="saleOrderNumber">
        <el-input
          v-model="queryParams.params.saleOrderNumber"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单客户" prop="saleOrderCustomerName">
        <el-input
          v-model="queryParams.params.saleOrderCustomerName"
          placeholder="请输入订单客户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入产品名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="生产状态" prop="proStatusArr">
        <el-select v-model="queryParams.proStatusArr" placeholder="请选择生产状态" clearable multiple>
          <el-option
            v-for="dict in dict.type.production_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工序" prop="processArr">
        <el-select v-model="queryParams.processArr" placeholder="请选择工序" clearable multiple>
          <el-option
            v-for="item in processList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备" prop="equipmentArr">
        <el-select v-model="queryParams.equipmentArr" placeholder="请选择设备" clearable multiple>
          <el-option
            v-for="item in equipmentList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          plain
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['production:workOrder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate(null)"
          v-hasPermi="['production:workOrder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          icon="el-icon-video-play"
          :disabled="multiple"
          @click="proStatusUpdate('B')"
          v-hasPermi="['production:workOrder:edit']"
        >开始排产</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          icon="el-icon-video-pause"
          :disabled="multiple"
          @click="proStatusUpdate('C')"
          v-hasPermi="['production:workOrder:edit']"
        >工单暂停</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          icon="el-icon-close"
          :disabled="multiple"
          @click="proStatusUpdate('E')"
          v-hasPermi="['production:workOrder:edit']"
        >强制关闭</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          icon="el-icon-link"
          :disabled="single"
          @click="queryReportClick()"
          v-hasPermi="['production:report:list']"
        >下查报工记录</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          plain-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="reportQualityViewClick()"-->
<!--          v-hasPermi="['quality:reportQuality:list']"-->
<!--        >下查质检记录</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          :disabled="single"
          icon="el-icon-caret-top"
          @click="customSort('up')"
        >上移</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          :disabled="single"
          icon="el-icon-caret-bottom"
          @click="customSort('down')"
        >下移</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete(null)"
          v-hasPermi="['production:workOrder:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      <el-dialog
        title="下载"
        :visible.sync="dialogVisible">
          <span v-for="(item,index) in fileList">
            <el-row>
              <a :href="item" target="_blank">{{item}}</a>
            </el-row>
          </span>
      </el-dialog>
    </el-row>
    <el-table v-loading="loading" :data="workOrderList" @selection-change="handleSelectionChange"
              highlight-current-row
              @row-click="onRowClick" height="500"
    class="tableBox">

      <el-table-column type="selection" width="55" align="center" />
      <div v-for="(item,index) in fieldExtendList" >
        <el-table-column :label="item.fieldName" align="center"  :width="item.width" show-overflow-tooltip='true' >
          <template slot-scope="scope">
            <!--  动态属性        -->
            <span v-if="item.type==='date'">
                  {{ parseTime(fieldFormat(scope.row,item), '{y}-{m}-{d}') }}
            </span>
            <span v-else-if="item.type.includes('dict:')">
                  <dict-tag :options="dict.type[dictFormat(item)]" :value="fieldFormat(scope.row,item)"/>
            </span>
            <span v-else-if="item.type==='annex'">
                   <el-link type="primary" @click="fileDownload(scope.row)">{{fieldFormat(scope.row,item)!=null?'下载':''}}</el-link>
            </span>
            <!--  规格信号样式换行处理    -->
            <span v-else-if="item.field==='specification'">
                <el-row v-for="(item,index) in scope.row.specification.split(',')">
                  {{item}}
                </el-row>
            </span>
            <span v-else-if="item.field==='processName'">
                   {{scope.row.workOrderEntryList[0].processName}}
            </span>
            <span v-else>{{fieldFormat(scope.row,item)}}</span>
          </template>
        </el-table-column>
      </div>

<!--      <el-table-column label="优先级" align="center" prop="priority" />-->
<!--      <el-table-column label="开始生产日期" align="center" prop="beginProDate" width="160">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.beginProDate, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="实际完成日期" align="center" prop="realFinishDate" width="160">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.realFinishDate, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row.id)"
            v-hasPermi="['production:workOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['production:workOrder:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-tabs v-model="activeName">
      <el-tab-pane label="领料排产" name="first">
        <el-table  :data="pickEntryList"   ref="pickEntryList"
                   style=""
                   :default-sort = "{prop: 'sort'}" >
          <el-table-column label="工序" width="150" prop="processName"/>
          <el-table-column label="领料仓库" width="150" align="center" prop="outWarehouseName" />
          <el-table-column label="领取物料" width="150"  prop="pickMaterialName" />
          <el-table-column label="规格型号" width="150" align="center" prop="pickSpecification" />
          <el-table-column label="领取数量" width="150" align="center" prop="pickQuantity" />
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="报工排产" name="second">
        <el-table :data="reportEntryList"
                  ref="reportEntryList"
                  style=""
                  :default-sort = "{prop: 'sort'}" >
          >
          <el-table-column label="工序" width="120" prop="processName"/>
          <el-table-column label="报工仓库"  width="120" align="center" prop="inWarehouseName" />
          <el-table-column label="报工物料" width="150" prop="reportMaterialName" />
          <el-table-column label="规格型号" width="150" align="center" prop="reportSpecification"/>
          <el-table-column label="排产数量" width="150" align="center"  prop="reportQuantity" />
          <el-table-column label="工位" width="150" align="center"  prop="stationName" />
          <el-table-column label="设备" width="150" align="center" prop="machineName"   />
          <!--   子表自定义扩展字段     -->
          <div v-for="(item,index) in entryFieldExtendList">
            <el-table-column :label="item.fieldName" align="center"  >
              <template slot-scope="scope">
                <span> {{scope.row.extendFieldJson[item.field]}}</span>
              </template>
            </el-table-column>

<!--              <el-table-column label="操作(详情，质检)" align="center">-->
<!--                <template slot-scope="scope">-->
<!--                  <el-form-item label-width="0" style="margin-bottom: 7px;" :prop="'reportEntryList.'+scope.$index+'.remark'">-->
<!--                    <el-input v-model="scope.row.remark"/>-->
<!--                  </el-form-item>-->
<!--                </template>-->
<!--              </el-table-column>-->
          </div>

        </el-table>
      </el-tab-pane>
<!--      <el-tab-pane label="质检" name="third"></el-tab-pane>-->
    </el-tabs>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {
  listWorkOrder,
  delWorkOrder, editProStatus,getWorkOrder,updateCustomSort
} from '@/api/mes/production/workOrder'

import { listRoute,getRoute } from "@/api/mes/production/route";
import {listProcess} from "@/api/mes/production/process";
import {listEquipment} from "@/api/mes/equipment/equipment";
import {dictFormat, fieldFormat} from "@/utils/jinzhong";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
export default {
  name: "WorkOrder",
  dicts: ['production_status','sys_yes_no'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 工单表格数据
      workOrderList: [],
      pickEntryList :[],
      reportEntryList :[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        number: null,
        batchNumber: null,
        materialId: null,
        materialUnitId: null,
        beginProDate: null,
        planFinishDate: null,
        realFinishDate: null,
        proStatusArr: ['A','B'],
        proStatus:null,
        status: null,
        processArr:[],
        equipmentArr:[],
        params:{
          processIds:null,
          equipmentIds:null,
          saleOrderNumber:null,
          saleOrderCustomerName:null
        }
      },
      // 表单参数
      form: {
        // 工单分录表格数据
        workOrderEntryList: [],
      },
      // 表单校验
      rules: {
        number: [
          { required: true, message: "编号不能为空", trigger: "blur" }
        ],
        materialId: [
          { required: true, message: "产品ID不能为空", trigger: "blur" }
        ],
        materialNumber: [
          { required: true, message: "产品编号不能为空", trigger: "blur" }
        ],
        materialName: [
          { required: true, message: "产品名称不能为空", trigger: "blur" }
        ],
        materialUnitId: [
          { required: true, message: "单位ID不能为空", trigger: "blur" }
        ],
        materialUnitName: [
          { required: true, message: "单位名称不能为空", trigger: "blur" }
        ],
        quantity: [
          { required: true, message: "生产数量不能为空", trigger: "blur" }
        ],
        planFinishDate: [
          { required: true, message: "计划完成日期不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ]
      },
      activeName: 'first',
      fieldExtendList:{},
      entryFieldExtendList:{},
      dialogVisible: false,
      fileList:[],
      processList:[],
      equipmentList:[]
    };
  },
  created() {
    this.getList();
    this.getProcessList();
    this.getEquipmentList();
  },
  methods: {
    /** 查询工单列表 */
    getList() {
      this.loading = true;
      const  proStatusArr = this.queryParams.proStatusArr;
      this.queryParams.proStatus = proStatusArr.join(",");
      this.queryParams.params.processIds = this.queryParams.processArr.join(",");
      this.queryParams.params.equipmentIds = this.queryParams.equipmentArr.join(",");
      //获取生产工单主表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'workOrder'}).then(response => {
        this.fieldExtendList = response.rows;
        this.loading = false;
      });

      listWorkOrder(this.queryParams).then(response => {
        this.workOrderList=response.rows;
        console.log("this.workOrderList===>",this.workOrderList)
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        number: null,
        name: null,
        sourceType: null,
        sourceSystem: null,
        sourceInfo: null,
        batchNumber: null,
        materialId: null,
        materialNumber: null,
        materialName: null,
        materialUnitId: null,
        materialUnitName: null,
        quantity: null,
        beginProDate: null,
        planFinishDate: null,
        realFinishDate: null,
        proStatus: null,
        proStatusArr:['A','B'],
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        status: null
      };
      this.workOrderEntryList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({ path: "/mes/workOrder-add/index" });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      let id =null;
      if(row==null){
        id =this.ids[0];
      }else{
        id =row;
      }
        this.$router.push({ path: "/mes/workOrder-edit/index/"+id });

    },
    /** 报工质检按钮 */
    reportQualityClick(workOrderEntryId){

      this.$router.push({ path: "/mes/report-add/index/"+workOrderEntryId });

    },
    reportQualityViewClick(){
      let workOrder =  this.workOrderList.filter(item => item.id ==this.ids[0]);
      this.$router.push({ name: "ReportQuality" , params: {workOrderNumber: workOrder[0].number}});

    },
    queryReportClick(){
      let workOrder =  this.workOrderList.filter(item => item.id ==this.ids[0]);
      this.$router.push({ name: "Report" , params: {workOrderNumber: workOrder[0].number}});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let ids =[];
      if(row == null){
        ids =this.ids;
      }else{
        ids.push(row.id);
      }
      //增加校验，只有生产状态为E的才可以进行删除
      for (let i = 0; i < ids.length; i++) {
        let workOrder =  this.workOrderList.filter(item => item.id ==ids[i]);
        console.log("workOrder===>",workOrder)
        if(workOrder[0].proStatus !="E"){
          this.$modal.msgWarning("只有关闭过的工单才可以进行删除操作!请重新勾选!")
          return;
        }
      }
      this.$modal.confirm('该操作会彻底删除所选工单(包括该工单的下的报工单/工资单/质检单等数据),仍要删除吗？').then(function() {
        return delWorkOrder(ids);
      }).then(response => {
        this.getList();
        this.$modal.msgSuccess("删除成功"+response.data+"条");
      }).catch(() => {});
    },

    proStatusUpdate(status){
      const ids =this.ids;
      let params ={}
      params.ids =ids.join(",");
      params.proStatus = status;
      //开始排产
      if(status==='B'){
        //只有生产状态为A或者C的工单才能开始排产
        for (let i = 0; i < ids.length; i++) {
          let workOrder =  this.workOrderList.filter(item => item.id ==ids[i]);
          if(!(workOrder[0].proStatus =="A"||workOrder[0].proStatus =="C")){
            this.$modal.msgWarning("只有待排产或已暂停的工单才可以进行排产操作!请重新勾选!")
            return;
          }
        }
        editProStatus(params).then(response => {
          this.$modal.msgSuccess(response.msg);
          this.handleQuery();
        });
      }
      //工单暂停
      if(status==='C'){
        //只有生产状态为B的工单才能暂停
        for (let i = 0; i < ids.length; i++) {
          let workOrder =  this.workOrderList.filter(item => item.id ==ids[i]);
          if(workOrder[0].proStatus !="B"){
            this.$modal.msgWarning("只有排产中的工单才可以进行暂停操作!请重新勾选!")
            return;
          }
        }
        editProStatus(params).then(response => {
          this.$modal.msgSuccess(response.msg);
          this.handleQuery();
        });
      }
      //强制关闭
      if(status==='E'){
        //只有生产状态为不为E的工单才能关闭
        for (let i = 0; i < ids.length; i++) {
          let workOrder =  this.workOrderList.filter(item => item.id ==ids[i]);
          if(workOrder[0].proStatus ==="E"){
            this.$modal.msgWarning("已关闭的工单不能再次关闭!请重新勾选!")
            return;
          }
        }
        this.$modal.confirm('强制关闭操作会令所选工单(包括该工单的下的报工单/工资单/质检单等数据)失效,仍要强制关闭吗?').then(function() {
          return editProStatus(params);
        }).then(response =>  {
          this.$modal.msgSuccess(response.msg);
          this.handleQuery();
        }).catch(() => {
          return;
        });
      }
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('production/workOrder/export', {
        ...this.queryParams
      }, `workOrder_${new Date().getTime()}.xlsx`)
    },
    onRowClick(row, event, column) {
      this.$refs.pickEntryList.clearFilter();
      this.$refs.reportEntryList.clearFilter();
      let entryList =  row.workOrderEntryList;
      let pickEntryList =[]
      let reportEntryList =[]
        for (let j = 0; j < entryList.length; j++) {
          let entry = entryList[j];
          if (entry.type === 'pick') {
            pickEntryList.push(entry);
          }
          if (entry.type === 'report') {
            reportEntryList.push(entry)
          }
        }
      this.pickEntryList=pickEntryList;
      this.reportEntryList = reportEntryList;
      //扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'process-9'}).then(response => {
        this.entryFieldExtendList = response.rows;
      });
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    fileDownload(row) {
      let annex = row.annex;
      this.fileList=annex.split(",");
      this.dialogVisible=true
    },
    fieldFormat(row,item){
      return fieldFormat(row,item);
    },
    dictFormat(item){
      return dictFormat(item);
    },
    getProcessList(){
      listProcess({pageSize:99999}).then(res =>{
        this.processList = res.rows;
      })
    },
    getEquipmentList(){
      listEquipment({pageSize:99999}).then(res =>{
        this.equipmentList = res.rows;
      })
    },
    customSort(type){
      const id =this.ids[0];
      console.log("ids====>",id);
      let updateList = [];
      let workOrder ={};
      let currentIndex =-1;
      for (let i = 0; i < this.workOrderList.length; i++) {
        if(this.workOrderList[i].id ==id){
          workOrder =this.workOrderList[i]
          currentIndex =i ;
        }
      }
      //当选选中行序号
      let customSort =workOrder.customSort;
      console.log("this.queryParams.params.equipmentIds===>",this.queryParams.params.equipmentIds)
      if(this.queryParams.params.equipmentIds==null||this.queryParams.params.equipmentIds==''){
        this.$modal.msgWarning("只能通过设备进行自定义排序")
        return;
      }
      //上移
      if(type==='up'){
        if(currentIndex==-1||currentIndex==0){
          this.$modal.msgWarning("已经无法上移")
          return;
        }

        //传递参数，交换序号
        workOrder.customSort=customSort-1;
        updateList.push(workOrder);
        let workOrderB= this.workOrderList[currentIndex-1];
        workOrderB.customSort=customSort;
        updateList.push(workOrderB);
      }
      //下移
      if(type==='down'){
        if(currentIndex==this.workOrderList.length-1){
          this.$modal.msgWarning("已经无法下移")
          return;
        }
        //传递参数，交换序号
        workOrder.customSort=customSort+1;
        updateList.push(workOrder);
        let workOrderB= this.workOrderList[currentIndex+1];
        workOrderB.customSort=customSort;
        updateList.push(workOrderB);
      }
      updateCustomSort(updateList).then(res =>{
        this.handleQuery();
      })

    }
  }
};
</script>
<style lang="scss">
.tableBox {
  th {
    padding: 0 !important;
    height: 20px;
    line-height: 20px;
  }
  td {
    padding: 0 !important;
    height: 15px;
    line-height: 15px;
  }
}
</style>
