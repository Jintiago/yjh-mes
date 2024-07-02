<template>
  <div class="app-container">
    <!-- 添加或修改销售订单对话框 -->
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">

      <el-row>
        <el-col :span="8">
          <el-form-item label="入库单号" prop="number">
            <el-input v-model="form.number" placeholder="不输入则自动生成" style="width: 220px"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="入库仓库" prop="warehouseId">
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
        <el-col :span="8">
          <el-form-item label="仓库编号" prop="warehouseNumber">
            <el-input v-model="form.warehouseNumber" placeholder="请输入仓库编号" :disabled="true"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="入库日期" prop="intoDate">
            <el-date-picker clearable
                            v-model="form.intoDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="请选择领料时间">
            </el-date-picker>
          </el-form-item>

        </el-col>
        <el-col :span="8">
          <el-form-item label="客户" prop="customerName">
            <el-select v-model="form.customerName" clearable filterable  placeholder="请选择客户" @change="customerChange">
              <el-option
                v-for="item in customerList"
                :key="item.id"
                :label="item.name"
                :value="item.name"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="仓管员" prop="keeperId">
            <el-select v-model="form.keeper" clearable filterable  placeholder="请选择仓管员" @change="keeperChange">
              <el-option
                v-for="item in userList"
                :key="item.userId"
                :label="item.nickName"
                :value="item.nickName"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
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
      <el-divider content-position="center">明细信息</el-divider>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">添加</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteEntry">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="sortUp">上移</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="sortDown">下移</el-button>
        </el-col>
      </el-row>
      <el-table :data="form.saleReturnEntryList"  @selection-change="handleSelectionChange" ref="entryTable">
        <el-table-column type="selection" width="50" align="center" />
        <!--  动态属性        -->
        <template v-for="(item,index) in entryFieldExtendList" >
          <el-table-column :label="item.fieldName" align="center"  :width="item.width+48" show-overflow-tooltip='true' >
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;"
                            :prop="'saleReturnEntryList.'+scope.$index+'.'+formPropFormat(item)"
                            :rules="rules[item.field]"
              >
              <span v-if="item.type==='date'">
                  <el-date-picker clearable v-model="scope.row[item.field]" type="date" value-format="yyyy-MM-dd" style="width: 140px"  />
              </span>
                <span v-else-if="item.type.includes('dict:')">
                  <dict-tag :options="dict.type[dictFormat(item)]" :value="scope.row[item.field]"/>
              </span>
                <span v-else-if="item.field==='materialName'">
                  <el-input v-model="scope.row.materialName" placeholder="产品为必填项" >
                    <el-button slot="append" @click="handleSelectProduct(scope.row)" icon="el-icon-search" ></el-button>
                  </el-input>
              </span>
                <span v-else>
                  <el-input v-if="item.isSystem=='Y'" v-model="scope.row[item.field]"  />
                  <el-input v-else v-model="scope.row.extendFieldJson[item.field]"  />
              </span>
              </el-form-item>
            </template>
          </el-table-column>
        </template>
      </el-table>
    </el-form>
    <MaterialSelect ref="materialSelect" @onMaterialSelect="onMaterialSelect" > </MaterialSelect>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </div>
</template>

