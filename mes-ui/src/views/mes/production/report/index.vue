<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="报工单号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="报工单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工序名称" prop="processName">
          <el-input
            v-model="queryParams.processName"
            placeholder="请输入工序名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="工单号" prop="workOrderNumber">
          <el-input
            v-model="queryParams.workOrderNumber"
            placeholder="请输入工单编号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
<!--        <el-form-item label="工单名称" prop="workOrderName">-->
<!--          <el-input-->
<!--            v-model="queryParams.workOrderName"-->
<!--            placeholder="请输入工单名称"-->
<!--            clearable-->
<!--            @keyup.enter.native="handleQuery"-->
<!--          />-->
<!--        </el-form-item>-->

      <el-form-item label="产品名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报工人" prop="reportUserName">
        <el-input
          v-model="queryParams.reportUserName"
          placeholder="请输入报工人名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
        <el-form-item label="报工途径" prop="reportChannel">
          <el-select v-model="queryParams.reportChannel"  clearable>
            <el-option
              v-for="dict in dict.type.report_channel"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
      <el-form-item>
        <el-form-item label="报工日期"  style="margin-left: 10px" >
          <el-date-picker
            v-model="queryParams.reportTimeQuery"
            type="daterange"
            unlink-panels
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          size="mini"-->
<!--          v-hasPermi="['production:report:add']"-->
<!--        >上查任务单</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['production:report:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          size="mini"
          :disabled="single"
          v-hasPermi="['production:report:edit']"
          @click="qualityAddClick"
        >抽检</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['production:report:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          v-hasPermi="['production:report:edit']"-->
<!--          @click="payRollPreview"-->
<!--        >生成工资单</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['production:report:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" ></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportList" @selection-change="handleSelectionChange" class="tableBox" height="610px">
      <el-table-column type="selection" width="55" align="center" />
      <div v-for="(item,index) in entryFieldExtendList" >
        <el-table-column :label="item.fieldName" align="center"  :width="item.width" show-overflow-tooltip='true' >
          <template slot-scope="scope">
            <!--  动态属性        -->
            <span v-if="item.type==='dateTime'">
                  {{ parseTime(fieldFormat(scope.row,item), '{y}-{m}-{d} {h}:{i}:{s}') }}
            </span>
            <span v-else>{{fieldFormat(scope.row,item)}}</span>
          </template>
        </el-table-column>
      </div>
<!--      <el-table-column label="车间" align="center" prop="workshopName" />-->
<!--      <el-table-column label="工资结算" align="center" prop="isSettle">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isSettle"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="报工途径" align="center" prop="reportChannel" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="110">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="qualityQueryClick(scope.row)"
            v-hasPermi="['production:report:edit']"
            v-show="scope.row.qualityStatus==='C'||scope.row.qualityStatus==='D'"
          >质检单</el-button>
          <el-button
            size="mini"
            type="text"
            @click="payrollClick(scope.row)"
            v-hasPermi="['finance:payroll:edit']"
            v-show="false"
          >工资单</el-button>
        </template>
      </el-table-column>
    </el-table>
    <payrollForm ref="payrollForm"  > </payrollForm>
    <reportQualityForm ref="reportQualityForm"  > </reportQualityForm>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-dialog
      title="工资单预览"
      :visible.sync="centerDialogVisible"
      :close-on-click-modal="false"
      width="70%"
      center>
      <el-form ref="payRollPreviewForm" :model="payRollPreviewForm" :rules="rules" label-width="120">
        <el-table  :data="payRollPreviewForm.list"  >
          <el-table-column label="人员" align="center"  >
            <template slot-scope="scope">
              <el-form-item       style="margin-bottom: 12px;">
                <el-input v-model="scope.row.userName" :disabled="true"/>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="工资单日期" align="center"  >
            <template slot-scope="scope">
              <el-form-item       style="margin-bottom: 12px;" >
                <el-date-picker clearable
                                v-model="scope.row.payrollDate"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择工资单日期"
                >
                </el-date-picker>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="计价方案" align="center"  >
            <template slot-scope="scope">
              <el-form-item       style="margin-bottom: 12px;">
                <el-input v-model="scope.row.planInfo" type="textarea" :disabled="true" :rows="1"/>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="应发工资" align="center"  >
            <template slot-scope="scope">
              <el-form-item       style="margin-bottom: 12px;">
                <el-input v-model="scope.row.shouldPay" :disabled="true"/>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="实发工资" align="center"  >
            <template slot-scope="scope">
              <el-form-item       style="margin-bottom: 12px;">
                <el-input v-model="scope.row.realPay" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="报工单" align="center"  >
            <template slot-scope="scope">
              <el-form-item       style="margin-bottom: 12px;">
                <el-input v-model="scope.row.reportNumbers" type="textarea" :disabled="true" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center"  >
            <template slot-scope="scope">
              <el-form-item       style="margin-bottom: 12px;">
                <el-input v-model="scope.row.remark" type="textarea" placeholder="请输入内容" />
              </el-form-item>
            </template>
          </el-table-column>
        </el-table>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="payrollSubmit">确认生成</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { listReport, getReport, delReport, addReport, updateReport } from "@/api/mes/production/report";
import Treeselect from '@riophae/vue-treeselect'
import MaterialSelect from '@/components/mes/materialSelect'
import reportQualityForm from '@/views/mes/quality/reportQuality/form'
import payrollForm from '@/views/mes/finance/payroll/form'
import {dictFormat, fieldFormat} from "@/utils/jinzhong";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import {
  listPayroll,
  getPayroll,
  delPayroll,
  addPayroll,
  updatePayroll,
  payRollPreview,
  batchAdd
} from '@/api/mes/finance/payroll'

export default {
  name: "Report",
  dicts: ['sys_yes_no','report_channel'],
  components: { reportQualityForm,payrollForm },
  data() {
    return {
      payRollPreviewForm:{
        list:[]
      },
      centerDialogVisible:false,
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
      // 报工单表格数据
      reportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        processName: null,
        workOrderNumber: null,
        workOrderName: null,
        reportUserName: null,
        reportChannel: null,
        reportTimeQuery:[],
        params:{}
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      entryFieldExtendList:{},
      pickerOptions: {
        shortcuts: [
          {
            text: "本周",
            onClick(picker) {
              // 获取今天
              const end = new Date();
              // 获取当前周的第一天
              const start = new Date(
                end.getFullYear(),
                end.getMonth(),
                end.getDate() - ((end.getDay() + 6) % 7)
              );
              start.setTime(start.getTime());
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "本月",
            onClick(picker) {
              const end = new Date();
              // 获取当前月的第一天
              const start = new Date(end.getFullYear(), end.getMonth(), 1);
              start.setTime(start.getTime());
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "本年",
            onClick(picker) {
              const end = new Date();
              // 获取当前年的第一天
              const start = new Date(end.getFullYear(), 0, 1);
              start.setTime(start.getTime());
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "上一年",
            onClick(picker) {
              const now = new Date();
              // 获取当前年的第一天
              const start = new Date(now.getFullYear()-1, 0, 1);
              const end = new Date(now.getFullYear()-1, 11, 31);
              start.setTime(start.getTime());
              picker.$emit("pick", [start, end]);
            },
          },
        ]
      },
    };
  },
  activated(){
    let workOrderNumber = this.$route.params.workOrderNumber;
    if(typeof (workOrderNumber)!="undefined"){
      this.queryParams.reportTimeQuery=[]
      this.queryParams.pageNum=null;
      this.queryParams.pageSize=null;
      this.queryParams.workOrderNumber=workOrderNumber
    }
    let reportNumber = this.$route.params.reportNumber;
    if(typeof (reportNumber)!="undefined"){
      this.queryParams.reportTimeQuery=[]
      this.queryParams.pageNum=null;
      this.queryParams.pageSize=null;
      this.queryParams.number=reportNumber
    }
    this.getList();
  },

  created() {
    // 获取今天
    const end = new Date();
    // 获取当前周的第一天
    const start = new Date(
      end.getFullYear(),
      end.getMonth(),
      end.getDate() - ((end.getDay() + 6) % 7)
    );
    start.setTime(start.getTime());
    this.queryParams.reportTimeQuery[0]=start.format('yyyy-MM-dd')
    this.queryParams.reportTimeQuery[1]=end.format('yyyy-MM-dd')
    this.getFieldExtendList();
    this.getList();
  },
  methods: {
    /** 查询报工单列表 */
    getList() {
      this.loading = true;
      listReport(this.queryParams).then(response => {
        this.reportList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 表单重置
    reset() {
      this.form = {
        id: null,
        processId: null,
        processName: null,
        sort: null,
        workOrderEntryId: null,
        workOrderNumber: null,
        workOrderName: null,
        quantity: null,
        reportUserId: null,
        reportUserName: null,
        reportChannel: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
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
    //点击质检单详情按钮
    qualityQueryClick(row){
      this.$refs.reportQualityForm.qualityAddClick(row);
    },
    //点击质检单新增按钮
    qualityAddClick(){
      let id = this.ids[0];
      let list =this.reportList.filter(function(item) {
        return item.id === id;
      });

      if(list[0].qualityStatus!=='C'&&list[0].qualityStatus!=='D'){
        list[0].isShow=true;
        console.log("list[0].isShow===>",list[0].isShow)
      }else{
        list[0].isShow=false;
        console.log("list[0].isShow===>",list[0].isShow)
      }
      this.$refs.reportQualityForm.qualityAddClick(list[0]);

    },
    //点击工资单按钮
    payrollClick(row){
      this.$refs.payrollForm.clickInfo(row);
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除报工单编号为"' + ids + '"的数据项？').then(function() {
        return delReport(ids);
      }).then(response => {
        this.getList();
        this.$modal.msgSuccess("删除成功"+response.data+"条");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('production/report/export', {
        ...this.queryParams
      }, `report_${new Date().getTime()}.xlsx`)
    },
    // 工资单预览
    payRollPreview() {
      const ids =  this.ids;
      this.payRollPreviewForm.list=[];
      //调用工资单预览接口
      payRollPreview(ids).then(response => {
        this.payRollPreviewForm.list=response.data;
        this.centerDialogVisible=true;
      });
    },
    //工资单提交
    payrollSubmit(){
      batchAdd(this.payRollPreviewForm.list).then(response => {
        this.centerDialogVisible=false;
      })
    },
    fieldFormat(row,item){
      return fieldFormat(row,item);
    },
    dictFormat(item){
      return dictFormat(item);
    },
    getFieldExtendList(){
      //获取销售订单子表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'proReport'}).then(response => {
        this.entryFieldExtendList = response.rows;
        this.loading = false;
      });
    },

  }
};
</script>
<style lang="scss">
.tableBox {
  th {
    padding: 0 !important;
    height: 15px;
    line-height: 15px;
  }
  td {
    padding: 0 !important;
    height: 15px;
    line-height: 15px;
  }
}
</style>
