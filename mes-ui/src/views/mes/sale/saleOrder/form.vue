<template>
  <div class="app-container">
  <!-- 添加或修改销售订单对话框 -->
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="订单编号" prop="number">
            <el-input v-model="form.number" placeholder="请输入单号" :disabled="isDisabled"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="客户" prop="customerId">
            <el-input v-model="form.customerName" placeholder="" :disabled="isDisabled"/>
<!--            <el-select v-model="form.customerId" filterable placeholder="请选择客户" @change="customerChange">-->
<!--              <el-option-->
<!--                v-for="item in customerList"-->
<!--                :key="item.id"-->
<!--                :label="item.name"-->
<!--                :value="item.id"-->
<!--              >-->
<!--              </el-option>-->
<!--            </el-select >-->
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="销售日期" prop="saleDate" style="margin-left: 20px" >
            <el-date-picker clearable
                            v-model="form.saleDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="请选择销售日期" :disabled="isDisabled">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="审核人" prop="checker" style="margin-left: 18px">
            <el-input v-model="form.checker"   :disabled="isDisabled" />
          </el-form-item>
        </el-col>
<!--        <el-col :span="6">-->
<!--          <el-form-item label="结算币别" prop="currency" style="margin-left: 20px">-->
<!--            <el-select v-model="form.currency"  :disabled="isDisabled">-->
<!--              <el-option-->
<!--                v-for="dict in dict.type.currency"-->
<!--                :key="dict.value"-->
<!--                :label="dict.label"-->
<!--                :value="dict.value"-->
<!--              />-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
      </el-row>
      <el-row >
<!--        <el-col :span="6">-->
<!--          <el-form-item label="销售部门" prop="deptId">-->
<!--            <treeselect @select="selectDept" v-model="form.deptId" :options="deptOptions" :show-count="true" placeholder="请选择销售部门" style="width: 240px" />-->
<!--          </el-form-item>-->
<!--        </el-col>-->
        <el-col :span="6">
          <el-form-item label="销售员" prop="userId">
            <el-input v-model="form.userName" placeholder="" :disabled="isDisabled"/>
<!--            <el-select v-model="form.userId" filterable placeholder="请选择销售员" @change="userChange">-->
<!--              <el-option-->
<!--                v-for="item in userList"-->
<!--                :key="item.userId"-->
<!--                :label="item.nickName"-->
<!--                :value="item.userId"-->
<!--              >-->
<!--              </el-option>-->
<!--            </el-select>-->
          </el-form-item>
        </el-col>
<!--        <el-col :span="6">-->
<!--          <el-form-item label="收货人" prop="consignee" style="margin-left: 20px" >-->
<!--            <el-input v-model="form.consignee" placeholder="请输入收货人" :disabled="isDisabled"/>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="6">-->
<!--          <el-form-item label="收货电话" prop="consigneePhone" style="margin-left: 18px" >-->
<!--            <el-input v-model="form.consigneePhone" placeholder="请输入收货电话" :disabled="isDisabled"/>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
        <el-col :span="12">
          <el-form-item label="备注" prop="remark" >
            <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="5" :disabled="isDisabled"/>
          </el-form-item>
        </el-col>
      </el-row>
