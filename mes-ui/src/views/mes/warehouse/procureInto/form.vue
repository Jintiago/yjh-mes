<template>
  <div class="app-container">
  <!-- 添加或修改销售订单对话框 -->
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="入库单编号" prop="number">
            <el-input v-model="form.number" placeholder="不输入则自动生成" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="入库日期" prop="intoDate">
            <el-date-picker clearable
                            v-model="form.intoDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="请选择入库日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="供应商" prop="supplierId">
            <el-select v-model="form.supplierId" clearable filterable  placeholder="请选择" @change="supplierChange">
              <el-option
                v-for="item in supplierList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
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
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea"
                      :rows="2" v-model="form.remark" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="总金额" prop="price">
            <el-input
                       v-model="form.price" placeholder="" :disabled="true"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="center">产品入库单明细</el-divider>
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
      <el-table  :data="form.procureIntoEntryList"  @selection-change="handleSelectionChange" ref="entryTable"  style="width: 100%">
        <el-table-column type="selection" width="40" align="center" />
        <el-table-column label="序号" prop="sort" width="50"  >
          <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;">
                <el-input v-model="scope.row.sort"  :disabled="true" />
              </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="产品名"  width="240" >
          <template slot-scope="scope">
            <el-form-item   :prop="'procureIntoEntryList.'+scope.$index+'.materialName'" :rules="rules.materialName" label-width="0"  style="margin-bottom: 12px;">
              <el-input v-model="scope.row.materialName" placeholder="产品为必填项" >
                <el-button slot="append" @click="handleSelectProduct(scope.row)" icon="el-icon-search"></el-button>
              </el-input>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="产品编号" prop="materialNumber" width="120">
          <template slot-scope="scope">
            <el-form-item label-width="0"  style="margin-bottom: 12px;">
              <el-input v-model="scope.row.materialNumber" :disabled="true" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="规格型号" prop="materialSpecification" width="150">
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
        <el-table-column label="数量"  width="120">
          <template slot-scope="scope">
            <el-form-item label-width="0"  style="margin-bottom: 12px;" :rules="rules.quantity"
                          :prop="'procureIntoEntryList.'+scope.$index+'.quantity'">
              <el-input v-model="scope.row.quantity"  @input="countPrice($event,scope.row,scope.row)" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="单价"  width="120">
          <template slot-scope="scope">
            <el-form-item label-width="0"  style="margin-bottom: 12px;"
                          :prop="'procureIntoEntryList.'+scope.$index+'.price'">
              <el-input v-model="scope.row.price"   @input="countPrice($event,scope.row,scope.row)"/>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="金额"  width="120">
          <template slot-scope="scope">
            <el-form-item label-width="0"  style="margin-bottom: 12px;"
                          :prop="'procureIntoEntryList.'+scope.$index+'.totalPrice'">
              <el-input v-model="scope.row.totalPrice"  :disabled="true" />
            </el-form-item>
          </template>
        </el-table-column>
<!--        <el-table-column label="批次编号" prop="batchNumber" >-->
<!--          <template slot-scope="scope">-->
<!--            <el-form-item label-width="0"  style="margin-bottom: 12px;">-->
<!--              <el-input v-model="scope.row.batchNumber"  />-->
<!--            </el-form-item>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column label="生产日期" prop="manufactureDate" >-->
<!--          <template slot-scope="scope">-->
<!--            <el-form-item label-width="0"  style="margin-bottom: 12px;">-->
<!--              <el-date-picker clearable v-model="scope.row.manufactureDate" type="date" value-format="yyyy-MM-dd"  />-->
<!--            </el-form-item>-->
<!--          </template>-->
<!--        </el-table-column>-->
      </el-table>
    </el-form>
    <MaterialSelect ref="materialSelect" @onMaterialSelect="onMaterialSelect" > </MaterialSelect>
  <div slot="footer" class="dialog-footer">
    <el-button type="primary" @click="submitForm">确 定</el-button>
  </div>
  </div>
</template>

