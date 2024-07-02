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
      <el-form-item label="工序路线名称" prop="name" label-width="100px">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入工序路线名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['production:route:add']"
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
          v-hasPermi="['production:route:edit']"
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
          v-hasPermi="['production:route:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['production:route:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="routeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="number" />
      <el-table-column label="工序路线名称" align="center" prop="name" />
      <el-table-column label="工序路线说明" align="center" prop="describes" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
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
            v-hasPermi="['production:route:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['production:route:remove']"
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

    <!-- 添加或修改工序路线对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="750px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules"  class="jz-compact-form" >
        <el-row  :gutter="30" style="margin-bottom: 7px;">
          <el-col :span="14">
            <el-form-item label="工序路线名称" prop="name"     >
              <el-input v-model="form.name" placeholder="请输入工序路线名称" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="编号" prop="number"   >
              <el-input v-model="form.number" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="工序路线说明" prop="describes">
          <el-input v-model="form.describes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-row :gutter="30" type="flex" align="bottom">
          <el-col :span="14">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="10" >
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="center">工序明细</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddProRouteProcess">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteProRouteProcess">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="sortUp">上移</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="sortDown">下移</el-button>
          </el-col>
        </el-row>
        <el-table :data="form.routeProcessList"
                  @selection-change="handleProRouteProcessSelectionChange" ref="proRouteProcess">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="排序" prop="sort" width="50">
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;">
                <el-input v-model="scope.row.sort"  :disabled="true"/>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="工序"  width="200" >
            <template slot-scope="scope">
              <el-form-item :prop="'routeProcessList.'+scope.$index+'.processId'" :rules="rules.processId"
                            label-width="0"  style="margin-bottom: 12px;"
                            :key="Math.random()">
                <el-select v-model="scope.row.processId" filterable placeholder="请选择" @change="selectProcess($event,scope.row)">
                  <el-option
                    v-for="item in processList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
<!--          <el-table-column label="与下道工序关系" prop="nextRule" width="200">-->
<!--            <template slot-scope="scope">-->
<!--              <el-form-item label-width="0"  style="margin-bottom: 12px;">-->
<!--                <el-input v-model="scope.row.nextRule"  />-->
<!--              </el-form-item>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="状态" prop="status" width="150">-->
<!--            <template slot-scope="scope">-->
<!--              <el-form-item label-width="0"  style="margin-bottom: 12px;">-->
<!--                <el-select v-model="scope.row.status" >-->
<!--                  <el-option label="请选择" value="" />-->
<!--                </el-select>-->
<!--              </el-form-item>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="质检类型" prop="status" >
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;">
                <el-select v-model="scope.row.qcType" >
                  <el-option
                    v-for="dict in dict.type.qc_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                     />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="首检数量" >
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;" :rules="rules.firstQcQuantity"
                            :prop="'routeProcessList.'+scope.$index+'.firstQcQuantity'">
                <el-input v-model="scope.row.firstQcQuantity"   />
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

