<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="质检单号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入质检编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="报工单号" prop="reportNumber">
        <el-input
          v-model="queryParams.reportNumber"
          placeholder="请输入报工单编号"
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
      <el-form-item label="工序" prop="processName">
        <el-input
          v-model="queryParams.processName"
          placeholder="请输入工序"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品编码" prop="materialNumber">
        <el-input
          v-model="queryParams.materialNumber"
          placeholder="请输入产品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入产品名称"
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
      <el-form-item label="质检类型" prop="qcType">
        <el-select v-model="queryParams.qcType" placeholder="请选择质检类型" clearable>
          <el-option
            v-for="dict in dict.type.qc_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="报工时间" prop="reportTime">
        <el-date-picker clearable
          v-model="queryParams.reportTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择报工时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="检测时间" prop="checkTime">
        <el-date-picker clearable
          v-model="queryParams.checkTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择检测时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="质检员" prop="checkUserName">
        <el-input
          v-model="queryParams.checkUserName"
          placeholder="请输入质检员名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          size="mini"
          @click="getReport()"
        >上查工单</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['quality:reportQuality:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['quality:reportQuality:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['quality:reportQuality:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportQualityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="质检单号" align="center" prop="number" />-->
      <el-table-column label="报工单号" align="center" prop="reportNumber" />
      <el-table-column label="工单号" align="center" prop="workOrderNumber" width="100"/>
      <el-table-column label="工序" align="center" prop="processName" />
<!--      <el-table-column label="产品编码" align="center" prop="materialNumber" />-->
      <el-table-column label="产品名称" align="center" prop="materialName" />
      <el-table-column label="规格型号" align="center" prop="specification" width="150"/>
      <el-table-column label="单位" align="center" prop="materialUnitName" />
<!--      <el-table-column label="批次号" align="center" prop="batchNumber" />-->
      <el-table-column label="质检类型" align="center" prop="qcType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.qc_type" :value="scope.row.qcType"/>
        </template>
      </el-table-column>
      <el-table-column label="检测数量" align="center" prop="quantity" />
      <el-table-column label="合格数" align="center" prop="qualifiedQuantity" />
      <el-table-column label="不合格数" align="center" prop="unqualifiedQuantity" />
      <el-table-column label="报工时间" align="center" prop="reportTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reportTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检测时间" align="center" prop="checkTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="质检员" align="center" prop="checkUserName" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="qualityAddClick(scope.row)"
            v-hasPermi="['production:report:edit']"
          >详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <reportQualityForm ref="reportQualityForm"  > </reportQualityForm>
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
import { listReportQuality, getReportQuality, delReportQuality, addReportQuality, updateReportQuality } from "@/api/mes/quality/reportQuality";
import reportQualityForm from '@/views/mes/quality/reportQuality/form'
export default {
  name: "ReportQuality",
  dicts: ['qc_type', 'sys_normal_disable'],
  components: { reportQualityForm },
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
      // 报工质检单表格数据
      reportQualityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        number: null,
        reportId: null,
        reportNumber: null,
        workOrderNumber: null,
        processName: null,
        materialNumber: null,
        materialName: null,
        specification: null,
        materialUnitName: null,
        batchNumber: null,
        qcType: null,
        quantity: null,
        qualifiedQuantity: null,
        unqualifiedQuantity: null,
        reportTime: null,
        checkTime: null,
        checkUserId: null,
        checkUserName: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        reportId: [
          { required: true, message: "报工单ID不能为空", trigger: "blur" }
        ],
        workOrderNumber: [
          { required: true, message: "工单编号不能为空", trigger: "blur" }
        ],
        processName: [
          { required: true, message: "工序不能为空", trigger: "blur" }
        ],
        materialNumber: [
          { required: true, message: "产品编码不能为空", trigger: "blur" }
        ],
        materialName: [
          { required: true, message: "产品名称不能为空", trigger: "blur" }
        ],
        materialUnitName: [
          { required: true, message: "单位不能为空", trigger: "blur" }
        ],
        qcType: [
          { required: true, message: "质检类型不能为空", trigger: "change" }
        ],
        quantity: [
          { required: true, message: "检测数量不能为空", trigger: "blur" }
        ],
        qualifiedQuantity: [
          { required: true, message: "合格数量不能为空", trigger: "blur" }
        ],
        unqualifiedQuantity: [
          { required: true, message: "不合格数量不能为空", trigger: "blur" }
        ],
        reportTime: [
          { required: true, message: "报工时间不能为空", trigger: "blur" }
        ],
        checkTime: [
          { required: true, message: "检测时间不能为空", trigger: "blur" }
        ],
        checkUserId: [
          { required: true, message: "质检员ID不能为空", trigger: "blur" }
        ],
        checkUserName: [
          { required: true, message: "质检员名称不能为空", trigger: "blur" }
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
  activated(){
    let workOrderNumber = this.$route.params.workOrderNumber;
    if(typeof (workOrderNumber)!="undefined"){
      this.queryParams.workOrderNumber=workOrderNumber
    }
    this.getList();
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询报工质检单列表 */
    getList() {
      this.loading = true;
      listReportQuality(this.queryParams).then(response => {
        this.reportQualityList = response.rows;
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
        reportId: null,
        reportNumber: null,
        workOrderNumber: null,
        processName: null,
        materialNumber: null,
        materialName: null,
        specification: null,
        materialUnitName: null,
        batchNumber: null,
        qcType: null,
        quantity: null,
        qualifiedQuantity: null,
        unqualifiedQuantity: null,
        reportTime: null,
        checkTime: null,
        checkUserId: null,
        checkUserName: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        status: null
      };
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
      this.reset();
      this.open = true;
      this.title = "添加报工质检单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getReportQuality(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改报工质检单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReportQuality(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReportQuality(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除报工质检单编号为"' + ids + '"的数据项？').then(function() {
        return delReportQuality(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('quality/reportQuality/export', {
        ...this.queryParams
      }, `reportQuality_${new Date().getTime()}.xlsx`)
    },
    //点击质检单新增按钮
    qualityAddClick(row){
      this.$refs.reportQualityForm.qualityInfo(row.id);
    },
    //上查工单
    getReport(){
      //进行页面跳转
      let reportQuality =  this.reportQualityList.filter(item => item.id ===this.ids[0]);
      console.log("reportQuality===>",reportQuality)
      this.$router.push({ name: "Report" , params: {reportNumber: reportQuality[0].reportNumber}});
    }
  }
};
</script>