<script>
import { listUser } from '@/api/system/user'
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import { listWarehouse } from '@/api/mes/warehouse/warehouse'
import { getUserProfile } from "@/api/system/user";
import { listUnit } from '@/api/mes/masterdata/unit'
import request from '@/utils/request'
import { listSaleOut, getSaleOut, delSaleOut, addSaleOut, updateSaleOut } from "@/api/mes/warehouse/saleOut";
import { listCustomer, getCustomer, delCustomer, addCustomer, updateCustomer } from "@/api/mes/masterdata/customer";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import { listSaleOrder, delSaleOrder } from "@/api/mes/sale/saleOrder";
import { listSaleReturn, getSaleReturn, delSaleReturn, addSaleReturn, updateSaleReturn } from "@/api/mes/warehouse/saleReturn";
import {fieldFormat, dictFormat, formPropFormat} from '@/utils/jinzhong';
export default {
  name: 'form',
  components: { MaterialSelect },
  dicts: ['bill_status','procure_method','invoice_mark'],
  data() {
    return {
      // 子表选中数据
      checkedEntry: [],
      // 表单参数
      form: {
        status: 'A',
        createBy:'',
        outTime : new Date().format("yyyy-MM-dd HH:mm:ss"),
        saleReturnEntryList: [
        ],
      },
      // 表单校验
      rules: {
        number: [
          {  message: "单号不能为空", trigger: "blur" }
        ],
        outTime: [
          { required: true, message: "领料日期不能为空", trigger: "blur" }
        ],
        materialName: [
          {
            required: true,
            message: '产品不能为空',
            trigger: 'blur',
          },
        ],
        customerName:[
          {
            required: true,
            message: '客户不能为空',
            trigger: 'blur',
          }
        ],
        warehouseId:[
          {
            required: true,
            message: '领料仓库不能为空',
            trigger: 'blur',
          }
        ],
        quantity:[
          {
            required: true,
            pattern:/^[1-9]*[1-9][0-9]*$/g,
            message: '请输入大于等于0的数字',
            trigger: "blur"
          }
        ]
      },
      currentRow: '',
      //页面类型，false新增，true编辑
      formType: false,
      //排序索引
      maxIndex: 0,
      userList: [],
      customerList:[],
      warehouseList:[],
      unitList:[],
      entryFieldExtendList:{},
    };
  },
  created() {
    this.formType=false;
    this.getUserList();
    this.getUnitList();
    this.getWarehouseList();
    this.getCustomerList();
    this.getFieldExtendList();
    const id = this.$route.params && this.$route.params.id;
    if ( typeof(id) !='undefined' ){
      this.getForm(id);
    }

  },
  methods: {
    getForm(id) {
      this.formType=true;
      getSaleReturn(id).then(response => {
        this.form = response.data;
        this.form.saleReturnEntryList = response.data.saleReturnEntryList;
      });
    },
    getFieldExtendList(){
      //获取销售订单子表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'saleReturnEntry'}).then(response => {
        let list =response.rows;
        this.entryFieldExtendList  = list.filter(function(fieldExtend) {
          //过滤掉主表的字段
          let field = fieldExtend.field;
          let arr = field.split(".");
          return arr.length==1;
        })
        this.loading = false;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let list = this.form.saleReturnEntryList;
          if(list.length<1){
            this.$modal.msgWarning("请录入明细");
            return ;
          }

          if (this.form.id != null) {
            updateSaleReturn(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              const obj = { path: "/wm/saleReturn" };
              this.$tab.closeOpenPage(obj);
            });
          } else {
            addSaleReturn(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              const obj = { path: "/wm/saleReturn" };
              this.$tab.closeOpenPage(obj);
            });
          }
        }
      });
    },

    /** 添加按钮操作 */
    handleAdd() {
      //编辑状态获取当前最大的sort为maxIndex
      if(this.form.id != null){
        let tmp =0;
        for (let i = 0; i < this.form.saleReturnEntryList.length; i++) {
          if(this.form.saleReturnEntryList[i].sort>tmp){
            tmp=this.form.saleReturnEntryList[i].sort;
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
      obj.quantity = 1;
      obj.batchNumber = "";
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
      obj.extendFieldJson = {};
      for (let i = 0; i < this.entryFieldExtendList.length; i++) {
        if(this.entryFieldExtendList[i].isSystem!=='Y'){
          var field =this.entryFieldExtendList[i].field;
          obj.extendFieldJson[field]='';
        }
      }
      console.log("obj.extendFieldJson===>",obj.extendFieldJson)
      this.form.saleReturnEntryList.push(obj);
      this.$refs.entryTable.sort("sort","ascending")
    },

    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteEntry() {
      if (this.checkedEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的数据");
      } else {
        const saleReturnEntryList = this.form.saleReturnEntryList;
        const checkedEntry = this.checkedEntry;
        this.form.saleReturnEntryList = saleReturnEntryList.filter(function(item) {
          return checkedEntry.indexOf(item.sort) == -1
        });
        console.log("this.checkedEntry========>",this.checkedEntry)
        for (let i = 0; i <this.form.saleReturnEntryList.length; i++) {
          this.form.saleReturnEntryList[i].sort=i+1
        }
        this.maxIndex=this.form.saleReturnEntryList.length;
        this.$refs.entryTable.sort("sort","ascending")
        this.$refs.entryTable.clearSelection();
        this.checkedEntry=[]
      }
    },

    /** 复选框选中数据 */
    handleSelectionChange(selection) {
      this.checkedEntry = selection.map(item => item.sort)

    },
    //物料选择弹出框
    handleSelectProduct(row){
      this.currentRow = row;
      this.$refs.materialSelect.showFlag = true;
    },
    //接收物料弹框返回参数
    onMaterialSelect(obj){
      if(obj != undefined && obj != null){
        // let entryList = this.form.saleReturnEntryList;
        // for (let i = 0; i < entryList.length; i++) {
        //   let materialName = entryList[i].materialName;
        //   if(materialName === obj.name){
        //     this.$modal.msgError("请不要选择重复的产品");
        //     return ;
        //   }
        // }
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
    sortUp(){
      const checkedEntry = this.checkedEntry;
      if(checkedEntry.length>1||checkedEntry.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }

      if(checkedEntry[0]===1){
        this.$modal.msgError("已经在最上面啦");
        return;
      }
      let saleReturnEntryList = this.form.saleReturnEntryList;
      for (let i = 0; i <saleReturnEntryList.length; i++) {
        let tmpSort =saleReturnEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedEntry[0]){
          //与上一个交换顺序
          tmp =this.form.saleReturnEntryList[i-1];
          saleReturnEntryList[i-1]=saleReturnEntryList[i]
          saleReturnEntryList[i-1].sort =tmpSort-1
          saleReturnEntryList[i]=tmp;
          saleReturnEntryList[i].sort=tmpSort;
        }
      }
      this.form.saleReturnEntryList=saleReturnEntryList;
      this.$refs.entryTable.sort("sort","ascending")
      this.checkedEntry[0]=checkedEntry[0]-1;
    },
    sortDown(){
      const checkedEntry = this.checkedEntry;
      if(checkedEntry.length>1||checkedEntry.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }
      let saleReturnEntryList = this.form.saleReturnEntryList;
      if(checkedEntry[0]===saleReturnEntryList.length){
        this.$modal.msgError("已经在最下面啦");
        return;
      }
      for (let i = 0; i <saleReturnEntryList.length; i++) {
        let tmpSort =saleReturnEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedEntry[0]){
          //与下一个交换顺序
          tmp =this.form.saleReturnEntryList[i+1];
          saleReturnEntryList[i+1]=saleReturnEntryList[i]
          saleReturnEntryList[i+1].sort =tmpSort+1
          saleReturnEntryList[i]=tmp;
          saleReturnEntryList[i].sort=tmpSort;
        }
      }
      this.form.saleReturnEntryList=saleReturnEntryList;
      this.$refs.entryTable.sort("sort","ascending")
      this.checkedEntry[0]=checkedEntry[0]+1;
    },

    /** 获取人员数据*/
    getUserList(){
      let qp =   {pageSize : 99999} ;
      listUser(this.addDateRange(qp, [])).then(response => {
          this.userList = response.rows;
        }
      );
    },
    customerChange(value){
      let opt= {};
      opt= this.customerList.find((item)=>{
        return item.id === key;
      });
      this.form.customerId =opt.id;
    },

    /** 获取仓库数据*/
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

    },
    getCustomerList(){
      listCustomer({pageSize:99999,status:'0'}).then(response => {
        this.customerList = response.rows;
      });
    },
    /** 获取计量单位数据*/
    getUnitList(){
      let qp =  {pageSize:99999};
      listUnit(qp).then(response => {
        this.unitList = response.rows;
      });
    },
    keeperChange(value){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      this.form.keeperName =opt.nickName;
    },
    fieldFormat(row,item){
      return fieldFormat(row,item);
    },
    formPropFormat(item){
      return formPropFormat(item);
    }

  }
}
</script>

