<!-- 标准工序的生产工单列表 -->
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
      <el-form-item label="批次号" prop="batchNumber">
        <el-input
          v-model="queryParams.batchNumber"
          placeholder="请输入批次号"
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['production:workOrder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
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
          :disabled="multiple"
          @click="proStatusUpdate('B')"
          v-hasPermi="['production:workOrder:edit']"
        >开始排产</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          :disabled="multiple"
          @click="proStatusUpdate('C')"
          v-hasPermi="['production:workOrder:edit']"
        >工单暂停</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          :disabled="multiple"
          @click="proStatusUpdate('E')"
          v-hasPermi="['production:workOrder:edit']"
        >强制关闭</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          :disabled="single"
          @click="queryReportClick()"
          v-hasPermi="['production:report:list']"
        >下查报工记录</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          size="mini"
          :disabled="single"
          @click="reportQualityViewClick()"
          v-hasPermi="['quality:reportQuality:list']"
        >下查质检记录</el-button>
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
    </el-row>

    <el-table v-loading="loading" :data="workOrderList" @selection-change="handleSelectionChange">

      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="expand" label="展开" >
        <template slot-scope="props">
          <el-table  :data="props.row.workOrderEntryList"   ref="workOrderEntryList"
                     style="margin-left: 30px;"
                     :default-sort = "{prop: 'sort'}" >
            <el-table-column label="排序" prop="sort"  width="70" align="center" />
            <el-table-column label="工序" prop="processName" width="160" align="center"/>
            <el-table-column label="已报工数量" prop="processName" width="160" align="center" />
            <el-table-column label="合格数" prop="processName" width="160" align="center" />
            <el-table-column label="不合格数" prop="processName" width="160" align="center"/>
            <el-table-column label="合格率" prop="processName" width="120" align="center">
              <template slot-scope="scope">
                <el-input v-model="scope.row.processName"  :disabled="true"/>
              </template>
            </el-table-column>
            <el-table-column label="车间" prop="workshopName" width="120" />
            <el-table-column label="工位" prop="stationName" width="120" />
            <el-table-column label="操作" >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-notebook-2"
                  @click="reportQualityClick(scope.row.id)"
                  v-hasPermi="['production:workOrder:edit']"
                >报工记录</el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="生产状态" align="center" prop="proStatus" >
        <template v-slot="scope">
          <dict-tag :options="dict.type.production_status" :value="scope.row.proStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="工单编号" align="center" prop="number" />
      <el-table-column label="产品编号" align="center" prop="materialNumber" />
      <el-table-column label="产品名称" align="center" prop="materialName" />
      <el-table-column label="批次号" align="center" prop="batchNumber" />
      <el-table-column label="单位" align="center" prop="materialUnitName" />
      <el-table-column label="生产数量" align="center" prop="quantity" />
      <el-table-column label="计划完成日期" align="center" prop="planFinishDate" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planFinishDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始生产日期" align="center" prop="beginProDate" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginProDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际完成日期" align="center" prop="realFinishDate" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.realFinishDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="是否返修" align="center" prop="repairFlag">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.repairFlag"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
  delWorkOrder, editProStatus,getWorkOrder
} from '@/api/mes/production/workOrder'


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
        status: null
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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询工单列表 */
    getList() {
      this.loading = true;
      const  proStatusArr = this.queryParams.proStatusArr;
      this.queryParams.proStatus = proStatusArr.join(",");
      listWorkOrder(this.queryParams).then(response => {
        this.workOrderList = response.rows;
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
      //增加校验，只有生产状态为A的才可以进行修改
      let workOrder = this.workOrderList.filter(item => item.id ==id);
      console.log("workOrder===>",workOrder)
      if(workOrder[0].proStatus ==="A"){
        this.$router.push({ path: "/mes/workOrder-edit/index/"+id });
      }else{
        this.$modal.msgWarning("只有待排产的工单才可以进行修改")
      }
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


  }
};
</script>