<!--      <el-row>-->
<!--        <el-col :span="6">-->
<!--          <el-form-item label="省市区" prop="area" >-->
<!--            <el-cascader-->
<!--              :options="options"-->
<!--              v-model="form.area"-->
<!--              style="width: 235px"-->
<!--              filterable :disabled="isDisabled"></el-cascader>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="6">-->
<!--          <el-form-item label="街道信息" prop="address">-->
<!--            <el-input v-model="form.address" type="textarea" placeholder="请输入内容" :disabled="isDisabled"/>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="6">-->
<!--          <el-form-item label="物流单号" prop="logisticsNumber" style="margin-left: 18px">-->
<!--            <el-input v-model="form.logisticsNumber" placeholder="请输入物流单号" :disabled="isDisabled" />-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--      </el-row>-->
      <el-form-item label="源单信息" prop="sourceInfo" v-show="false">
        <el-input v-model="form.sourceInfo" type="textarea" placeholder="请输入内容" />
      </el-form-item>
      <el-divider content-position="center">销售订单明细</el-divider>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSalOrderEntry" :disabled="isDisabled">添加</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSalOrderEntry" :disabled="isDisabled">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="sortUp" :disabled="isDisabled">上移</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="sortDown" :disabled="isDisabled">下移</el-button>
        </el-col>
      </el-row>
      <el-table  :data="form.salOrderEntryList"  @selection-change="handleSalOrderEntrySelectionChange" ref="salOrderEntry"  class="tableBox">
        <el-table-column type="selection" width="40" align="center" />
        <!--  动态属性        -->
        <template v-for="(item,index) in entryFieldExtendList" >
          <el-table-column :label="item.fieldName" align="center"  :width="item.width+48" show-overflow-tooltip='true' >
            <template slot-scope="scope">
              <el-form-item label-width="0"  style="margin-bottom: 12px;"
                            :prop="'salOrderEntryList.'+scope.$index+'.'+formPropFormat(item)"
                            :rules="rules[item.field]"
              >
              <span v-if="item.type==='date'">
                  <el-date-picker clearable v-model="scope.row[item.field]" type="date" value-format="yyyy-MM-dd" style="width: 140px"  />
              </span>
              <span v-else-if="item.type.includes('dict:')">
                  <dict-tag :options="dict.type[dictFormat(item)]" :value="scope.row[item.field]"/>
              </span>
              <span v-else-if="item.field==='materialName'">
                  <el-input v-model="scope.row.materialName" placeholder="产品为必填项" :disabled="isDisabled">
                    <el-button slot="append" @click="handleSelectProduct(scope.row)" icon="el-icon-search" :disabled="isDisabled"></el-button>
                  </el-input>
              </span>
              <span v-else>
                <el-input v-if="item.isSystem=='Y'" v-model="scope.row[item.field]" :disabled="isDisabled" />
                <el-input v-else v-model="scope.row.extendFieldJson[item.field]"  :disabled="isDisabled"/>
              </span>
              </el-form-item>
            </template>
          </el-table-column>
        </template>

      </el-table>
    </el-form>
    <MaterialSelect ref="materialSelect" @onMaterialSelect="onMaterialSelect" > </MaterialSelect>
  <div slot="footer" class="dialog-footer">
    <el-button type="primary" @click="submitForm" >确 定</el-button>
  </div>
  </div>
</template>