<script>
import { listRoute, getRoute, delRoute, addRoute, updateRoute } from "@/api/mes/production/route";
import { listProcess } from "@/api/mes/production/process";
export default {
  name: "Route",
  dicts: ['sys_normal_disable','qc_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedProRouteProcess: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 工序路线表格数据
      routeList: [],
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
        describes: null,
        status: '0',
      },
      // 表单参数
      form: {
        // 工序路线明细表格数据
        routeProcessList: [],
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "工序路线名称不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        processId: [
          {
            required: true,
            message: '工序不能为空',
            trigger: 'blur',
          },
        ],
        firstQcQuantity:[{
            required: true,
            pattern:/^\d+(\.\d+)?$/g,
            message: '请输入大于等于0的数字',
            trigger: "blur"
        }],
      },
      processList :[],
      maxIndex: 0,
    };
  },
  created() {
    this.getList();
    this.getProcessList();
  },
  methods: {
    /** 查询工序路线列表 */
    getList() {
      this.loading = true;
      listRoute(this.queryParams).then(response => {
        this.routeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.maxIndex=0;
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        number: null,
        name: null,
        describes: null,
        status: "0",
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.form.routeProcessList = [];
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
      this.title = "添加工序路线";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRoute(id).then(response => {
        this.form = response.data;
        this.form.routeProcessList = response.data.routeProcessList;
        this.open = true;
        this.title = "修改工序路线";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          if(this.form.routeProcessList.length<1){
            this.$modal.msgWarning("请录入明细");
            return ;
          }

          if (this.form.id != null) {
            updateRoute(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRoute(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
          this.maxIndex=0;
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除工序路线编号为"' + ids + '"的数据项？').then(function() {
        return delRoute(ids);
      }).then(response => {
        this.getList();
        this.$modal.msgSuccess("删除成功"+response.data+"条");
      }).catch(() => {});
    },

    /** 工序路线明细添加按钮操作 */
    handleAddProRouteProcess() {
      //编辑状态获取当前最大的sort为maxIndex
      if(this.form.id != null){
        let tmp =0;
        for (let i = 0; i < this.form.routeProcessList.length; i++) {
          if(this.form.routeProcessList[i].sort>tmp){
            tmp=this.form.routeProcessList[i].sort;
          }
        }
        this.maxIndex =tmp;
      }
      this.maxIndex = this.maxIndex+1
      let obj = {};
      obj.processId = "";
      obj.processName = "";
      obj.sort = this.maxIndex;
      obj.nextRule = "";
      obj.status = "";
      obj.remark = "";
      obj.qcType="A";
      obj.firstQcQuantity=0;
      this.form.routeProcessList.push(obj);
      this.$refs.proRouteProcess.sort("sort","ascending")
    },
    /** 工序路线明细删除按钮操作 */
    handleDeleteProRouteProcess() {

      if (this.checkedProRouteProcess.length == 0) {
        this.$modal.msgError("请先选择要删除的工序路线明细数据");
      } else {
        let routeProcessList = this.form.routeProcessList;
        let checkedProRouteProcess = this.checkedProRouteProcess;

        this.form.routeProcessList = routeProcessList.filter(function(item) {
          return checkedProRouteProcess.indexOf(item.sort) == -1
        });
        for (let i = 0; i <this.form.routeProcessList.length; i++) {
          this.form.routeProcessList[i].sort=i+1
        }
        this.maxIndex=this.form.routeProcessList.length;
        this.$refs.proRouteProcess.sort("sort","ascending")
        this.$refs.proRouteProcess.clearSelection();
        this.checkedProRouteProcess=[]
        this.$forceUpdate();
        }

    },
    /** 复选框选中数据 */
    handleProRouteProcessSelectionChange(selection) {
      this.checkedProRouteProcess = selection.map(item => item.sort)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('production/route/export', {
        ...this.queryParams
      }, `route_${new Date().getTime()}.xlsx`)
    },
    /** 查询工序列表 */
    getProcessList() {
      listProcess({pageNum: 1,pageSize: 999999 ,status: '0'} ).then(response => {
        this.processList = response.rows;
        console.log("this.processList",this.processList)
      });
    },
    sortUp(){
      let checkedProRouteProcess = this.checkedProRouteProcess;
      if(checkedProRouteProcess.length>1||checkedProRouteProcess.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }

      if(checkedProRouteProcess[0]===1){
        this.$modal.msgError("已经在最上面啦");
        return;
      }
      let routeProcessList = this.form.routeProcessList;

      for (let i = 0; i <routeProcessList.length; i++) {
        let tmpSort =routeProcessList[i].sort;
        let tmp ={};
        if(tmpSort===checkedProRouteProcess[0]){
          //与上一个交换顺序
          tmp =this.form.routeProcessList[i-1];
          routeProcessList[i-1]=routeProcessList[i]
          routeProcessList[i-1].sort =tmpSort-1
          routeProcessList[i]=tmp;
          routeProcessList[i].sort=tmpSort;
        }
      }
      this.form.routeProcessList=routeProcessList;
      this.$refs.proRouteProcess.sort("sort","ascending")
      this.checkedProRouteProcess[0]=checkedProRouteProcess[0]-1;
    },
    sortDown(){
      let checkedProRouteProcess = this.checkedProRouteProcess;
      if(checkedProRouteProcess.length>1||checkedProRouteProcess.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }
      let routeProcessList = this.form.routeProcessList;
      console.log("this.checkedProRouteProcess========>",this.checkedProRouteProcess)
      if(checkedProRouteProcess[0]===routeProcessList.length){
        this.$modal.msgError("已经在最下面啦");
        return;
      }
      for (let i = 0; i <routeProcessList.length; i++) {
        let tmpSort =routeProcessList[i].sort;
        let tmp ={};
        if(tmpSort===checkedProRouteProcess[0]){
          //与下一个交换顺序
          tmp =this.form.routeProcessList[i+1];
          routeProcessList[i+1]=routeProcessList[i]
          routeProcessList[i+1].sort =tmpSort+1
          routeProcessList[i]=tmp;
          routeProcessList[i].sort=tmpSort;
        }
      }
      this.form.routeProcessList=routeProcessList;
      this.$refs.proRouteProcess.sort("sort","ascending")
      this.checkedProRouteProcess[0]=checkedProRouteProcess[0]+1;
    },
    selectProcess(value,row){
      let currentRow = row;
      let res = this.processList.filter((item) => {
        return item.id ===  value;
      });
      currentRow.processName = res[0].name;
      this.$refs["form"].validate();
    },
  }
};
</script>

