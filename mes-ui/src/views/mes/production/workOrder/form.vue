<!-- 标准工序的生产工单页面 -->
<template>
  <div class="app-container">
  <!-- 添加或修改生产工单页面 -->
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="工单号" prop="number">
            <el-input v-model="form.number" placeholder="请输入工单号" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="批次号" prop="batchNumber">
            <el-input v-model="form.batchNumber" placeholder="请输入批次号" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="创建人" prop="createBy">
            <el-input v-model="form.createBy"  :disabled="true"/>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="状态" prop="status" >
            <el-select v-model="form.status"  disabled >
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
          <el-form-item label="物料" prop="materialName">
            <el-input v-model="form.materialName" placeholder="请选择产品" >
              <el-button slot="append" @click="handleSelectProduct" icon="el-icon-search"></el-button>
            </el-input>
            <MaterialSelect ref="materialSelect" @onMaterialSelect="onMaterialSelect" > </MaterialSelect>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="物料编号" prop="materialNumber">
            <el-input v-model="form.materialNumber"  :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="规格型号" prop="specification">
            <el-input v-model="form.specification" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="单位" prop="materialUnitName">
            <el-input v-model="form.materialUnitName"  :disabled="true" />
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
        <el-col :span="6">
          <el-form-item label="计划完工" prop="planFinishDate" >
            <el-date-picker clearable
                            v-model="form.planFinishDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="请选择计划完成日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="开始生产" prop="beginProDate">
            <el-date-picker clearable
                            v-model="form.beginProDate"
                            type="date"
                            :disabled="true"
                            value-format="yyyy-MM-dd"
                            placeholder="暂未开始生产"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="实际完工" prop="realFinishDate">
            <el-date-picker clearable
                            v-model="form.realFinishDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            :disabled="true"
                            placeholder="暂未完工"
              >
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row >
        <el-col :span="6">
          <el-form-item label="生产数量" prop="quantity" >
            <el-input v-model="form.quantity" placeholder="请输入本次生产数量" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="现有库存" prop="inventory">
            <el-input v-model="form.inventory" :disabled="true" placeholder="0" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入备注"  type="textarea" :rows="2"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item label="是否返修" prop="repairFlag">
        <el-switch
          v-model="form.repairFlag"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="Y"
          inactive-value="N">
        </el-switch>
        </el-form-item>
      </el-row>
      <el-divider content-position="center">工序路线明细</el-divider>
      <el-table  :data="form.workOrderEntryList"   ref="salOrderEntry"  >
        <el-table-column label="排序" prop="sort"  width="70" >
          <template slot-scope="scope">
            <el-input v-model="scope.row.sort"  :disabled="true" />
          </template>
        </el-table-column>
        <el-table-column label="工序" prop="processName" >
          <template slot-scope="scope">
            <el-input v-model="scope.row.processName"  :disabled="true"/>
          </template>
        </el-table-column>
        <el-table-column label="质检类型" prop="status" >
          <template slot-scope="scope">
            <el-form-item label-width="0"  style="margin-bottom: 0px;">
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
            <el-form-item label-width="0"  style="margin-bottom: 0px;" :rules="rules.firstQcQuantity"
                          :prop="'workOrderEntryList.'+scope.$index+'.firstQcQuantity'">
              <el-input v-model="scope.row.firstQcQuantity"   />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="车间" prop="workshopId" >
          <template  slot-scope="scope">
              <el-select v-model="scope.row.workshopId" placeholder="请选择车间"  @change="workshopSelectChange($event,scope.row)" style="width: 100%">
                <el-option
                  v-for="item in workshopOptions"
                  :key="item.id"
                  :label="item.name"
                  :value= "item.id"
                ></el-option>
              </el-select>
          </template>
        </el-table-column>
        <el-table-column label="工位" prop="stationId" >
          <template slot-scope="scope">
            <el-select v-model="scope.row.stationId" placeholder="请选择工位"  @change="stationSelectChange($event,scope.row)" style="width: 100%">
              <el-option
                v-for="item in stationOptions"
                :key="item.id"
                :label="item.name"
                :value= "item.id"
              ></el-option>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button type="primary" @click="submitForm" style="margin-left: 20px">提 交</el-button>
  </div>
  </div>
</template>

