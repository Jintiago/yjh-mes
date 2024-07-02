<!-- 根据不同工序定制排产的生产工单页面 -->
<template>
  <keep-alive>
    <div class="app-container">
      <el-row>
        <div style="text-align: center;margin-bottom: 20px">排产工序：{{ process.name }}   <el-button style="margin-left: 20px" type="primary" @click="openProcessPlan" size="mini">切换</el-button></div>
        <!--  工单主表    -->
        <el-form ref="form" :model="form" :rules="rules"  style="margin-top: 20px">
            <el-row>
              <el-col :span="4">
                <el-form-item  label="优先级"  :rules="rules.priority" >
                    <el-input-number v-model="form.priority"  :min="1" :max="10" label="请输入数字，1为最高级"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item  label="产品"  :rules="rules.materialName" style="margin-left: 20px">
                  <el-input v-model="form.materialName" placeholder="产品为必填项">
                    <el-button slot="append" @click="handleSelectProduct(form)"
                               icon="el-icon-search"></el-button>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="型号"  :rules="rules.specification" style="margin-left: 20px"
                >
                  <el-input v-model="form.specification"/>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="数量"  :rules="rules.quantity" style="margin-left: 20px"
                >
                  <el-input v-model="form.quantity"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="计划完成日期"  :rules="rules.planFinishDate"
                >
                  <el-date-picker :clearable="false" v-model="form.planFinishDate" type="date"
                                  value-format="yyyy-MM-dd" style="width: 140px"/>
                </el-form-item>

              </el-col>

              <!--            <el-table-column label="附件" align="center" :width="120">-->
              <!--              <template slot-scope="scope">-->
              <!--                <el-link type="primary" @click="fileDownload(scope.row)">下载</el-link>-->
              <!--              </template>-->
              <!--            </el-table-column>-->
              <el-col :span="18">
                <el-form-item label="备注"  >
                  <el-input v-model="form.remark"/>
                </el-form-item>
              </el-col>
            </el-row>

        </el-form>
      </el-row>
      <el-row>
        <el-dialog title="选择工序" :visible.sync="dialogFormVisible">

          <el-select v-model="process.id" placeholder="请选择">
            <el-option
              v-for="item in processList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="selectProcess">确 定</el-button>
          </div>
        </el-dialog>
        <el-tabs v-model="activeName">
          <el-tab-pane label="领料" name="first">
            <el-button type="primary" @click="entryAddClick('pick')">新增</el-button>
            <el-form ref="pickForm" :model="form" :rules="rules" label-width="80px" style="margin-top: 20px">
              <el-table :data="form.pickEntryList"
                        height="300"
              >
                <el-table-column label="领取物料" width="240">
                  <template slot-scope="scope">
                    <el-form-item :prop="'pickEntryList.'+scope.$index+'.pickMaterialName'" :rules="rules.materialName"
                                  label-width="0" style="margin-bottom: 12px;">
                      <el-input v-model="scope.row.pickMaterialName" placeholder="产品为必填项">
                        <el-button slot="append" @click="handleSelectProduct(scope.row)"
                                   icon="el-icon-search"></el-button>
                      </el-input>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="规格型号" align="center">
                  <template slot-scope="scope">
                    <el-form-item label-width="0" style="margin-bottom: 7px;"
                                  :rules="rules.specification"
                                  :prop="'pickEntryList.'+scope.$index+'.pickSpecification'">
                      <el-input v-model="scope.row.pickSpecification"/>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="领取数量" align="center">
                  <template slot-scope="scope">
                    <el-form-item label-width="0" style="margin-bottom: 7px;" :rules="rules.quantity"
                                  :prop="'pickEntryList.'+scope.$index+'.pickQuantity'">
                      <el-input v-model="scope.row.pickQuantity"/>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="备注" align="center">
                  <template slot-scope="scope">
                    <el-form-item label-width="0" style="margin-bottom: 7px;">
                      <el-input v-model="scope.row.remark"/>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="handleDelete( scope.$index,'pick',scope.row)"
                      v-hasPermi="['production:workOrder:remove']"
                    >移除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="报工" name="second">
            <el-button type="primary" @click="entryAddClick('report')">新增</el-button>
            <el-form ref="reportForm" :model="form" :rules="rules" label-width="80px" style="margin-top: 20px">
              <el-table :data="form.reportEntryList"
              >
                <el-table-column label="报工物料" width="180">
                  <template slot-scope="scope">
                    <el-form-item :prop="'reportEntryList.'+scope.$index+'.reportMaterialName'"
                                  :rules="rules.materialName" label-width="0" style="margin-bottom: 12px;">
                      <el-input v-model="scope.row.reportMaterialName" placeholder="产品为必填项">
                        <el-button slot="append" @click="handleSelectProduct(scope.row)"
                                   icon="el-icon-search"></el-button>
                      </el-input>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="规格型号" align="center" width="180">
                  <template slot-scope="scope">
                    <el-form-item label-width="0" style="margin-bottom: 7px;"
                                  :rules="rules.specification"
                                  :prop="'reportEntryList.'+scope.$index+'.reportSpecification'">
                      <el-input v-model="scope.row.reportSpecification"/>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="报工数量" align="center" width="80">
                  <template slot-scope="scope">
                    <el-form-item label-width="0" style="margin-bottom: 7px;" :rules="rules.quantity"
                                  :prop="'reportEntryList.'+scope.$index+'.reportQuantity'">
                      <el-input v-model="scope.row.reportQuantity"/>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="工位" align="center" width="120">
                  <template slot-scope="scope">
                    <el-form-item label-width="0" style="margin-bottom: 7px;" :rules="rules.station"
                                  :prop="'reportEntryList.'+scope.$index+'.stationId'">
                      <el-select v-model="scope.row.stationId" placeholder="请选择工位"
                                 @change="stationSelectChange($event,scope.row)" style="width: 100%">
                        <el-option
                          v-for="item in stationOptions"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="设备" prop="stationId" width="120">
                  <template slot-scope="scope">
                    <el-form-item label-width="0" style="margin-bottom: 7px;" :rules="rules.machine"
                                  :prop="'reportEntryList.'+scope.$index+'.machineId'">
                      <el-select v-model="scope.row.machineId" placeholder="请选择设备"
                                 @change="machineSelectChange($event,scope.row)" style="width: 100%">
                        <el-option
                          v-for="item in equipmentOptions"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </template>
                </el-table-column>
                <!--   子表自定义扩展字段     -->
                <div v-if="entryFieldExtendList.length==0">
                  <el-table-column label="备注" align="center">
                    <template slot-scope="scope">
                      <el-form-item label-width="0" style="margin-bottom: 7px;" :prop="'reportEntryList.'+scope.$index+'.remark'">
                        <el-input v-model="scope.row.remark"/>
                      </el-form-item>
                    </template>
                  </el-table-column>
                </div>
                <div v-for="(item,index) in entryFieldExtendList">
                    <el-table-column :label="item.fieldName"   width="120">
                      <template slot-scope="scope">
                        <el-form-item label-width="0"  style="margin-bottom: 12px;"
                                      :prop="'reportEntryList.'+scope.$index+'.extendFieldJson[item.field]'">
                          <el-input v-model="scope.row.extendFieldJson[item.field]"   />
                        </el-form-item>
                      </template>
                    </el-table-column>
                  <div v-if="index===entryFieldExtendList.length-1">
                    <el-table-column label="备注" align="center" width="120">
                      <template slot-scope="scope">
                        <el-form-item label-width="0" style="margin-bottom: 7px;" :prop="'reportEntryList.'+scope.$index+'.remark'">
                          <el-input v-model="scope.row.remark"/>
                        </el-form-item>
                      </template>
                    </el-table-column>
                  </div>
                </div>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="handleDelete(scope.$index,'report')"
                      v-hasPermi="['production:workOrder:remove']"
                    >移除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
          </el-tab-pane>

        </el-tabs>
        <MaterialSelect ref="materialSelect" @onMaterialSelect="onMaterialSelect"></MaterialSelect>
        <el-dialog
          title="下载"
          :visible.sync="dialogVisible">
          <span v-for="(item,index) in fileList">
            <el-row>
              <a :href="item" target="_blank">{{item}}</a>
            </el-row>
          </span>
        </el-dialog>
      </el-row>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" style="margin-top: 20px;" @click="closeButton">关闭</el-button>
        <el-button type="primary" style="margin-top: 20px;" @click="handleAdd" :disabled="buttonDisabled">保存工单
        </el-button>
      </div>
    </div>

  </keep-alive>
