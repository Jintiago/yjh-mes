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
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input
          v-model="queryParams.equipmentName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备类型" prop="equipmentType">
        <el-select v-model="queryParams.equipmentType" placeholder="请选择设备类型" clearable>
          <el-option
            v-for="dict in dict.type.equipment_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="报修时间">
        <el-date-picker
          v-model="daterangeReportRepairTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="处理结果" prop="repairResult">
        <el-select v-model="queryParams.repairResult" placeholder="请选择处理结果" clearable>
          <el-option
            v-for="dict in dict.type.repair_result"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="维修人" prop="repairUserName">
        <el-input
          v-model="queryParams.repairUserName"
          placeholder="请输入维修人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单据状态" prop="statusArr">
        <el-select v-model="queryParams.statusArr" multiple placeholder="请选择处理结果" clearable>
          <el-option
            v-for="dict in dict.type.repair_order_status"
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
          v-hasPermi="['equipment:repairOrder:add']"
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
          v-hasPermi="['equipment:repairOrder:edit']"
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
          v-hasPermi="['equipment:repairOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['equipment:repairOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="repairOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="number" />
      <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="规格型号" align="center" prop="equipmentSpecification" />
      <el-table-column label="设备类型" align="center" prop="equipmentType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.equipment_type" :value="scope.row.equipmentType"/>
        </template>
      </el-table-column>
      <el-table-column label="报修时间" align="center" prop="reportRepairTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reportRepairTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="维修完成时间" align="center" prop="finishTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.finishTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="维修人" align="center" prop="repairUserName" />
      <el-table-column label="验收日期" align="center" prop="confirmTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.confirmTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单据状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.repair_order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="处理结果" align="center" prop="repairResult">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.repair_result" :value="scope.row.repairResult"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['equipment:repairOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['equipment:repairOrder:remove']"
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

    <!-- 添加或修改设备维修单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备" prop="equipmentId">
              <el-select v-model="form.equipmentId" placeholder="请选择" @change="equipmentSelect">
                <el-option
                  v-for="item in equipmentList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="equipmentType">
              <el-select v-model="form.equipmentType"   :disabled="true">
              <el-option
                v-for="dict in dict.type.equipment_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span=  "12">
            <el-form-item label="品牌" prop="equipmentBrand">
              <el-input v-model="form.equipmentBrand"  :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格型号" prop="equipmentSpecification">
              <el-input v-model="form.equipmentSpecification"  :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报修时间" prop="reportRepairTime">
              <el-date-picker clearable
                              v-model="form.reportRepairTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择报修时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单据状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择">
                <el-option
                  v-for="dict in dict.type.repair_order_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="维修完成时间" prop="finishTime">
              <el-date-picker clearable
                              v-model="form.finishTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择维修完成时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修人" prop="repairUserId">
              <el-select v-model="form.repairUserId" clearable filterable  placeholder="请选择" @change="repairUserChange">
                <el-option
                  v-for="item in userList"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12"  >
            <el-form-item label="处理结果" prop="repairResult">
              <el-select v-model="form.repairResult" placeholder="请选择处理结果">
                <el-option
                  v-for="dict in dict.type.repair_result"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="验收日期" prop="confirmTime">
              <el-date-picker clearable
                              v-model="form.confirmTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择验收日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收人" prop="confirmUserId">
              <el-select v-model="form.confirmUserId" clearable filterable  placeholder="请选择" @change="confirmUserChange">
                <el-option
                  v-for="item in userList"
                  :key="item.userId"
                  :label="item.nickName"
                  :value="item.userId"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-divider content-position="center">设备维修单明细信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddRepairOrderEntry">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteRepairOrderEntry">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="repairOrderEntryList" :row-class-name="rowRepairOrderEntryIndex" @selection-change="handleRepairOrderEntrySelectionChange" ref="repairOrderEntry">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="项目" prop="itemId" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.itemId" @change="itemSelectChange($event,scope.row)" placeholder="请选择">
                <el-option
                  v-for="item in inspectionItemList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="项目编码" prop="itemNumber" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemNumber"  :disabled="true"/>
            </template>
          </el-table-column>
          <el-table-column label="项目类型" prop="itemType" width="80">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.inspection_item_type" :value="scope.row.itemType" style="margin-left: 12px"/>
            </template>
          </el-table-column>
          <el-table-column label="故障描述" prop="remark" >
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="请输入故障描述" />
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
import { listRepairOrder, getRepairOrder, delRepairOrder, addRepairOrder, updateRepairOrder } from "@/api/mes/equipment/repairOrder";
import { listEquipment } from "@/api/mes/equipment/equipment";
import { listInspectionItem } from "@/api/mes/equipment/inspectionItem";
import { getStation } from '@/api/mes/masterdata/station'
import { listUser } from '@/api/system/user'
export default {
  name: "RepairOrder",
  dicts: ['repair_result', 'inspection_item_type','repair_order_status','equipment_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedRepairOrderEntry: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 设备维修单表格数据
      repairOrderList: [],
      // 设备维修单明细表格数据
      repairOrderEntryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 更新时间时间范围
      daterangeReportRepairTime: [],
      // 更新时间时间范围
      daterangeFinishTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        number: null,
        name: null,
        equipmentName: null,
        equipmentType: null,
        reportRepairTime: null,
        repairResult: null,
        repairUserName: null,
        statusArr: ['B','C','D'],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [],
        equipmentId: [
          { required: true, message: "设备ID不能为空", trigger: "blur" }
        ],
        equipmentName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "单据状态不能为空", trigger: "change" }
        ],
      },
      equipmentList:[],
      inspectionItemList:[],
      userList: [],
    };
  },
  created() {
    this.getList();
    this.getUserList();
    this.getEquipmentList();
    this.getInspectionItemList()
  },
  methods: {
    /** 查询设备维修单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeReportRepairTime && '' != this.daterangeReportRepairTime) {
        this.queryParams.params["beginReportRepairTime"] = this.daterangeReportRepairTime[0];
        this.queryParams.params["endReportRepairTime"] = this.daterangeReportRepairTime[1];
      }
      if (null != this.daterangeFinishTime && '' != this.daterangeFinishTime) {
        this.queryParams.params["beginFinishTime"] = this.daterangeFinishTime[0];
        this.queryParams.params["endFinishTime"] = this.daterangeFinishTime[1];
      }
      this.queryParams.status = this.queryParams.statusArr.join(",");
      listRepairOrder(this.queryParams).then(response => {
        this.repairOrderList = response.rows;
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
        equipmentId: null,
        equipmentNumber: null,
        equipmentName: null,
        equipmentBrand: null,
        equipmentSpecification: null,
        equipmentType: null,
        reportRepairTime: new Date().format("yyyy-MM-dd HH:mm:ss"),
        finishTime: null,
        repairResult: null,
        repairUserId: null,
        repairUserName: null,
        confirmUserId: null,
        confirmUserName: null,
        confirmTime: null,
        status: 'A',
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.repairOrderEntryList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeReportRepairTime = [];
      this.daterangeFinishTime = [];
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
      this.title = "添加设备维修单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRepairOrder(id).then(response => {
        this.form = response.data;
        this.repairOrderEntryList = response.data.repairOrderEntryList;
        this.open = true;
        this.title = "修改设备维修单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.repairOrderEntryList = this.repairOrderEntryList;
          if (this.form.id != null) {
            updateRepairOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRepairOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除设备维修单编号为"' + ids + '"的数据项？').then(function() {
        return delRepairOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 设备维修单明细序号 */
    rowRepairOrderEntryIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 设备维修单明细添加按钮操作 */
    handleAddRepairOrderEntry() {
      let obj = {};
      obj.itemId = "";
      obj.itemNumber = "";
      obj.itemName = "";
      obj.itemType = "";
      obj.itemContent = "";
      obj.itemStandard = "";
      obj.remark = "";
      this.repairOrderEntryList.push(obj);
    },
    /** 设备维修单明细删除按钮操作 */
    handleDeleteRepairOrderEntry() {
      if (this.checkedRepairOrderEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的设备维修单明细数据");
      } else {
        const repairOrderEntryList = this.repairOrderEntryList;
        const checkedRepairOrderEntry = this.checkedRepairOrderEntry;
        this.repairOrderEntryList = repairOrderEntryList.filter(function(item) {
          return checkedRepairOrderEntry.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleRepairOrderEntrySelectionChange(selection) {
      this.checkedRepairOrderEntry = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('equipment/repairOrder/export', {
        ...this.queryParams
      }, `repairOrder_${new Date().getTime()}.xlsx`)
    },
    getEquipmentList(){
      listEquipment({pageSize: 99999}).then(res=>{
        this.equipmentList =res.rows;
      })
    },
    getInspectionItemList(){
      listInspectionItem({pageSize: 99999}).then(res=>{
        this.inspectionItemList =res.rows;
      })
    },
    equipmentSelect(id){
      let list = [];
      list = this.equipmentList.filter(function(item) {
        return item.id === id
      });
      this.form.equipmentNumber=list[0].number;
      this.form.equipmentName=list[0].name;
      this.form.equipmentBrand=list[0].brand;
      this.form.equipmentSpecification=list[0].specification;
      this.form.equipmentType=list[0].type;
    },
    itemSelectChange(value,row){
      //获取下拉框中label的值
      let currentRow = row;
      let res = this.inspectionItemList.find((item)=>{
        return item.id === value;
      });
      currentRow.itemType = res.type;
      currentRow.itemNumber = res.number;
      currentRow.itemName = res.name;
    },
    repairUserChange(value){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      this.form.repairUserName =opt.nickName;
    },
    confirmUserChange(value){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      this.form.confirmUserName =opt.nickName;
    },
    /** 获取人员数据*/
    getUserList(){
      let qp =   {pageSize : 99999} ;
      listUser(this.addDateRange(qp, [])).then(response => {
          this.userList = response.rows;
        }
      );
    },
  }
};
</script>
