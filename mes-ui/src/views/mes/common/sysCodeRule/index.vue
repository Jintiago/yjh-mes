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
      <el-form-item label="基础元素" prop="basicDomain">
        <el-select v-model="queryParams.basicDomain" placeholder="请选择基础元素" clearable>
          <el-option
            v-for="dict in dict.type.basic_domain"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['common:sysCodeRule:add']"
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
          v-hasPermi="['common:sysCodeRule:edit']"
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
          v-hasPermi="['common:sysCodeRule:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sysCodeRuleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="规则名称" align="center" prop="name" />
      <el-table-column label="规则编号" align="center" prop="number" />
      <el-table-column label="基础元素" align="center" prop="basicDomain">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.basic_domain" :value="scope.row.basicDomain"/>
        </template>
      </el-table-column>
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['common:sysCodeRule:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['common:sysCodeRule:remove']"
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

    <!-- 添加或修改编码规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="规则名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="规则编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="基础元素" prop="basicDomain">
              <el-select v-model="form.basicDomain" placeholder="请选择基础元素">
                <el-option
                  v-for="dict in dict.type.basic_domain"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="center">编码规则子信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysCodeRuleEntry">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysCodeRuleEntry">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="form.sysCodeRuleEntryList"
                  @selection-change="handleSysCodeRuleEntrySelectionChange"
                  :row-class-name="rowSysCodeRuleEntryIndex"
                  ref="sysCodeRuleEntry">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50" >
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;">
                <el-input v-model="scope.row.index"  :disabled="true"/>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="类型"  width="130">
            <template slot-scope="scope">
              <el-form-item :rules="rules.typeId"
                            label-width="0"
                            :key="Math.random()"
                            :prop="'sysCodeRuleEntryList.'+scope.$index+'.typeId'"
                            style="margin-bottom: 12px;">
                <el-select v-model="scope.row.typeId" placeholder="请选择类型">
                  <el-option
                    v-for="dict in dict.type.code_rule_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="流水长度"  width="80">
            <template slot-scope="scope">
              <el-form-item label-width="0"
                            style="margin-bottom: 12px;"
                            :rules="rules.lengthFlow"
                            :prop="'sysCodeRuleEntryList.'+scope.$index+'.lengthFlow'"
                            :key="Math.random()" >
                <el-input v-model="scope.row.lengthFlow"   />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="当前流水号"  width="100">
            <template slot-scope="scope">
              <el-form-item label-width="0"
                            style="margin-bottom: 12px;"
                            :rules="rules.lengthFlow"
                            :prop="'sysCodeRuleEntryList.'+scope.$index+'.maxFlow'"
                            :key="Math.random()" >
                <el-input v-model="scope.row.maxFlow"  placeholder="将获取的流水号" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="补位符"  width="160">
            <template slot-scope="scope">
              <el-form-item label-width="0"
                            style="margin-bottom: 12px;">
                <el-input v-model="scope.row.codeCover"  placeholder="根据流水长度补位" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="日期格式"  width="150">
            <template slot-scope="scope">
              <el-form-item label-width="0" style="margin-bottom: 12px;"
                            :prop="'sysCodeRuleEntryList.'+scope.$index+'.dateFormat'">
                <el-select v-model="scope.row.dateFormat" placeholder="请选择">
                  <el-option
                    v-for="dict in dict.type.date_format"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="常量" prop="constantChar" width="150">
            <template slot-scope="scope">
              <el-form-item label-width="0" style="margin-bottom: 12px;">
              <el-input v-model="scope.row.constantChar" placeholder="请输入常量" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="元素键值" prop="elementSourceTable" width="150">
            <template slot-scope="scope">
              <el-form-item label-width="0" style="margin-bottom: 12px;">
              <el-select v-model="scope.row.elementSourceTable" placeholder="请选择">
                <el-option
                  v-for="dict in dict.type.basic_domain"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="元素属性" prop="sourceAttribute" width="150">
            <template slot-scope="scope">
              <el-form-item label-width="0" style="margin-bottom: 12px;">
              <el-input v-model="scope.row.sourceAttribute" placeholder="输入" />
              </el-form-item>
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
<script >
import { listSysCodeRule, getSysCodeRule, delSysCodeRule, addSysCodeRule, updateSysCodeRule } from "@/api/mes/common/sysCodeRule";
export default {
  name: "SysCodeRule",
  dicts: ['basic_domain', 'code_rule_type', 'sys_normal_disable','date_format'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSysCodeRuleEntry: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 编码规则表格数据
      sysCodeRuleList: [],
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
        basicDomain: null,
        status: '0',
      },
      // 表单参数
      form: {
        basicDomain:null,
        // 编码规则子表格数据
        sysCodeRuleEntryList:[],
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        typeId:[
          { required: true, message: "类型不能为空", trigger: "blur" }
        ],
        basicDomain:[
          { required: true, message: "元素不能为空", trigger: "change" }
        ],
        lengthFlow:[{
          required: false,
          pattern:/^\d+(\.\d+)?$/g,
          message: '请输入大于等于0的数字',
          trigger: "input"
        }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询编码规则列表 */
    getList() {
      this.loading = true;
      listSysCodeRule(this.queryParams).then(response => {
        this.sysCodeRuleList = response.rows;
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
        remark: null,
        number: null,
        name: null,
        basicDomain: null,
        status: '0'
      };
      this.form.sysCodeRuleEntryList = [];
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
    /** 编码规则子序号 */
    rowSysCodeRuleEntryIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加编码规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSysCodeRule(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改编码规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      console.log("this.form.sysCodeRuleEntryList===>",this.form.sysCodeRuleEntryList)
      this.$refs["form"].validate(valid => {
        if (valid) {
          let list = this.form.sysCodeRuleEntryList;
          if(list.length<1){
            this.$modal.msgWarning("请录入明细");
            return ;
          }
          for (let i = 0; i < this.form.sysCodeRuleEntryList.length; i++) {
            this.form.sysCodeRuleEntryList[i].sort = this.form.sysCodeRuleEntryList[i].index;
          }
          if (this.form.id != null) {
            updateSysCodeRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSysCodeRule(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除编码规则编号为"' + ids + '"的数据项？').then(function() {
        return delSysCodeRule(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 编码规则子添加按钮操作 */
    handleAddSysCodeRuleEntry() {
      let obj = {};
      obj.sort = "";
      obj.typeId = "";
      obj.lengthFlow = 0;
      obj.maxFlow = 0;
      obj.dateFormat = "";
      obj.constantChar = "";
      obj.elementSourceTable = "";
      obj.sourceAttribute = "";
      obj.sourceValue = ""
      obj.codeCover="";
      this.form.sysCodeRuleEntryList.push(obj);
      this.$forceUpdate();
    },
    /** 编码规则子删除按钮操作 */
    handleDeleteSysCodeRuleEntry() {
      if (this.checkedSysCodeRuleEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的编码规则子数据");
      } else {
        const sysCodeRuleEntryList = this.form.sysCodeRuleEntryList;
        const checkedSysCodeRuleEntry = this.checkedSysCodeRuleEntry;
        this.form.sysCodeRuleEntryList = sysCodeRuleEntryList.filter(function(item) {
          return checkedSysCodeRuleEntry.indexOf(item.index) === -1
        });
        this.$forceUpdate();
      }
    },
    /** 复选框选中数据 */
    handleSysCodeRuleEntrySelectionChange(selection) {
      this.checkedSysCodeRuleEntry = selection.map(item => item.index)
    },

  }
};
</script>