</template>

<script>

import {
  addWorkOrder,
  batchAddWorkOrder,
  listWorkOrderPreview,
  updateWorkOrder,
  batchAddWorkOrderByJson, getWorkOrder,
} from '@/api/mes/production/workOrder'
import Treeselect from "@riophae/vue-treeselect";
import MaterialSelect from '@/components/mes/materialSelect'
import {delSaleOrder, listEntryByIds} from '@/api/mes/sale/saleOrder'
import {listWorkshop} from '@/api/mes/masterdata/workshop'
import {getStation, listStation} from '@/api/mes/masterdata/station'
import {getProcess, listProcess} from "@/api/mes/production/process";
import {listEquipment} from "@/api/mes/equipment/equipment";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import { listWarehouse } from '@/api/mes/warehouse/warehouse'
export default {
  name: "workOrderformA",
  dicts: ['production_status'],
  components: {
    Treeselect, MaterialSelect
  },
  data() {
    return {

      // 工单主表集合数据
      form: {
        reportEntryList: [],
        pickEntryList: []
      },

      // 表单校验
      rules: {
        quantity: [
          {
            required: true,
            pattern: /^[1-9]*[1-9][0-9]*$/g,
            message: "请输入生产数量",
            trigger: "blur"
          }
        ],
        priority:[
          {
            required: true,
            pattern: /^[1-9]*[1-9][0-9]*$/g,
            message: "请输入优先级",
            trigger: "blur"
          }
        ],
        specification: [
          {
            required: true,
            message: "请填写规格型号",
            trigger: "blur"
          }
        ],
        materialName: [
          {
            required: true,
            message: "请选中物料",
            trigger: "blur"
          }
        ],
        station: [
          {
            required: true,
            message: "请选择",
            trigger: "change"
          }
        ],
        machine: [
          {
            required: true,
            message: "请选择",
            trigger: "change"
          }
        ],
      },
      //车间选项
      workshopOptions: [],
      //工位选项
      stationOptions: [],
      buttonDisabled: false,
      activeName: 'first',
      process: {},
      currentRow: "",
      equipmentOptions: [],
      dialogVisible: false,
      fileList:[],
      entryFieldExtendList:{},
      warehouseList:[],
      dialogFormVisible: false,
      processList:[]
    };
  },
  created() {
    this.getEquipmentList();
    this.getStation();
    const id = this.$route.params && this.$route.params.id;
    if ( typeof(id) !='undefined' ){
      this.getForm(id);
    }else{
      this.initForm();
    }
  },
  methods: {
    initForm(){
      let workOrder = {};
      workOrder.materialId = '';
      workOrder.materialNumber = '';
      workOrder.materialUnitId = '';
      workOrder.materialUnitName = '';
      workOrder.quantity = 1;
      workOrder.priority = 1;
      workOrder.remark = '';
      workOrder.sourceInfo = ''
      workOrder.materialName = '';
      workOrder.specification = '';
      workOrder.proStatus = 'A';
      workOrder.annex = '';
      workOrder.pickEntryList=[];
      workOrder.reportEntryList=[];
      this.form =workOrder;
    },
    getForm(id) {
      this.formType=true;
      getWorkOrder(id).then(response => {
        this.form = response.data;
        let entryList =  response.data.workOrderEntryList;
        getProcess(entryList[0].processId).then(res =>{
          this.process=res.data;
          this.getFieldExtend();
        })
        let pickEntryList =[]
        let reportEntryList =[]
        for (let j = 0; j < entryList.length; j++) {
          let entry = entryList[j];
          if (entry.type === 'pick') {
            pickEntryList.push(entry);
          }
          if (entry.type === 'report') {
            reportEntryList.push(entry)
          }
        }
        this.form.pickEntryList=pickEntryList;
        this.form.reportEntryList = reportEntryList;
      });
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$refs["pickForm"].validate(validB => {
            if (validB) {
              this.$refs["reportForm"].validate(validC => {
                if (validC) {
                  //this.buttonDisabled = true;
                  this.form.workOrderEntryList =this.form.pickEntryList.concat(this.form.reportEntryList);

                  for (let i = 0; i < this.form.workOrderEntryList.length; i++) {
                    this.form.workOrderEntryList[i].processId=this.process.id;
                    this.form.workOrderEntryList[i].processName=this.process.name;
                  }
                  if (this.form.id != null) {
                    updateWorkOrder(this.form).then(response => {
                      this.$modal.msgSuccess("修改成功");
                      const obj = { path: "/pro/workOrder" };
                      this.$tab.closeOpenPage(obj);
                    });
                  } else {
                    addWorkOrder(this.form).then(response => {
                      //二次确认弹框
                      this.$modal.msgSuccess("保存成功");
                      const obj = {path: "/pro/workOrder"};
                      this.$tab.closeOpenPage(obj);
                    }).catch(() => {
                      this.$message.error(response.mas)
                    });
                  }
                }
              })
            }
          })
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(index, type, row) {
      if (type === "pick") {
        this.form.pickEntryList.splice(index, 1);
      }
      if (type === "report") {
        this.form.reportEntryList.splice(index, 1);
      }
    },

    tableRowClassName({row, rowIndex}) {
      row.index = rowIndex;
      row.sort = rowIndex + 1;
    },
    closeButton() {
      let billType = this.$route.params.billType;
      let path = "/index";
      if (billType === "saleOrder") {
        path = "/sal/saleOrder"
      }
      const obj = {path: path};
      this.$tab.closeOpenPage(obj);
    },
    //查询车间信息
    getWorkshops() {
      listWorkshop({pageSize: 999999}).then(response => {
        this.workshopOptions = response.rows;
        console.log("this.workshopOptions=========>", this.workshopOptions)
      });
    },
    //查询工位信息
    getStation() {
      listStation({pageSize: 999999}).then(response => {
        this.stationOptions = response.rows;
      });
    },
    workshopSelectChange(value, row) {
      //获取下拉框中label的值
      let currentRow = row;
      let res = this.workshopOptions.find((item) => {
        return item.id === value;
      });
      currentRow.workshopName = res.name;
      currentRow.stationId = '';
      //返回对应的车间的工位
      let queryParams = {workshopId: res.id, pageSize: 999999, status: 0}
      listStation(queryParams).then(response => {
        this.stationOptions = response.rows;
      });
      this.$refs["form"].validate();
    },
    machineSelectChange(value, row) {
      //获取下拉框中label的值
      let currentRow = row;
      let res = this.equipmentOptions.find((item) => {
        return item.id === value;
      });
      currentRow.machineName = res.name;

    },
    entryAddClick(type) {
      if(JSON.stringify(this.process)=='{}'){
        this.$modal.msgWarning("请先选择工序")
        return;
      }
      let entry = {
        pickQuantity: 0,
        reportQuantity: 0,
        processId: this.process.id,
        processName: this.process.name,
        remark: "",
        type: type,
        qcType: 'D',
        pickMaterialId : "",
        pickMaterialName : "",
        pickSpecification : "",
        reportMaterialId : "",
        reportMaterialName : "",
        reportSpecification : "",
        machineId : "",
        machineName : "",
        stationId  : "",
        stationName : ""
      }
      entry.extendFieldJson = {};
      for (let i = 0; i < this.entryFieldExtendList.length; i++) {
        var field =this.entryFieldExtendList[i].field;
        entry.extendFieldJson[field]='';
      }
      if (type === "pick") {
        this.form.pickEntryList.push(entry);
        this.$refs["pickForm"].validate();
      }
      if (type === "report") {
        this.form.reportEntryList.push(entry);
        this.$refs["reportForm"].validate();
      }
      listWarehouse({pageSize:99999}).then(response => {
        this.warehouseList = response.rows;
      });
    },
    //物料选择弹出框
    handleSelectProduct(row) {
      this.currentRow = row;
      this.$refs.materialSelect.showFlag = true;
    },
    //接收物料弹框返回参数
    onMaterialSelect(obj) {
      let row = this.currentRow;
      if (row.type === "pick") {
        row.pickMaterialId = obj.id;
        row.pickMaterialName = obj.name;
      }
     else if(row.type === "report") {
        row.reportMaterialId = obj.id;
        row.reportMaterialName = obj.name;
      }else{
       this.form.materialId=obj.id;
        this.form.materialName=obj.name;
        this.form.materialNumber = obj.number;
        this.form.materialUnitId=obj.unitId;
        this.form.materialUnitName = obj.unitName;
      }
      this.$refs["form"].validate();
    },
    getEquipmentList() {
      listEquipment({pageSize: 99999, status: '0'}).then(res => {
        this.equipmentOptions = res.rows;
      })
    },

    stationSelectChange(value, row) {
      //获取下拉框中label的值
      let currentRow = row;
      let res = this.stationOptions.find((item) => {
        return item.id === value;
      });
      currentRow.stationName = res.name;
      this.$refs["form"].validate();
    },
    warehouseSelect(value, row) {
      //获取下拉框中label的值
      let currentRow = row;
      let res = this.warehouseList.find((item) => {
        return item.id === value;
      });
      currentRow.inWarehouseName = res.name;
    },
    fileDownload(row) {
      let annex = row.annex;
      this.fileList=annex.split(",");
      this.dialogVisible=true
    },
    selectProcess(){
      let processId = this.process.id;
      getProcess(processId).then(response => {
        this.process = response.data;
        this.getFieldExtend();
      });
      this.dialogFormVisible=false;
    },
    getFieldExtend(){
      //扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'process-'+this.process.id}).then(response => {
        this.entryFieldExtendList = response.rows;
      });
    },
    openProcessPlan(){
      listProcess({pageNum: 1,pageSize: 999999 ,status: '0'} ).then(response => {
        this.processList = response.rows;
      });
      this.dialogFormVisible=true;
    },

  }
};
</script>
<style>


</style>

