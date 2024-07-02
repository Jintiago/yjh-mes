<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['integration:integrateDataSource:add']"
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
          v-hasPermi="['integration:integrateDataSource:edit']"
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
          v-hasPermi="['integration:integrateDataSource:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['integration:integrateDataSource:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="integrateDataSourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="number" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="源单名称" align="center" prop="sourceBillName" />
      <el-table-column label="源单内码" align="center" prop="sourceBillCode" />
      <el-table-column label="集成方式" align="center" prop="integrationMode" />
      <el-table-column label="请求地址" align="center" prop="requestAddress" />
      <el-table-column label="用户名" align="center" prop="userName" />
      <el-table-column label="密码" align="center" prop="password" />
      <el-table-column label="账套码" align="center" prop="bookCode" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['integration:integrateDataSource:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['integration:integrateDataSource:remove']"
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

    <!-- 添加或修改集成数据源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="集成方式" prop="integrationMode">
              <el-select v-model="form.integrationMode" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.integration_mode"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求地址" prop="requestAddress">
              <el-input v-model="form.requestAddress" placeholder="请输入请求地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入密码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="令牌" prop="token">
              <el-input v-model="form.token" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密钥" prop="secretKey">
              <el-input v-model="form.secretKey" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="账套码" prop="bookCode">
              <el-input v-model="form.bookCode" placeholder="请输入账套码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="AppID" prop="appId">
              <el-input v-model="form.appId" placeholder="请输入AppID" />
            </el-form-item>
          </el-col>

        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="源单名称" prop="sourceBillName">
              <el-input v-model="form.sourceBillName" placeholder="请输入源单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="源单内码" prop="sourceBillCode">
              <el-input v-model="form.sourceBillCode" placeholder="请输入源单内码" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="集成厂商" prop="manufacturer">
              <el-select v-model="form.manufacturer" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.into_manufacturer"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="集成系统版本" prop="systemVersion">
              <el-input v-model="form.systemVersion" placeholder="请输入集成系统版本" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="请求参数1" prop="parameter1">
              <el-input v-model="form.parameter1" placeholder="请输入请求参数1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求参数2" prop="parameter2">
              <el-input v-model="form.parameter2" placeholder="请输入请求参数2" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="请求参数3" prop="parameter3">
              <el-input v-model="form.parameter3" placeholder="请输入请求参数3" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求参数4" prop="parameter4">
              <el-input v-model="form.parameter4" placeholder="请输入请求参数4" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="备注" prop="remark">
                <el-input v-model="form.remark" placeholder="请输入备注" />
              </el-form-item>
            </el-col>
            <el-col :span="4" >
              <el-button style="margin-left: 20px"  type="primary" @click="clickTestConnection">测试链接</el-button>
            </el-col>
          </el-row>


        </el-row>
        <el-divider content-position="center">数据源字段信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddInteDataSourceField">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteInteDataSourceField">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="inteDataSourceFieldList" :row-class-name="rowInteDataSourceFieldIndex" @selection-change="handleInteDataSourceFieldSelectionChange" ref="inteDataSourceField">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="名称" prop="name" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.name" placeholder="请输入名称" />
            </template>
          </el-table-column>
          <el-table-column label="字段" prop="field" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.field" placeholder="请输入字段" />
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="请输入备注" />
            </template>
          </el-table-column>
          <el-table-column label="是否隐藏" prop="status" >
            <template slot-scope="scope">
              <el-select v-model="scope.row.status" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listIntegrateDataSource, getIntegrateDataSource, delIntegrateDataSource, addIntegrateDataSource, updateIntegrateDataSource,testConnection } from "@/api/mes/integration/integrateDataSource";

export default {
  name: "IntegrateDataSource",
  dicts: ['integration_mode','sys_yes_no','into_manufacturer'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedInteDataSourceField: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 集成数据源表格数据
      integrateDataSourceList: [],
      // 数据源字段表格数据
      inteDataSourceFieldList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        number: null,
        name: null,
        ipAddress: null,
        integrationMode: null,
        appId: null,
        token: null,
        secretKey: null,
        bookCode: null,
        userName: null,
        password: null,
        sourceBillName: null,
        sourceBillCode: null,
        parameter9: null,
        parameter8: null,
        parameter7: null,
        parameter6: null,
        parameter5: null,
        parameter4: null,
        parameter3: null,
        parameter2: null,
        parameter1: null,
        requestAddress: null,
        requestMethod: null,
        manufacturer: null,
        systemName: null,
        systemVersion: null,
        databaseType: null,
        sqlSentence: null,
        javaClass: null,
        javaFunction: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        userName: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询集成数据源列表 */
    getList() {
      this.loading = true;
      listIntegrateDataSource(this.queryParams).then(response => {
        this.integrateDataSourceList = response.rows;
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
        ipAddress: null,
        integrationMode: null,
        appId: null,
        token: null,
        secretKey: null,
        bookCode: null,
        userName: null,
        password: null,
        sourceBillName: null,
        sourceBillCode: null,
        parameter9: null,
        parameter8: null,
        parameter7: null,
        parameter6: null,
        parameter5: null,
        parameter4: null,
        parameter3: null,
        parameter2: null,
        parameter1: null,
        requestAddress: null,
        requestMethod: null,
        manufacturer: null,
        systemName: null,
        systemVersion: null,
        databaseType: null,
        sqlSentence: null,
        javaClass: null,
        javaFunction: null,
        status: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.inteDataSourceFieldList = [];
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
      this.title = "添加集成数据源";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getIntegrateDataSource(id).then(response => {
        this.form = response.data;
        this.inteDataSourceFieldList = response.data.inteDataSourceFieldList;
        this.open = true;
        this.title = "修改集成数据源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.inteDataSourceFieldList = this.inteDataSourceFieldList;
          if (this.form.id != null) {
            updateIntegrateDataSource(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addIntegrateDataSource(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除集成数据源编号为"' + ids + '"的数据项？').then(function() {
        return delIntegrateDataSource(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 数据源字段序号 */
    rowInteDataSourceFieldIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
      row.sort=row.index;
    },
    /** 数据源字段添加按钮操作 */
    handleAddInteDataSourceField() {
      let obj = {};
      obj.sort = "";
      obj.name = "";
      obj.field = "";
      obj.remark = "";
      obj.status = "";
      this.inteDataSourceFieldList.push(obj);
    },
    /** 数据源字段删除按钮操作 */
    handleDeleteInteDataSourceField() {
      if (this.checkedInteDataSourceField.length == 0) {
        this.$modal.msgError("请先选择要删除的数据源字段数据");
      } else {
        const inteDataSourceFieldList = this.inteDataSourceFieldList;
        const checkedInteDataSourceField = this.checkedInteDataSourceField;
        this.inteDataSourceFieldList = inteDataSourceFieldList.filter(function(item) {
          return checkedInteDataSourceField.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleInteDataSourceFieldSelectionChange(selection) {
      this.checkedInteDataSourceField = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('integration/integrateDataSource/export', {
        ...this.queryParams
      }, `integrateDataSource_${new Date().getTime()}.xlsx`)
    },
    clickTestConnection(){
      testConnection(this.form).then(res=>{
        this.$modal.msgSuccess("连接成功");
      })

    }
  }
};
</script>