<script>
import { listProcureInto, getProcureInto, delProcureInto, addProcureInto, updateProcureInto } from "@/api/mes/warehouse/procureInto";
import { listUser } from '@/api/system/user'
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import { listWarehouse } from '@/api/mes/warehouse/warehouse'
import { getUserProfile } from "@/api/system/user";
import { listSupplier, getSupplier, delSupplier, addSupplier, updateSupplier } from "@/api/mes/masterdata/supplier";
import request from '@/utils/request'
export default {
  name: 'form',
  components: { MaterialSelect },
  dicts: [],
  data() {
    return {
      // 子表选中数据
      checkedEntry: [],
      // 表单参数
      form: {
        status: 'A',
        createBy:'',
        intoDate : new Date().format("yyyy-MM-dd HH:mm:ss"),
        procureIntoEntryList: [
        ],
      },
      // 表单校验
      rules: {
        number: [
          {  message: "单号不能为空", trigger: "blur" }
        ],
        intoDate: [
          { required: true, message: "入库日期不能为空", trigger: "blur" }
        ],
        delivererId: [
          { required: true, message: "交货人不能为空", trigger: "blur" }
        ],
        materialName: [
          {
            required: true,
            message: '产品不能为空',
            trigger: 'blur',
          },
        ],
        quantity:[
          {
            required: true,
            pattern:/^([1-9]\d*(\.\d*[1-9][0-9])?)|(0\.\d*[1-9][0-9])|(0\.\d*[1-9])$/g,
            message: '请输入大于等于0的数字',
            trigger: "blur"
          }
        ],
        warehouseId:[
          {
            required: true,
            message: '仓库不能为空',
            trigger: 'blur',
          }
        ],
      },
      currentRow: '',
      //页面类型，false新增，true编辑
      formType: false,
      //排序索引
      maxIndex: 0,
      supplierList: [],
      warehouseList:[],
    };
  },
  created() {
    this.formType=false;
    this.getSupplierList();
    this.getWarehouseList();
    const id = this.$route.params && this.$route.params.id;
    if ( typeof(id) !='undefined' ){
      this.getForm(id);
    }
  },
  methods: {
    getForm(id) {
      this.formType=true;
      getProcureInto(id).then(response => {
        this.form = response.data;
        this.form.procureIntoEntryList = response.data.procureIntoEntryList;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let list = this.form.procureIntoEntryList;
          if(list.length<1){
            this.$modal.msgWarning("请录入入库单明细");
            return ;
          }

          if (this.form.id != null) {
            updateProcureInto(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              const obj = { path: "/wm/procureInto" };
              this.$tab.closeOpenPage(obj);
            });
          } else {
            addProcureInto(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              const obj = { path: "/wm/procureInto" };
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
        for (let i = 0; i < this.form.procureIntoEntryList.length; i++) {
          if(this.form.procureIntoEntryList[i].sort>tmp){
            tmp=this.form.procureIntoEntryList[i].sort;
          }
        }
        this.maxIndex =tmp;
      }
      this.maxIndex = this.maxIndex+1
      let obj = {};
      obj.sort = this.maxIndex;
      obj.materialNumber = "";
      obj.materialName = "";
      obj.specification = "";
      obj.materialUnitName = "";
      obj.quantity = 0;
      obj.batchNumber = "";
      obj.warehouseNumber = "";
      obj.warehouseName = "";
      obj.manufactureDate = new Date();
      console.log(obj)
      this.form.procureIntoEntryList.push(obj);
      this.$refs.entryTable.sort("sort","ascending")
      this.$forceUpdate();
    },

    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteEntry() {
      console.log("this.$refs.materialSelect===>",this.$refs)
      if (this.checkedEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的数据");
      } else {
        let procureIntoEntryList = this.form.procureIntoEntryList;
        const checkedEntry = this.checkedEntry;
        for (let i = 0; i < procureIntoEntryList.length; i++) {
          for (let j = 0; j < checkedEntry.length; j++) {
            if(checkedEntry[j]===procureIntoEntryList[i].sort){
              this.form.procureIntoEntryList.splice(i,1);
            }
          }
        }
        for (let i = 0; i <this.form.procureIntoEntryList.length; i++) {
          this.form.procureIntoEntryList[i].sort=i+1
        }
        this.maxIndex=this.form.procureIntoEntryList.length;
        this.$refs.entryTable.sort("sort","ascending")
        this.$refs.entryTable.clearSelection();
        this.checkedEntry=[]

      }
      this.counttotoalPrice();
    },

    /** 复选框选中数据 */
    handleSelectionChange(selection) {
      this.checkedEntry = selection.map(item => item.sort)

    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data;
      });
    },
    //物料选择弹出框
    handleSelectProduct(row){
      console.log("this.$refs.materialSelect===>",this.$refs)
      this.currentRow = row;
      this.$refs.materialSelect.showFlag = true;
    },
    //接收物料弹框返回参数
    onMaterialSelect(obj){
      if(obj != undefined && obj != null){
        // let entryList = this.form.procureIntoEntryList;
        // for (let i = 0; i < entryList.length; i++) {
        //   let materialName = entryList[i].materialName;
        //   if(materialName === obj.name){
        //     this.$modal.msgError("请不要选择重复的产品");
        //     return ;
        //   }
        // }
        console.log("obj==========>",obj)
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
      let procureIntoEntryList = this.form.procureIntoEntryList;
      for (let i = 0; i <procureIntoEntryList.length; i++) {
        let tmpSort =procureIntoEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedEntry[0]){
          //与上一个交换顺序
          tmp =this.form.procureIntoEntryList[i-1];
          procureIntoEntryList[i-1]=procureIntoEntryList[i]
          procureIntoEntryList[i-1].sort =tmpSort-1
          procureIntoEntryList[i]=tmp;
          procureIntoEntryList[i].sort=tmpSort;
        }
      }
      this.form.procureIntoEntryList=procureIntoEntryList;
      this.$refs.entryTable.sort("sort","ascending")
      this.checkedEntry[0]=checkedEntry[0]-1;
    },
    sortDown(){
      const checkedEntry = this.checkedEntry;
      if(checkedEntry.length>1||checkedEntry.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }
      let procureIntoEntryList = this.form.procureIntoEntryList;
      if(checkedEntry[0]===procureIntoEntryList.length){
        this.$modal.msgError("已经在最下面啦");
        return;
      }
      for (let i = 0; i <procureIntoEntryList.length; i++) {
        let tmpSort =procureIntoEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedEntry[0]){
          //与下一个交换顺序
          tmp =this.form.procureIntoEntryList[i+1];
          procureIntoEntryList[i+1]=procureIntoEntryList[i]
          procureIntoEntryList[i+1].sort =tmpSort+1
          procureIntoEntryList[i]=tmp;
          procureIntoEntryList[i].sort=tmpSort;
        }
      }
      this.form.procureIntoEntryList=procureIntoEntryList;
      this.$refs.entryTable.sort("sort","ascending")
      this.checkedEntry[0]=checkedEntry[0]+1;
    },
    selectDept(node){
      this.form.deptName = node.label
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
      this.form.userName =opt.nickName;
    },
    customerChange(value){
      let opt= {};
      opt= this.customerList.find((item)=>{
        return item.id === value;
      });
      this.form.customerName =opt.name;
    },
    /** 获取计量单位数据*/
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
    supplierChange(value){
      let opt= {};
      opt= this.supplierList.find((item)=>{
        return item.id === value;
      });
      this.form.supplierName =opt.name;
    },
    getSupplierList(){
      listSupplier({pageSize:99999,status:'0'}).then(response => {
        this.supplierList = response.rows;
      });
    },
    countPrice(value,row){
      row.totalPrice = (row.quantity * row.price).toFixed(3);
      this.counttotoalPrice();
    },
    //计算主表合计金额
    counttotoalPrice(){
      var ic = 0.00;
      let list = this.form.procureIntoEntryList;
      for (let i = 0; i < list.length; i++) {
        if(list[i].totalPrice==='NaN'||typeof (list[i].totalPrice)==='undefined'){
          list[i].totalPrice=0;
        }
        ic=parseFloat(ic)+parseFloat(list[i].totalPrice);
      }
      this.form.price=ic.toFixed(3);
      this.$forceUpdate();
    }
  }
}
</script>

