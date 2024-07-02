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
      <el-form-item label="设备类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择设备类型" clearable>
          <el-option
            v-for="dict in dict.type.equipment_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工位ID" prop="stationId">
        <el-input
          v-model="queryParams.stationId"
          placeholder="请输入工位ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备状态" prop="equipmentStatus">
        <el-select v-model="queryParams.equipmentStatus" placeholder="请选择设备状态" clearable>
          <el-option
            v-for="dict in dict.type.equipment_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="启用状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择启用状态" clearable>
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
          v-hasPermi="['equipment:equipment:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

      <el-col :span="6" v-for="item in equipmentList">
        <el-card class="box-card" :style="{'background-color': item.equipmentStatus=='A'?'#909399': item.equipmentStatus=='B'?'#E6A23C': item.equipmentStatus=='C'?'#67C23A':item.equipmentStatus=='D'?'#F56C6C':'#909399'}">
          <el-row>
            <el-col :span="4">
              <el-image
                style="width: 50px; height: 150px"
                :src="require('@/assets/images/'+item.image+'.jpg')"
                >
              </el-image>
            </el-col>
            <el-col :span="20" style="padding-left: 8px">
              <el-row style="margin-bottom: 4px;">
                <el-col :span="20">
                  设备：{{ item.name }}
                </el-col>
                <el-col :span="4">
                  <el-button  plain round size="small" @click="handleUpdate(item)">修改</el-button>
                </el-col>

              </el-row>
              <el-row style="margin-bottom: 4px">
                <el-col :span="18">设备编号：{{ item.number }}</el-col>


                <el-col :span="6">
                  <el-button  type="primary" plain round size="small" >OEE数据</el-button>
                </el-col>
              </el-row>
              <el-row style="margin-bottom: 4px">
                <el-col :span="8">
                  状态：
                </el-col>
                <el-col :span="16">
                  <dict-tag size="mini" :options="dict.type.equipment_status" :value="item.equipmentStatus"/>
                </el-col>

              </el-row>
              <el-row style="margin-bottom: 4px">
                型号：{{ item.specification }}
              </el-row>
              <el-row style="margin-bottom: 4px">
                品牌：{{ item.brand }}
              </el-row>
              <el-row style="margin-bottom: 4px">
                <el-col :span="20">
                  所在工位：{{item.stationName}}
                </el-col>
                <el-col :span="4">
                  <el-button type="danger" round size="small" @click="handleDelete(item)">删除</el-button>
                </el-col>
              </el-row>
            </el-col>
          </el-row>


        </el-card>
      </el-col>

<!--    <el-table v-loading="loading" :data="equipmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="品牌" align="center" prop="brand" />
      <el-table-column label="型号" align="center" prop="specification" />
      <el-table-column label="设备类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.equipment_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="工位" align="center" prop="stationName" />
      <el-table-column label="设备状态" align="center" prop="equipmentStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.equipment_status" :value="scope.row.equipmentStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="启用状态" align="center" prop="status">
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
            v-hasPermi="['equipment:equipment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['equipment:equipment:remove']"
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
    />-->

    <!-- 添加或修改设备信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="编号" prop="number">
          <el-input v-model="form.number" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="型号" prop="specification">
          <el-input v-model="form.specification" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="设备类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择设备类型">
            <el-option
              v-for="dict in dict.type.equipment_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="物联序列号" prop="iotSn">
          <el-input v-model="form.iotSn" placeholder="请输入物联序列号" />
        </el-form-item>
        <el-form-item label="工位ID" prop="stationId">
          <el-input v-model="form.stationId" placeholder="请输入工位ID" />
        </el-form-item>
        <el-form-item label="设备状态" prop="equipmentStatus">
          <el-select v-model="form.equipmentStatus" placeholder="请选择设备状态">
            <el-option
              v-for="dict in dict.type.equipment_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="异常预警时间" prop="abnormalWarn">
          <el-input v-model="form.abnormalWarn" placeholder="请输入异常预警时间" />
        </el-form-item>
        <el-form-item label="启用状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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
  </div>
</template>

<script>
import { listEquipment, getEquipment, delEquipment, addEquipment, updateEquipment } from "@/api/mes/equipment/equipment";

export default {
  name: "Equipment",
  dicts: ['equipment_status', 'equipment_type', 'sys_normal_disable'],
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
      // 设备信息表格数据
      equipmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 99999,
        number: null,
        name: null,
        type: null,
        stationId: null,
        equipmentStatus: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        equipmentStatus: [
          { required: true, message: "设备状态不能为空", trigger: "change" }
        ],
        abnormalWarn: [
          { required: true, message: "异常预警时间不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "启用状态不能为空", trigger: "change" }
        ],
      },
      timer:null
    };
  },
  created() {
    this.getList();
    let _this =this;
    this.timer = setInterval(()=>{
      //需要做的事情
      _this.getList();
    },5000);
  },
  methods: {
    /** 查询设备信息列表 */
    getList() {
      this.loading = true;
      listEquipment(this.queryParams).then(response => {
        this.equipmentList = response.rows;
        for (let i = 0; i < this.equipmentList.length; i++) {
          let s = this.equipmentList[i].equipmentStatus;
          if(s=='A'){
            this.equipmentList[i].image="weikaiji";
          }
          if(s=='B'){
            this.equipmentList[i].image="daiji";
          }
          if(s=='C'){
            this.equipmentList[i].image="yunxing";
          }
          if(s=='D'){
            this.equipmentList[i].image="guzhang";
          }
        }
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
        brand: null,
        specification: null,
        type: null,
        stationId: null,
        stationName: null,
        equipmentStatus: null,
        checkPlanId: null,
        abnormalWarn: null,
        status: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.title = "添加设备信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEquipment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEquipment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEquipment(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备:'+row.name+'？').then(function() {
        return delEquipment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('equipment/equipment/export', {
        ...this.queryParams
      }, `equipment_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style>
  .box-card{
    margin: 10px
  }

</style>
