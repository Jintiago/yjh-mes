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
      <el-form-item label="盘点时间">
        <el-date-picker
          v-model="daterangeTakeTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="是否期初库存" prop="initializeFlag" label-width="150" >
        <el-radio v-model="queryParams.initializeFlag" label="Y">是</el-radio>
        <el-radio v-model="queryParams.initializeFlag" label="N">否</el-radio>
      </el-form-item>
        <el-form-item label="库存校正标记" prop="correctFlag" label-width="150">
          <el-radio v-model="queryParams.correctFlag" label="Y" >已矫正</el-radio>
          <el-radio v-model="queryParams.correctFlag" label="N" >未校正</el-radio>
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
          v-hasPermi="['warehouse:takeInventory:add']"
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
          v-hasPermi="['warehouse:takeInventory:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          style="color: #6978B5;  background-color: #C7CEEB"
          icon="el-icon-finished"
          size="mini"
          :disabled="single"
          @click="kcjzClick"
        >库存校正</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['warehouse:takeInventory:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['warehouse:takeInventory:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="takeInventoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="编号" align="center" prop="number" />
      <el-table-column label="仓库" align="center" prop="warehouseName" />
      <el-table-column label="仓管" align="center" prop="keeperName" />
      <el-table-column label="盘点时间" align="center" prop="takeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.takeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="库存校正标记" align="center" prop="correctFlag" />
      <el-table-column label="期初库存" align="center" prop="initializeFlag" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['warehouse:takeInventory:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['warehouse:takeInventory:remove']"
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

    <!-- 添加或修改库存盘点对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1300px"  append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="盘点时间" prop="takeTime">
              <el-date-picker clearable
                              v-model="form.takeTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择盘点时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option
                  v-for="dict in dict.type.bill_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" clearable filterable  placeholder="请选择仓库" @change="warehouseSelect">
                <el-option
                  v-for="item in warehouseList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="仓管员" prop="keeperId">
              <el-select v-model="form.keeperId" clearable filterable  placeholder="请选择仓管员" @change="userChange">
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
<!--          <el-col :span="6">-->
<!--            <el-form-item label="审核人" prop="checker">-->
<!--              <el-input v-model="form.checker" placeholder="请输入审核人" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :span="6">-->
<!--            <el-form-item label="审核日期" prop="checkDate">-->
<!--              <el-date-picker clearable-->
<!--                              v-model="form.checkDate"-->
<!--                              type="date"-->
<!--                              value-format="yyyy-MM-dd"-->
<!--                              placeholder="请选择审核日期">-->
<!--              </el-date-picker>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="库存校正标记" prop="correctFlag" >
              <el-radio v-model="form.correctFlag" label="Y" :disabled="true">已矫正</el-radio>
              <el-radio v-model="form.correctFlag" label="N" :disabled="true">未校正</el-radio>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="是否期初库存" prop="initializeFlag">
              <el-radio v-model="form.initializeFlag" label="Y">是</el-radio>
              <el-radio v-model="form.initializeFlag" label="N">否</el-radio>
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="center">库存盘点明细信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddTakeInventoryEntry">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteTakeInventoryEntry">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="takeInventoryEntryList"  @selection-change="handleTakeInventoryEntrySelectionChange" ref="takeInventoryEntry"
                  class="tableBox"  height="500">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" prop="sort" width="50" >
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;">
                <el-input v-model="scope.row.sort"  :disabled="true" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="产品名"  width="240" prop="materialName">
            <template slot-scope="scope">
              <el-form-item    :rules="rules.materialName" label-width="0"  style="margin-bottom: 12px;">
                <el-input v-model="scope.row.materialName" placeholder="产品为必填项" >
                  <el-button slot="append" @click="handleSelectProduct(scope.row)" icon="el-icon-search" ></el-button>
                </el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="产品编号" prop="materialNumber" width="120" >
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;">
                <el-input v-model="scope.row.materialNumber" :disabled="true" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="规格型号" prop="specification" width="180">
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;">
                <el-input v-model="scope.row.specification"  />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="单位" prop="materialUnitName" width="70">
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;">
                <el-input v-model="scope.row.materialUnitName" :disabled="true" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="库存数量"  width="120" prop="inventoryQuantity">
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;"  >
                <el-input v-model="scope.row.inventoryQuantity"  @input="takeQuantityInput($event,scope.row)" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="盘点数量"  width="120" prop="takeQuantity">
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;" :rules="rules.quantity"
                            >
                <el-input v-model="scope.row.takeQuantity"  @input="takeQuantityInput($event,scope.row)" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="差异数量"  width="120" prop="differenceQuantity">
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;" :rules="rules.quantity" >
                <el-input v-model="scope.row.differenceQuantity"  :disabled="true" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="状态"   prop="status">
            <template slot-scope="scope">
          <el-form-item  label-width="0"  style="margin-bottom: 12px;">
            <el-select v-model="scope.row.status" placeholder="请选择状态" :disabled="true">
              <el-option
                v-for="dict in dict.type.take_inventory_entry_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <MaterialSelect ref="materialSelect" @onMaterialSelect="onMaterialSelect" > </MaterialSelect>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTakeInventory, getTakeInventory, delTakeInventory, addTakeInventory, updateTakeInventory } from "@/api/mes/warehouse/takeInventory";
import { getUserProfile } from "@/api/system/user";
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import { listWarehouse } from '@/api/mes/warehouse/warehouse'
import { listUser } from '@/api/system/user'
import { listInventory } from "@/api/mes/warehouse/inventory";
import { addByTakeInventory } from "@/api/mes/warehouse/profitLossRecord";
export default {
  name: "TakeInventory",
  dicts: ['bill_status','take_inventory_entry_status'],
  components: { MaterialSelect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedTakeInventoryEntry: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库存盘点表格数据
      takeInventoryList: [],
      // 库存盘点明细表格数据
      takeInventoryEntryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 扩展字段时间范围
      daterangeTakeTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        name: null,
        number: null,
        warehouseId: null,
        keeperName: null,
        takeTime: null,
        status: null,
        correctFlag:'N',
        initializeFlag:'N'

      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {

        warehouseId: [
          { required: true, message: "仓库不能为空", trigger: "change" }
        ],
        keeperId: [
          { required: true, message: "仓管员不能为空", trigger: "blur" }
        ],
        materialName: [
          {
            required: true,
            message: '产品不能为空',
            trigger: 'blur',
          },
        ],
        takeTime:[
          {
            required: true,
            message: '盘点时间必填',
            trigger: 'blur',
          },
        ],
        name:[
          {
            required: true,
            message: '单据名称必填',
            trigger: 'blur',
          },
        ],
        quantity:[
          {
            required: true,
            pattern:/^[1-9]*[1-9][0-9]*$/g,
            message: '请输入大于等于0的数字',
            trigger: "blur"
          }
        ],
      },
      currentRow: '',
      //排序索引
      maxIndex: 0,
      userList: [],
      warehouseList:[],
    };
  },
  created() {
    this.getUserList();
    this.getWarehouseList();
    this.getList();
  },
  methods: {
    /** 查询库存盘点列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeTakeTime && '' != this.daterangeTakeTime) {
        this.queryParams.params["beginTakeTime"] = this.daterangeTakeTime[0];
        this.queryParams.params["endTakeTime"] = this.daterangeTakeTime[1];
      }
      listTakeInventory(this.queryParams).then(response => {
        this.takeInventoryList = response.rows;
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
        name: null,
        initializeFlag: 'N',
        correctFlag: 'N',
        number: null,
        warehouseId: null,
        warehouseNumber: null,
        warehouseName: null,
        regionId: null,
        regionNumber: null,
        regionName: null,
        areaId: null,
        areaNumber: null,
        areaName: null,
        keeperId: null,
        keeperName: null,
        takeTime: null,
        checker: null,
        checkDate: null,
        status: 'A',
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        snArray: null
      };
      this.takeInventoryEntryList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeTakeTime = [];
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
      this.title = "添加库存盘点";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTakeInventory(id).then(response => {
        this.form = response.data;
        this.takeInventoryEntryList = response.data.takeInventoryEntryList;
        this.open = true;
        this.title = "修改库存盘点";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.takeInventoryEntryList = this.takeInventoryEntryList;
          if (this.form.id != null) {
            updateTakeInventory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTakeInventory(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除库存盘点编号为"' + ids + '"的数据项？').then(function() {
        return delTakeInventory(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 库存盘点明细添加按钮操作 */
    handleAddTakeInventoryEntry() {
      //编辑状态获取当前最大的sort为maxIndex
      if(this.form.id != null){
        let tmp =0;
        for (let i = 0; i < this.takeInventoryEntryList.length; i++) {
          if(this.takeInventoryEntryList[i].sort>tmp){
            tmp=this.takeInventoryEntryList[i].sort;
          }
        }
        this.maxIndex =tmp;
      }
      this.maxIndex = this.maxIndex+1
      let obj = {};
      obj.sort = this.maxIndex;
      obj.materialId = "";
      obj.materialNumber = "";
      obj.materialName = "";
      obj.specification = "";
      obj.materialUnitId = "";
      obj.materialUnitName = "";
      obj.inventoryQuantity = "";
      obj.takeQuantity = "";
      obj.differenceQuantity = "";
      obj.warehouseId = "";
      obj.warehouseNumber = "";
      obj.warehouseName = "";
      obj.regionId = "";
      obj.regionNumber = "";
      obj.regionName = "";
      obj.areaId = "";
      obj.areaNumber = "";
      obj.areaName = "";
      obj.remark = "";
      obj.extendField = "";
      obj.status='A'
      this.takeInventoryEntryList.push(obj);
    },
    /** 库存盘点明细删除按钮操作 */
    handleDeleteTakeInventoryEntry() {
      if (this.checkedTakeInventoryEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的库存盘点明细数据");
      } else {
        const takeInventoryEntryList = this.takeInventoryEntryList;
        console.log("takeInventoryEntryList====>",takeInventoryEntryList)
        const checkedTakeInventoryEntry = this.checkedTakeInventoryEntry;
        console.log("checkedTakeInventoryEntry====>",checkedTakeInventoryEntry)
        this.takeInventoryEntryList = takeInventoryEntryList.filter(function(item) {
          return checkedTakeInventoryEntry.indexOf(item.sort) == -1
        });
        this.$forceUpdate();
      }
    },
    /** 复选框选中数据 */
    handleTakeInventoryEntrySelectionChange(selection) {
      this.checkedTakeInventoryEntry = selection.map(item => item.sort)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('warehouse/takeInventory/export', {
        ...this.queryParams
      }, `takeInventory_${new Date().getTime()}.xlsx`)
    },
    //物料选择弹出框
    handleSelectProduct(row){
      this.currentRow = row;
      this.$refs.materialSelect.showFlag = true;
    },
    //接收物料弹框返回参数
    onMaterialSelect(obj){
      if(obj != undefined && obj != null){
        let row = this.currentRow;
        row.materialId = obj.id;
        row.materialName = obj.name;
        row.materialNumber = obj.number;
        row.specification= obj.specification;
        row.materialUnitId=obj.unitId;
        row.materialUnitName = obj.unitName;
      }
      this.$refs["form"].validate();
    },
    /** 获取销售员数据*/
    getUserList(){
      let qp =   {pageSize : 99999} ;
      listUser(this.addDateRange(qp, [])).then(response => {
          this.userList = response.rows;
        }
      );
    },
    userChange(value){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      this.form.keeperName =opt.nickName;
    },
    getWarehouseList(){
      let qp =  {pageSize:99999};
      listWarehouse(qp).then(response => {
        this.warehouseList = response.rows;
      });
    },
    warehouseSelect(id){
      let warehouse = [];
      warehouse = this.warehouseList.filter(function(item) {
        return item.id === id
      });
      this.form.warehouseId=warehouse[0].id;
      this.form.warehouseNumber=warehouse[0].number;
      this.form.warehouseName=warehouse[0].name;

      listInventory({warehouseId :this.form.warehouseId,pageSize: 99999}).then(res =>{
        console.log("res====>",res.rows);
        let resList = res.rows;
        this.takeInventoryEntryList=[];
        for (let i = 0; i < resList.length; i++) {
          let resObj = resList[i];
          let obj = {};
          obj.sort = i+1;
          obj.materialId = resObj.materialId;
          obj.materialNumber = resObj.materialNumber;
          obj.materialName = resObj.materialName;
          obj.specification = resObj.specification;
          obj.materialUnitName = resObj.materialUnitName;
          obj.inventoryQuantity = resObj.quantity;
          obj.takeQuantity = resObj.quantity;
          obj.differenceQuantity = 0;
          obj.warehouseId = resObj.warehouseId;
          obj.warehouseName = resObj.warehouseName;
          obj.extendField = "";
          obj.status='A'
          this.takeInventoryEntryList.push(obj);
        }
      })

    },
    sortUp(){
      const checkedEntry = this.checkedTakeInventoryEntry;
      if(checkedEntry.length>1||checkedEntry.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }

      if(checkedEntry[0]===1){
        this.$modal.msgError("已经在最上面啦");
        return;
      }
      let takeInventoryEntryList = this.form.takeInventoryEntryList;
      for (let i = 0; i <takeInventoryEntryList.length; i++) {
        let tmpSort =takeInventoryEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedEntry[0]){
          //与上一个交换顺序
          tmp =this.form.takeInventoryEntryList[i-1];
          takeInventoryEntryList[i-1]=takeInventoryEntryList[i]
          takeInventoryEntryList[i-1].sort =tmpSort-1
          takeInventoryEntryList[i]=tmp;
          takeInventoryEntryList[i].sort=tmpSort;
        }
      }
      console.log("takeInventoryEntryList=====>",takeInventoryEntryList)
      this.form.takeInventoryEntryList=takeInventoryEntryList;
      this.$refs.entryTable.sort("sort","ascending")
      this.checkedEntry[0]=checkedEntry[0]-1;
    },
    sortDown(){
      const checkedEntry = this.takeInventoryEntryList;
      if(checkedEntry.length>1||checkedEntry.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }
      let takeInventoryEntryList = this.form.takeInventoryEntryList;
      if(checkedEntry[0]===takeInventoryEntryList.length){
        this.$modal.msgError("已经在最下面啦");
        return;
      }
      for (let i = 0; i <takeInventoryEntryList.length; i++) {
        let tmpSort =takeInventoryEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedEntry[0]){
          //与下一个交换顺序
          tmp =this.form.takeInventoryEntryList[i+1];
          takeInventoryEntryList[i+1]=takeInventoryEntryList[i]
          takeInventoryEntryList[i+1].sort =tmpSort+1
          takeInventoryEntryList[i]=tmp;
          takeInventoryEntryList[i].sort=tmpSort;
        }
      }
      console.log("salOrderEntryList=====>",takeInventoryEntryList)
      this.form.takeInventoryEntryList=takeInventoryEntryList;
      this.$refs.entryTable.sort("sort","ascending")
      this.checkedEntry[0]=checkedEntry[0]+1;
    },
    takeQuantityInput(value,row){
      if(row.takeQuantity-row.inventoryQuantity==0){
        row.status ='A';
      }
      if(row.takeQuantity-row.inventoryQuantity>0){
        row.status ='B';
      }
      if(row.takeQuantity-row.inventoryQuantity<0){
        row.status ='C';
      }
      row.differenceQuantity=Math.abs(row.takeQuantity-row.inventoryQuantity)
    },
    kcjzClick(){
      const id =  this.ids;
      let takeInventoryList =  this.takeInventoryList.filter(item => item.id ==id);
      if(takeInventoryList[0].initializeFlag==='Y'||takeInventoryList[0].correctFlag==='Y'){
        this.$modal.msgWarning("期初数据和已矫正的盘点不允许再次矫正");
        return;
      }

      addByTakeInventory(id).then( res =>{
        this.$modal.msgSuccess("新增盘点盈亏单成功");
      })


    }
  }
};
</script>
<style lang="scss">
.tableBox {
  th {
    padding: 0 !important;
    height: 40px;
    line-height: 40px;
  }
  td {
    padding: 0 !important;
    height: 40px;
    line-height: 40px;
  }
}
</style>