<script>
import {  getWorkOrder , addWorkOrder, updateWorkOrder } from "@/api/mes/production/workOrder";
import { getStation, listStation } from '@/api/mes/masterdata/station'
import { listUser, getUserProfile } from '@/api/system/user'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import { listCustomer} from "@/api/mes/masterdata/customer";
import { listRoute,getRoute } from "@/api/mes/production/route";
import { listWorkshop } from '@/api/mes/masterdata/workshop'
export default {
  name: 'form',
  components: { MaterialSelect },
  dicts: ['currency','bill_status','qc_type'],
  data() {
    return {
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSalOrderEntry: [],
      // 表单参数
      form: {
        planFinishDate: new Date(),
        createBy:'',
        materialNumber : '',
        materialId : '',
        materialName:'',
        materialUnitId : '',
        materialUnitName : '',
        specification : '',
        proStatus:'A',
        status:'A',
        quantity:1,
        workOrderEntryList: [],
      },
      // 表单校验
      rules: {
        quantity:[
          {required: true, message: "生产数量", trigger: "blur"}
        ],
        number: [
          { required: true, message: "单号不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
        materialName: [
          {
            required: true,
            message: '产品不能为空',
            trigger: 'blur',
          },
        ],
        firstQcQuantity:[{
          required: true,
          pattern:/^[1-9]*[1-9][0-9]*$/g,
          message: '请输入大于等于0的数字',
          trigger: "blur"
        }],
      },
      //页面类型，false新增，true编辑
      formType: false,
      userList: [],
      //车间选项
      workshopOptions:[],
      //工位选项
      stationOptions:[],
    };
  },
  created() {
    this.formType=false;
    //this.getUserList();
    //this.getCustomerList();
    this.getUser();
    this.getRouteList();
    this.getWorkshops();
    this.getStation();
    const id = this.$route.params && this.$route.params.id;
    if ( typeof(id) !='undefined' ){
      this.getForm(id);
    }
  },
  methods: {
    getForm(id) {
      this.formType=true;
      getWorkOrder(id).then(response => {
        this.form = response.data;
        this.form.workOrderEntryList = response.data.workOrderEntryList;
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {

        if (valid) {
          let list = this.form.workOrderEntryList;
          if(list.length<1){
            this.$modal.msgWarning("请录入订单明细");
            return ;
          }

          if (this.form.id != null) {
            updateWorkOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              const obj = { path: "/pro/workOrder" };
              this.$tab.closeOpenPage(obj);
            });
          } else {
            addWorkOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              const obj = { path: "/pro/workOrder" };
              this.$tab.closeOpenPage(obj);
            });
          }
        }
      });
    },

    getUser() {
      getUserProfile().then(response => {
        this.form.createBy= response.data.nickName;
      });
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
    //物料选择弹出框
    handleSelectProduct(){
      this.$refs.materialSelect.showFlag = true;
    },
    //接收物料弹框返回参数
    onMaterialSelect(obj){
      if(obj != undefined && obj != null){
        if(obj.routeId==null){
          this.$modal.msgWarning("该物料未绑定工序路线,请先进行绑定");
          return ;
        }
        getRoute(obj.routeId).then(response => {
          let routeProcessList = response.data.routeProcessList;
          if(routeProcessList==null ||routeProcessList.length<1){
            this.$modal.msgWarning("该物料未绑定工序路线,请先进行绑定");
            return ;
          }
          this.form.workOrderEntryList = [];
          for (let i = 0; i < routeProcessList.length; i++) {
            let routeProcess = routeProcessList[i];
            let obj = {};
            obj.sort = routeProcess.sort;
            obj.routeId = routeProcess.routeId;
            obj.processId = routeProcess.processId;
            obj.processName = routeProcess.processName;
            obj.qcType = routeProcess.qcType;
            obj.firstQcQuantity = routeProcess.firstQcQuantity;
            obj.stationId ='';
            obj.stationName ='';
            obj.workshopId ='';
            obj.workshopName ='';
            //获取默认车间和工位
            let queryParams ={processIds: obj.processId,pageSize: 1,status: 0 }
            listStation(queryParams).then( response => {
              if(response.rows.length>0){
                obj.stationId =response.rows[0].id;
                obj.stationName =response.rows[0].name;
                obj.workshopId =response.rows[0].workshopId;
                obj.workshopName =response.rows[0].workshopName;
              }
            });
            this.form.workOrderEntryList.push(obj);
          }
        });
        this.form.materialNumber = obj.number;
        this.form.materialId = obj.id;
        this.form.materialName = obj.name;
        this.form.materialUnitId = obj.unitId
        this.form.materialUnitName = obj.unitName;
        this.form.specification = obj.specification;
        this.$refs["form"].validate();
      }
    },
    /** 查询工序路线列表 */
    getRouteList() {
      listRoute({pageSize:999999}).then(response => {
        this.routeList = response.rows;
      });
    },
    //查询车间信息
    getWorkshops(){
      listWorkshop({pageSize:999999}).then( response => {
        this.workshopOptions = response.rows;
        console.log("this.workshopOptions=========>",this.workshopOptions)
      });
    },
    //查询工位信息
    getStation(){
      listStation({pageSize:999999}).then( response => {
        this.stationOptions = response.rows;
      });
    },
    workshopSelectChange(value,row){
      //获取下拉框中label的值
      let currentRow = row;
      let res = this.workshopOptions.find((item)=>{
        return item.id === value;
      });
      currentRow.workshopName = res.name;
      currentRow.stationId='';
      //返回对应的车间的工位
      let queryParams ={workshopId: res.id,pageSize: 999999,status: 0 }
      listStation(queryParams).then( response => {
        this.stationOptions = response.rows;
      });
      this.$refs["form"].validate();
    },
    stationSelectChange(value,row){
      //获取下拉框中label的值
      let currentRow = row;
      let res = this.stationOptions.find((item)=>{
        return item.id === value;
      });
      currentRow.stationName = res.name;
      //返回对应的车间的工位
      getStation(res.id).then( response => {
        console.log("response=====>",response)
        currentRow.workshopId =response.data.workshopId;
        currentRow.workshopName = response.data.workshopName;
      });
      this.$refs["form"].validate();
    },
  }
}
</script>

