<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属车间" prop="workshopId">
        <el-select v-model="queryParams.workshopId" placeholder="请选择车间" clearable>
          <el-option
            v-for="item in workshopOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="工位名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入工位名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工位编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入工位编号"
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
          v-hasPermi="['masterdata:station:add']"
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
          v-hasPermi="['masterdata:station:edit']"
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
          v-hasPermi="['masterdata:station:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['masterdata:station:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="所属车间" align="center" prop="workshopName" />
      <el-table-column label="工位名称" align="center" prop="name" />
      <el-table-column label="工位编号" align="center" prop="number" />
      <el-table-column label="绑定工序" width="230" align="center" prop="processIds"  >
        <template slot-scope="scope">
          {{processFormat(scope.row.processIds)}}
        </template>
      </el-table-column>
      <el-table-column label="绑定设备" width="230" align="center" prop="machineIds"  >
        <template slot-scope="scope">
          {{equipmentFormat(scope.row.machineIds)}}
        </template>
      </el-table-column>
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
            v-hasPermi="['masterdata:station:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['masterdata:station:remove']"
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

    <!-- 添加或修改工位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工位编号" prop="number">
          <el-input v-model="form.number" placeholder="请输入工位编号" />
        </el-form-item>
        <el-form-item label="工位名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入工位名称" />
        </el-form-item>
        <el-form-item label="所属车间" prop="workshopId">
          <el-select v-model="form.workshopId" placeholder="请选择车间" @change="workshopSelectChange" style="width:350px">
            <el-option
              v-for="item in workshopOptions"
              :key="item.id"
              :label="item.name"
              :value= "item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="绑定工序" prop="processId">
          <el-select v-model="form.processIds" placeholder="请选择工序" multiple style="width:350px">
            <el-option
              v-for="item in processOptions"
              :key="item.id"
              :label="item.name"
              :value= "item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="绑定设备" prop="machineIds">
          <el-select v-model="form.machineIds" placeholder="请选择设备" multiple style="width:350px">
            <el-option
              v-for="item in equipmentOptions"
              :key="item.id"
              :label="item.name"
              :value= "item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
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
import { listStation, getStation, delStation, addStation, updateStation } from "@/api/mes/masterdata/station";
import { listWorkshop } from "@/api/mes/masterdata/workshop";
import { listProcess } from "@/api/mes/production/process";
import { selectDictLabel } from '@/utils/jinzhong'
import { listEquipment } from "@/api/mes/equipment/equipment";
export default {
  name: "Station",
  dicts: ['sys_normal_disable'],
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
      // 工位表格数据
      stationList: [],
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
        workshopId: null,
        workshopName: null,
        processIds: null,
        machineIds: null,
        status: '0',
      },
      // 表单参数
      form: {
        processIds: null,
      },
      // 表单校验
      rules: {
        number: [
          { required: true, message: "工位编号不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "工位名称不能为空", trigger: "blur" }
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
      },
      //车间选项
      workshopOptions:[],
      processOptions:[],
      equipmentOptions :[],
    };
  },
  created() {
    this.getWorkshops();
    this.getProcess();
    this.getEquipmentList();
    this.getList();
  },
  methods: {
    /** 查询工位列表 */
    getList() {
      this.loading = true;
      listStation(this.queryParams).then(response => {
        this.stationList = response.rows;
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
        workshopId: null,
        workshopName: null,
        processIds: null,
        machineIds: null,
        status: "0",
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
      this.title = "添加工位";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStation(id).then(response => {
        let data = response.data
        //多选数据处理
        let idsStr = data.processIds
        if(idsStr != null&&idsStr!=''){
          data.processIds= idsStr.split(',').map(parseFloat);
        }
        let idsStrB = data.machineIds
        if(idsStrB != null&&idsStrB!=''){
          data.machineIds= idsStrB.split(',').map(parseFloat);
        }
        this.form = data;
        this.open = true;
        this.title = "修改工位";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let tmp = this.form.processIds;
          if(tmp instanceof Array){
            if(tmp.length>0){
              this.form.processIds = tmp.join(",");
            }else {
              this.form.processIds='';
            }
          }else{
            this.form.processIds='';
          }
          let tmpB = this.form.machineIds;
          if(tmpB instanceof Array){
            if(tmpB.length>0){
              this.form.machineIds = tmpB.join(",");
            }else {
              this.form.machineIds='';
            }
          }else{
            this.form.machineIds='';
          }

          if (this.form.id != null) {
            updateStation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(() =>{
              this.open = false;
            });
          } else {
            addStation(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除工位编号为"' + ids + '"的数据项？').then(function() {
        return delStation(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('masterdata/station/export', {
        ...this.queryParams
      }, `station_${new Date().getTime()}.xlsx`)
    },
    //查询车间信息
    getWorkshops(){
      listWorkshop({pageSize: 99999,status: "0"}).then( response => {
        this.workshopOptions = response.rows;
      });
    },
    workshopSelectChange(value){
      //获取下拉框中label的值
      let obj = {};
      obj = this.workshopOptions.find((item)=>{
        return item.id === value;
      });
      this.form.workshopName = obj.name;
    },
    getProcess(){
      listProcess({pageNum: 1,pageSize: 999999 ,status: '0'}).then(response => {
        this.processOptions = response.rows;
      })
    },
    processFormat(ids){
      if(ids ===null ||ids === undefined) {
        return null;
      }
      if(ids instanceof Array){
        if(ids.length<1){
          return null;
        }
      }
      let idsArr = ids.split(",");
      let result ='';
      for (let i = 0; i < idsArr.length; i++) {
        result+=selectDictLabel(this.processOptions, idsArr[i])
          if(i!=idsArr.length-1){
            result+=",";
          }
      }
      return result;
    },
    getEquipmentList(){
      listEquipment({pageSize: 99999,status:'0'}).then( res =>{
        this.equipmentOptions = res.rows;
      })
    },
    equipmentFormat(ids){
      if(ids ===null ||ids === undefined) {
        return null;
      }
      if(ids instanceof Array){
        if(ids.length<1){
          return null;
        }
      }
      let idsArr = ids.split(",");
      let result ='';
      for (let i = 0; i < idsArr.length; i++) {
        result+=selectDictLabel(this.equipmentOptions, idsArr[i])
        if(i!=idsArr.length-1){
          result+=",";
        }
      }
      return result;
    },
  }
};
</script>
