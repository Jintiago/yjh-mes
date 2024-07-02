<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="人员名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入人员名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工资单日期"  label-width="120px">
        <el-date-picker
          v-model="daterangePayrollDate"
          style="width: 240px"
          unlink-panels
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['finance:payroll:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['finance:payroll:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['finance:payroll:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['finance:payroll:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payrollList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工资单日期" align="center" prop="payrollDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payrollDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="人员" align="center" prop="userName" />
      <el-table-column label="应发工资" align="center" prop="shouldPay" />
      <el-table-column label="实发工资" align="center" prop="realPay" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bill_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['finance:payroll:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['finance:payroll:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 添加或修改工资单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-form-item label="工资单编号" prop="number" >
          <el-input v-model="form.number" placeholder="不输入则由系统自动生成" :disabled="isDisabled"/>
        </el-form-item>

        <el-form-item label="报工单编号" prop="reportNumbers">
          <el-input v-model="form.reportNumbers"  placeholder="例: REP1001,REP1002"/>
        </el-form-item>

        <el-form-item label="工资单日期" prop="payrollDate">
          <el-date-picker clearable
                          v-model="form.payrollDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择工资单日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="人员" prop="userName">
          <el-select v-model="form.userId" filterable placeholder="请选择人员" @change="userChange">
            <el-option
              v-for="item in userList"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            >
            </el-option>
          </el-select>

        </el-form-item>

        <el-form-item label="应发工资" prop="shouldPay">
          <el-input v-model="form.shouldPay" placeholder="请输入应发工资" />
        </el-form-item>

        <el-form-item label="实发工资" prop="realPay">
          <el-input v-model="form.realPay" placeholder="请输入实发工资" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status"   >
            <el-option
              v-for="dict in dict.type.bill_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
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
import { listPayroll, getPayroll, delPayroll, addPayroll, updatePayroll,payRollPreview } from "@/api/mes/finance/payroll";
import { listUser } from '@/api/system/user'

export default {
  dicts: ['bill_status'],
  name: "Payroll",
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
      // 工资单表格数据
      payrollList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangePayrollDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        userName: null,
        payrollDate: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "人员ID不能为空", trigger: "change" }
        ],
        userName: [
          { required: true, message: "人员名称不能为空", trigger: "blur" }
        ],
        reportIds: [
          { required: true, message: "报工单IDs不能为空", trigger: "blur" }
        ],
        shouldPay: [
          { required: true, message: "应发工资不能为空", trigger: "blur" }
        ],
        realPay: [
          { required: true, message: "实发工资不能为空", trigger: "blur" }
        ],
        payrollDate: [
          { required: true, message: "工资单日期不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      },
      pickerOptions: {
        shortcuts: [
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
            text: "上月",
            onClick(picker) {
              const now = new Date();
              // 获取当前月的第一天
              const start = new Date(now.getFullYear(), now.getMonth()-1, 1);
              const end = new Date(now.getFullYear(), now.getMonth(), 0);
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
      isDisabled:false,
      userList:[]

    };
  },
  created() {
    this.getList();
    this.getUserList();
  },
  methods: {
    /** 查询工资单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangePayrollDate && '' != this.daterangePayrollDate) {
        this.queryParams.params["beginPayrollDate"] = this.daterangePayrollDate[0];
        this.queryParams.params["endPayrollDate"] = this.daterangePayrollDate[1];
      }
      listPayroll(this.queryParams).then(response => {
        this.payrollList = response.rows;
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
        userId: null,
        userName: null,
        reportIds: null,
        shouldPay: null,
        realPay: null,
        wageType: null,
        planInfo: null,
        payrollDate: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        status: 'A',
        remark: null
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
      this.daterangePayrollDate = [];
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
      this.isDisabled=false;
      this.reset();
      this.open = true;
      this.title = "添加工资单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.isDisabled=true;
      this.reset();
      const id = row.id || this.ids
      getPayroll(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工资单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePayroll(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPayroll(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除工资单编号为"' + ids + '"的数据项？').then(function() {
        return delPayroll(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/payroll/export', {
        ...this.queryParams
      }, `payroll_${new Date().getTime()}.xlsx`)
    },
    userChange(value){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      this.form.userName =opt.nickName;
    },
    /** 获取人员数据*/
    getUserList(){
      listUser({pageSize : 99999,status:'0'}).then(response => {
          this.userList = response.rows;
        }
      );
    },
  }
};
</script>