<script>
import {  getSaleOrder,  addSaleOrder, updateSaleOrder } from "@/api/mes/sale/saleOrder";
import addressJson from '@/components/mes/address.json'
import { listUser,deptTreeSelect } from '@/api/system/user'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import { listCustomer} from "@/api/mes/masterdata/customer";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import {fieldFormat, dictFormat, formPropFormat} from '@/utils/jinzhong';
export default {
  name: 'form',
  components: { Treeselect,MaterialSelect },
  dicts: ['currency','salorder_status'],
  data() {
    return {
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSalOrderEntry: [],
      // ${subTable.functionName}表格数据

      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {
        currency:'CNY',
        saleDate : new Date(),
        salOrderEntryList: [
        ],
      },
      // 表单校验
      rules: {
        number: [
          { required: false, message: "单号不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
        currency: [
          { required: true, message: "结算币别不能为空", trigger: "blur" }
        ],
        saleDate: [
          { required: true, message: "销售日期不能为空", trigger: "blur" }
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
            pattern:/^[1-9]*[1-9][0-9]*$/g,
            message: '请输入大于等于0的数字',
            trigger: "blur"
          }
        ],
      },
      options: addressJson,
      // 部门树选项
      deptOptions: undefined,
      currentRow: '',
      //页面类型，false新增，true编辑
      formType: false,
      maxIndex: 0,
      userList: [],
      customerList: [],
      entryFieldExtendList:{},
      isDisabled:true
    };
  },
  created() {
    this.formType=false;
    this.getFieldExtendList();
    this.getDeptTree();
    this.getUserList();
    this.getCustomerList();
    const saleOrderId = this.$route.params && this.$route.params.saleOrderId;
    if ( typeof(saleOrderId) !='undefined' ){
      this.getForm(saleOrderId);
    }
  },
  mounted() {

  },
  methods: {
    getFieldExtendList(){
      //获取销售订单子表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'saleOrderEntry'}).then(response => {
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
    getForm(id) {
      this.formType=true;
      getSaleOrder(id).then(response => {
        console.log("response.data.area===>",response.data.area)
        if(response.data.area!=null){
          response.data.area =response.data.area.split(",")
        }
        this.form = response.data;
        this.form.salOrderEntryList = response.data.salOrderEntryList;
      });

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {

        if (valid) {
          let list = this.form.salOrderEntryList;
          if(list.length<1){
            this.$modal.msgWarning("请录入订单明细");
            return ;
          }

          if(this.form.area!=null){
            this.form.area=this.form.area.join(",");
          }

          if (this.form.id != null) {
            updateSaleOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              const obj = { path: "/pro/saleOrder" };
              this.$tab.closeOpenPage(obj);
            });
          } else {
            addSaleOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              const obj = { path: "/pro/saleOrder" };
              this.$tab.closeOpenPage(obj);
            });
          }
        }
      });
    },

    /** ${subTable.functionName}添加按钮操作 */
    handleAddSalOrderEntry() {
      //编辑状态获取当前最大的sort为maxIndex
      if(this.form.id != null){
        let tmp =0;
        for (let i = 0; i < this.form.salOrderEntryList.length; i++) {
          if(this.form.salOrderEntryList[i].sort>tmp){
            tmp=this.form.salOrderEntryList[i].sort;
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
      obj.materialSpecification = "";
      obj.materialClass = "";
      obj.materialType = "";
      obj.unitId = "";
      obj.unitName = "";
      obj.quantity = 1;
      obj.price = 99999;
      obj.taxPrice = "";
      obj.isGift = "1";
      obj.taxRate = "";
      obj.totalTax = "";
      obj.totalPrice = 99999;
      obj.totalTaxPrice = "";
      obj.needDate = new Date();
      obj.deliveryDate = new Date();
      obj.remark = "";
      obj.extendFieldJson = {};
      for (let i = 0; i < this.entryFieldExtendList.length; i++) {
        var field =this.entryFieldExtendList[i].field;
        obj.extendFieldJson[field]='';
      }
      this.form.salOrderEntryList.push(obj);
      this.$refs.salOrderEntry.sort("sort","ascending")
    },
    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteSalOrderEntry() {
      if (this.checkedSalOrderEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的数据");
      } else {
        const salOrderEntryList = this.form.salOrderEntryList;
        const checkedSalOrderEntry = this.checkedSalOrderEntry;
        this.form.salOrderEntryList = salOrderEntryList.filter(function(item) {
          return checkedSalOrderEntry.indexOf(item.sort) == -1
        });
        console.log("this.checkedSalOrderEntry========>",this.checkedSalOrderEntry)
        for (let i = 0; i <this.form.salOrderEntryList.length; i++) {
          this.form.salOrderEntryList[i].sort=i+1
        }
        this.maxIndex=this.form.salOrderEntryList.length;
        this.$refs.salOrderEntry.sort("sort","ascending")
        this.$refs.salOrderEntry.clearSelection();
        this.checkedSalOrderEntry=[]
      }
    },
    /** 复选框选中数据 */
    handleSalOrderEntrySelectionChange(selection) {
      this.checkedSalOrderEntry = selection.map(item => item.sort)

    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data;
      });
    },
    //物料选择弹出框
    handleSelectProduct(row){
      this.currentRow = row;
      this.$refs.materialSelect.showFlag = true;
    },
    //接收物料弹框返回参数
    onMaterialSelect(obj){
      if(obj != undefined && obj != null){
        let entryList = this.form.salOrderEntryList;
        for (let i = 0; i < entryList.length; i++) {
          let materialName = entryList[i].materialName;
          if(materialName === obj.name){
            this.$modal.msgError("请不要选择重复的产品");
            return ;
          }
        }

        let row = this.currentRow;
        row.materialId = obj.id;
        row.materialName = obj.name;
        row.materialNumber = obj.number;
        row.materialSpecification= obj.specification;
        row.unitId=obj.unitId;
        row.unitName = obj.unitName;
      }
      this.$refs["form"].validate();
    },
    sortUp(){
      const checkedSalOrderEntry = this.checkedSalOrderEntry;
      if(checkedSalOrderEntry.length>1||checkedSalOrderEntry.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }

      if(checkedSalOrderEntry[0]===1){
        this.$modal.msgError("已经在最上面啦");
        return;
      }
      let salOrderEntryList = this.form.salOrderEntryList;
      for (let i = 0; i <salOrderEntryList.length; i++) {
        let tmpSort =salOrderEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedSalOrderEntry[0]){
          //与上一个交换顺序
          tmp =this.form.salOrderEntryList[i-1];
          salOrderEntryList[i-1]=salOrderEntryList[i]
          salOrderEntryList[i-1].sort =tmpSort-1
          salOrderEntryList[i]=tmp;
          salOrderEntryList[i].sort=tmpSort;
        }
      }
      console.log("salOrderEntryList=====>",salOrderEntryList)
      this.form.salOrderEntryList=salOrderEntryList;
      this.$refs.salOrderEntry.sort("sort","ascending")
      this.checkedSalOrderEntry[0]=checkedSalOrderEntry[0]-1;
    },
    sortDown(){
      const checkedSalOrderEntry = this.checkedSalOrderEntry;
      if(checkedSalOrderEntry.length>1||checkedSalOrderEntry.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }
      let salOrderEntryList = this.form.salOrderEntryList;
      if(checkedSalOrderEntry[0]===salOrderEntryList.length){
        this.$modal.msgError("已经在最下面啦");
        return;
      }
      for (let i = 0; i <salOrderEntryList.length; i++) {
        let tmpSort =salOrderEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedSalOrderEntry[0]){
          //与下一个交换顺序
          tmp =this.form.salOrderEntryList[i+1];
          salOrderEntryList[i+1]=salOrderEntryList[i]
          salOrderEntryList[i+1].sort =tmpSort+1
          salOrderEntryList[i]=tmp;
          salOrderEntryList[i].sort=tmpSort;
        }
      }
      console.log("salOrderEntryList=====>",salOrderEntryList)
      this.form.salOrderEntryList=salOrderEntryList;
      this.$refs.salOrderEntry.sort("sort","ascending")
      this.checkedSalOrderEntry[0]=checkedSalOrderEntry[0]+1;
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
    /** 获取客户数据*/
    getCustomerList(){
      listCustomer({pageSize : 99999}).then(response => {
          this.customerList = response.rows;
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
    fujianDow(){
      window.open("")
    },
    fieldFormat(row,item){
      return fieldFormat(row,item);
    },
    dictFormat(item){
      return dictFormat(item);
    },
    formPropFormat(item){
      return formPropFormat(item);
    }
  }
}
</script>
<style lang="scss">
.tableBox {
  th {
    padding: 0 !important;
    height: 5px;
    line-height: 5px;
  }
  td {
    padding: 0 !important;
    height: 5px;
    line-height: 5px;
  }
}
</style>
