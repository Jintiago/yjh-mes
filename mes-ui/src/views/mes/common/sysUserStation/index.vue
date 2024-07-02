<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工位" prop="stationId">
        <el-select v-model="queryParams.stationId" clearable placeholder="请选择工位">
          <el-option
            v-for="item in stationOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="人员名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入人员名称"
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
          v-hasPermi="['common:sysUserStation:add']"
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
          v-hasPermi="['common:sysUserStation:edit']"
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
          v-hasPermi="['common:sysUserStation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['common:sysUserStation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sysUserStationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="车间" align="center" prop="workshopName" />
      <el-table-column label="工位" align="center" prop="stationName" />
      <el-table-column label="用户名" align="center" prop="userName" />
      <el-table-column label="昵称" align="center" prop="nickName" />
      <el-table-column label="操作人" align="center" prop="updateBy" />
      <el-table-column label="操作时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['common:sysUserStation:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['common:sysUserStation:remove']"
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

    <!-- 添加或修改用户工位绑定对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车间">
          <el-row>
            <el-col :span="14">
              <el-input v-model="form.workshopName" :disabled="true" ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="工位" prop="stationId">
          <el-select v-model="form.stationId"  placeholder="请选择工位" @change="stationChange($event)">
            <el-option
              v-for="item in stationOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名">
          <el-row>
            <el-col :span="14">
              <el-input v-model="form.userName" :disabled="true" ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="用户昵称" prop="userId">
          <el-select v-model="form.userId"  placeholder="请选择人员" @change="userChange($event)">
            <el-option
              v-for="item in userOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            ></el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSysUserStation, getSysUserStation, delSysUserStation, addSysUserStation, updateSysUserStation } from "@/api/mes/common/sysUserStation";
import { listUser } from "@/api/system/user";
import { listStation } from "@/api/mes/masterdata/station";
export default {
  name: "SysUserStation",
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
      // 用户工位绑定表格数据
      sysUserStationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        userName: null,
        stationId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户不能为空", trigger: "blur" }
        ],
        stationId: [
          { required: true, message: "工位不能为空", trigger: "blur" }
        ],
      },
      stationOptions:[],
      userOptions:[],
    };
  },
  created() {
    this.getList();
    this.getUserList();
    this.getStationList();
  },
  methods: {
    /** 查询用户工位绑定列表 */
    getList() {
      this.loading = true;
      listSysUserStation(this.queryParams).then(response => {
        this.sysUserStationList = response.rows;
        console.log("this.sysUserStationList===>",this.sysUserStationList)
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
        userId: null,
        userName: null,
        stationId: null,
        stationName: null,
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
      this.title = "添加用户工位绑定";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSysUserStation(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户工位绑定";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSysUserStation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSysUserStation(this.form).then(response => {
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
      const id = row.id || this.ids;
      this.$modal.confirm('是否确认删除该绑定关系').then(function() {
        return delSysUserStation(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('common/sysUserStation/export', {
        ...this.queryParams
      }, `sysUserStation_${new Date().getTime()}.xlsx`)
    },
    getStationList() {
      listStation({pageSize:99999}).then(response => {
        this.stationOptions = response.rows;
      });
    },
    getUserList(){
      listUser({pageSize: 99999}).then(response => {
        this.userOptions = response.rows;
      })
    },
    userChange(value){
      let opt= {};
      opt= this.userOptions.find((item)=>{
        return item.userId === value;
      });
      this.form.nickName = opt.nickName;
      this.form.userName = opt.userName;
    },
    stationChange(value){
      let opt= {};
      opt= this.stationOptions.find((item)=>{
        return item.id === value;
      });
      this.form.stationName =opt.name;
      this.form.workshopName =opt.workshopName;
    },


  },

};
</script>
