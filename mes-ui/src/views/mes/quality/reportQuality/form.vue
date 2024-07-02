<template>
  <!-- 添加或修改报工质检单对话框 -->
  <el-dialog title="质检单" :visible.sync="open" width="950px" append-to-body :close-on-click-modal="false">
    <el-form ref="reportQualityForm" :model="reportQuality" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="8">
          <el-form-item label="工单号" prop="workOrderNumber">
            <el-input v-model="reportQuality.workOrderNumber" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="报工单号" prop="reportNumber">
            <el-input v-model="reportQuality.reportNumber" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="批次号" prop="batchNumber">
            <el-input v-model="reportQuality.batchNumber" placeholder="请输入批次号" :disabled="true"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="产品名称" prop="materialName">
            <el-input v-model="reportQuality.materialName" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="工序" prop="processName">
            <el-input v-model="reportQuality.processName" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="质检类型" prop="qcType">
            <el-select v-model="reportQuality.qcType" :disabled="true">
              <el-option
                v-for="dict in dict.type.qc_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="质检单号" prop="number">
            <el-input v-model="reportQuality.number" placeholder="不输入则自动生成" :disabled="qualityDisabled"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="报工时间" prop="reportTime">
            <el-date-picker clearable
                            v-model="reportQuality.reportTime"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="请选择报工时间"
                            :disabled="true">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="质检员" prop="checkUserId">
            <el-select filterable v-model="reportQuality.checkUserId" filterable placeholder="请选择质检员"
                       @change="checkUserChange($event)" :disabled="qualityDisabled">
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
        <el-col :span="8">
          <el-form-item label="质检时间" prop="checkTime">
            <el-date-picker :clearable="false"
                            v-model="reportQuality.checkTime"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="请选择检测时间" :disabled="qualityDisabled">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="质检数量" prop="quantity">
            <el-input v-model="reportQuality.quantity"  />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item label="备注" >
            <el-input v-model="reportQuality.remark" placeholder="请输入备注"  type="textarea"
                      :rows="2" :disabled="qualityDisabled"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="合格数" prop="qualifiedQuantity">
            <el-input v-model="reportQuality.qualifiedQuantity" placeholder="请输入合格数量" :disabled="qualityDisabled"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="不合格数" prop="unqualifiedQuantity">
            <el-input v-model="reportQuality.unqualifiedQuantity" placeholder="请输入不合格数量" :disabled="qualityDisabled"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="3">
          <el-button style="float: right; padding: 3px 0" type="text" @click="addReportQualityEntry('qualified')" v-show="qualityDisabled==true?false:true">+合格等级</el-button>
        </el-col>
        <el-col :span="12">
          <el-button style="float: right; padding: 3px 0" type="text" @click="addReportQualityEntry('unQualified')" v-show="qualityDisabled==true?false:true">+不合格等级</el-button>
        </el-col>
      </el-row>

      <el-row :gutter="8" >
        <!--     合格品区域   begin     -->
        <el-col :span="12"  >
          <div class="bg-purple-light">
          <div v-for="(item,index) in tmpQualityLevelList" :key="index">
            <el-card class="box-card" style="margin-bottom: 6px;">
              <div slot="header" class="clearfix">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="" prop="qcType" label-width="20">
                      <el-select v-model="item.levelId"  filterable  placeholder="请选择等级" style="width: 152px" :disabled="qualityDisabled">
                        <el-option
                          v-for="item in qualityLevelList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label=""  label-width="0">
                      <el-input v-model="item.quantity" placeholder="请输入数量" style="width: 100px" :disabled="qualityDisabled"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-button style="float: right; padding: 3px 0" type="text" @click="clickReason('qualified',index)" v-show="qualityDisabled==true?false:true">添加原因</el-button>
                    <el-button style="float: right; padding: 3px 0" type="text" @click="deleteQualityEntry('qualified',index)" v-show="qualityDisabled==true?false:true">移除等级</el-button>
                  </el-col>
                </el-row>
              </div>
              <div v-for="(reasonItem,reasonIndex) in item.reasonList" :key="index">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="" prop="qcType" label-width="20" >
                      <el-select v-model="reasonItem.reasonId"  style="width: 150px" :disabled="qualityDisabled">
                        <el-option
                          v-for="item in reasonList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10">
                    <el-form-item label=""  label-width="0">
                      <el-input v-model="reasonItem.quantity" placeholder="请输入数量" style="width: 120px" :disabled="qualityDisabled"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="4">
                    <el-button type="danger" icon="el-icon-delete" circle style="margin-left: 0px" @click="deleteReason('qualified',index,reasonIndex)" v-show="qualityDisabled==true?false:true"></el-button>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </div>
        </div>
        </el-col>

        <!--     合格品区域   end     -->
        <!--     不合格品区域   begin     -->
        <el-col :span="12" >
          <div class="bg-purple-light">
          <div v-for="(item,index) in tmpUnQualityLevelList" :key="index">
            <el-card class="box-card" style="margin-bottom: 6px;">
              <div slot="header" class="clearfix">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="" prop="qcType" label-width="20">
                      <el-select v-model="item.levelId"  filterable  placeholder="请选择等级" style="width: 152px" :disabled="qualityDisabled">
                        <el-option
                          v-for="item in unQualityLevelList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label=""  label-width="0">
                      <el-input v-model="item.quantity" placeholder="请输入数量" style="width: 100px" :disabled="qualityDisabled"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-button style="float: right; padding: 3px 0" type="text" @click="clickReason('unQualified',index)" v-show="qualityDisabled==true?false:true">添加原因</el-button>
                    <el-button style="float: right; padding: 3px 0" type="text" @click="deleteQualityEntry('unQualified',index)" v-show="qualityDisabled==true?false:true">移除等级</el-button>
                  </el-col>
                </el-row>
              </div>
              <div v-for="(reasonItem,reasonIndex) in item.reasonList" :key="index">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="" prop="qcType" label-width="20" >
                      <el-select v-model="reasonItem.reasonId"  style="width: 150px" :disabled="qualityDisabled">
                        <el-option
                          v-for="item in unReasonList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10">
                    <el-form-item label=""  label-width="0">
                      <el-input v-model="reasonItem.quantity" placeholder="请输入数量" style="width: 120px" :disabled="qualityDisabled"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="4">
                    <el-button type="danger" icon="el-icon-delete" circle style="margin-left: 0px" @click="deleteReason('unQualified',index,reasonIndex)" v-show="qualityDisabled==true?false:true"></el-button>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </div>
          </div>
        </el-col>
        <!--     不合格品区域   end     -->
      </el-row>

    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitReportQualityForm" v-show="qualityDisabled==true?false:true">保存</el-button>
      <el-button @click="reportQualityCancel" >取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import addressJson from '@/components/mes/address.json'
import { listUser,deptTreeSelect } from '@/api/system/user'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import { listReport, getReport, delReport, addReport, updateReport,batchSave } from "@/api/mes/production/report";
import { getWorkOrder,getWorkOrderByEntryId } from '@/api/mes/production/workOrder'
import { getStation, listStation } from '@/api/mes/masterdata/station'
import { listWorkshop } from '@/api/mes/masterdata/workshop'
import { listQualityLevel, getQualityLevel, delQualityLevel, addQualityLevel, updateQualityLevel } from "@/api/mes/quality/qualityLevel";
import { listReason, getReason, delReason, addReason, updateReason } from "@/api/mes/quality/reason";
import { addReportQuality,listReportQuality,getReportQuality } from "@/api/mes/quality/reportQuality";

export default {
  name: 'reportQualityForm',
  dicts: ['production_status','report_channel','sys_yes_no','qc_type'],
  data() {
    return {

      //质检单
      reportQuality:{
      },
      // 是否显示弹出层
      open: false,

      // 表单校验
      rules: {
        reportUserId: [
          { required: true, message: "报工人必选", trigger: "blur" }
        ],
        reportTime: [
          { required: true, message: "报工时间必填", trigger: "blur" }
        ],
        reportQuantity: [
          {required: true,
            pattern:/^[1-9]*[1-9][0-9]*$/g,
            message: '请输入大于0的数字',
            trigger: "blur" }
        ],
        workshopId: [
          { required: true, message: "车间必选", trigger: "blur" }
        ],
        stationId: [
          { required: true, message: "工位必选", trigger: "blur" }
        ],
        checkUserId: [
          { required: true, message: "质检员必选", trigger: "blur" }
        ],
        quantity: [
          {required: true,
            pattern:/^\d+(\.\d+)?$/g,
            message: '请输入数字',
            trigger: "blur" }
        ],
        qualifiedQuantity: [
          {required: true,
            pattern:/^\d+(\.\d+)?$/g,
            message: '请输入数字',
            trigger: "blur" }
        ],
        unqualifiedQuantity: [
          {required: true,
            pattern:/^\d+(\.\d+)?$/g,
            message: '请输入数字',
            trigger: "blur" }
        ],
      },
      userList: [],
      //车间选项
      workshopOptions:[],
      //工位选项
      stationOptions:[],
      //质检合格等级列表
      qualityLevelList:[],
      //质检不合格等级列表
      unQualityLevelList:[],
      //质检原因合格列表
      reasonList:[],
      //质检原因不合格列表
      unReasonList:[],
      tmpQualityLevelList:[],
      tmpUnQualityLevelList:[],
      qualityDisabled:false
    };
  },
  activated() {
    this.qualityAddClick()
  },
  created() {
    //新增
    this.getUserList();
    this.getWorkshops();
    this.getStation();
  },
  methods: {
    /** 获取用户数据*/
    getUserList(){
      let qp =   {pageSize : 99999} ;
      listUser(this.addDateRange(qp, [])).then(response => {
          this.userList = response.rows;
        }
      );
    },

    userChange(value,row){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      let currentRow = row;
      currentRow.reportUserName=opt.nickName;
    },
    checkUserChange(value){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      this.reportQuality.checkUserName=opt.nickName;
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
      this.$refs["reportQualityForm"].validate();
    },
    stationSelectChange(value,row){
      //获取下拉框中label的值
      let currentRow = row;
      let res = this.stationOptions.find((item)=>{
        return item.id === value;
      });
      currentRow.stationName = res.name;
      this.$refs["reportQualityForm"].validate();
    },

    //通过报工单点击质检单新增按钮
    qualityAddClick(row){

      if( typeof (row.isShow)=="undefined"||row.isShow==true){
        this.qualityDisabled=false;
      }else{
        this.qualityDisabled=true;
      }

      //获取下拉框数据
      listQualityLevel({pageSize:99999,status:'0'}).then(response => {
        let list = [];
        list =response.rows;
        //区分合格和不合格等级
        for (let i = 0; i < list.length; i++) {
          if("A"===list[i].type){
            this.qualityLevelList.push(list[i]);
          }
          if("B"===list[i].type){
            this.unQualityLevelList.push(list[i]);
          }
        }
      });
      listReason({pageSize:99999,status:'0' } ).then(response => {
        let list = [];
        list =response.rows;
        //区分合格和不合格等级
        for (let i = 0; i < list.length; i++) {
          if("A"===list[i].type){
            this.reasonList.push(list[i]);
          }
          if("B"===list[i].type){
            this.unReasonList.push(list[i]);
          }
        }
      });

      //通过报工单ID查其质检单
      listReportQuality({pageSize:99999,reportIds:row.id}).then(qualityResp =>{
        this.tmpQualityLevelList=[];
        this.tmpUnQualityLevelList=[];
        if(qualityResp.rows.length>0){
          this.qualityDisabled=true;
          let list =qualityResp.rows[0].qualityLevelList;
          for (let i  = 0; i < list.length; i++) {
            if(list[i].type==="A"){
              this.tmpQualityLevelList.push(list[i]);
            }
            if(list[i].type==="B"){
              this.tmpUnQualityLevelList.push(list[i]);
            }
          }
          let rq = qualityResp.rows[0];
          // 表单数据初始化
          this.reportQuality = rq;
        }else{
          // this.$modal.msgWarning("不存在质检单");
          // return;
          // 表单数据初始化
          this.reportQuality = {
            reportIds: row.id,
            reportNumber: row.number,
            workOrderNumber: row.workOrderNumber,
            processName: row.processName,
            materialNumber: row.materialNumber,
            materialName: row.materialName,
            specification: row.specification,
            materialUnitName: row.materialUnitName,
            batchNumber: row.batchNumber,
            qcType: row.qcType,
            quantity: 1,
            qualifiedQuantity: 1.,
            unqualifiedQuantity: 0,
            reportTime: row.reportTime,
            checkTime: new Date().format("yyyy-MM-dd HH:mm:ss"),
            checkUserId: '',
            checkUserName: '',
            status: '0',
            qualityLevelList:[]
          };
        }
        this.resetForm("reportQualityForm");
        this.open = true;
      })
    },
    //详情
    qualityInfo(id){
      this.qualityDisabled=true;
      //获取下拉框数据
      listQualityLevel({pageSize:99999,status:'0'}).then(response => {
        let list = [];
        list =response.rows;
        //区分合格和不合格等级
        for (let i = 0; i < list.length; i++) {
          if("A"===list[i].type){
            this.qualityLevelList.push(list[i]);
          }
          if("B"===list[i].type){
            this.unQualityLevelList.push(list[i]);
          }
        }
      });
      listReason({pageSize:99999,status:'0' } ).then(response => {
        let list = [];
        list =response.rows;
        //区分合格和不合格等级
        for (let i = 0; i < list.length; i++) {
          if("A"===list[i].type){
            this.reasonList.push(list[i]);
          }
          if("B"===list[i].type){
            this.unReasonList.push(list[i]);
          }
        }
      });
      getReportQuality(id).then(quantityRes => {
        this.reportQuality = quantityRes.data;
        this.tmpQualityLevelList=[];
        this.tmpUnQualityLevelList=[];
        let list =this.reportQuality.qualityLevelList;
        for (let i  = 0; i < list.length; i++) {
          if(list[i].type==="A"){
            this.tmpQualityLevelList.push(list[i]);
          }
          if(list[i].type==="B"){
            this.tmpUnQualityLevelList.push(list[i]);
          }
        }
        this.resetForm("reportQualityForm");
        this.open = true;
      })

    },
    // 取消按钮
    reportQualityCancel() {
      this.reportQuality={};
      this.qualityLevelList=[];
      this.unQualityLevelList=[];
      this.reasonList=[];
      this.unReasonList=[];
      this.tmpQualityLevelList=[];
      this.tmpUnQualityLevelList=[];
      this.resetForm("reportQualityForm");
      this.open = false;
      this.qualityDisabled=false;
    },
    addReportQualityEntry(type){
      //合格
      if(type==="qualified"){
        this.tmpQualityLevelList.push({
          quantity:1,
          type:'A',
          reasonList:[
          ]
        })
      }else{
        //不合格
        this.tmpUnQualityLevelList.push({
          quantity:1,
          type:'B',
          reasonList:[
          ]
        })
      }
      this.$forceUpdate();
    },
    clickReason(type,index){
      //合格
      if(type==="qualified"){
        this.tmpQualityLevelList[index].reasonList.push({
          quantity:1
        })

      }else{
        //不合格
        this.tmpUnQualityLevelList[index].reasonList.push({
          quantity:1
        })
      }
      this.$forceUpdate();
    },
    deleteQualityEntry(type,index){
      if(type==="qualified"){
        this.tmpQualityLevelList.splice(index,1);
      }else{
        this.tmpUnQualityLevelList.splice(index,1);
      }
    },
    deleteReason(type,index,reasonIndex){
      if(type==="qualified"){
        this.tmpQualityLevelList[index].reasonList.splice(reasonIndex,1);
      }else{
        this.tmpUnQualityLevelList[index].reasonList.splice(reasonIndex,1);
      }
    },
    //质检单新增提交
    submitReportQualityForm(){
      this.$refs["reportQualityForm"].validate(valid => {
        if (valid) {
          this.reportQuality.qualityLevelList=[];
          for (let i = 0; i < this.tmpQualityLevelList.length; i++) {
            //给等级名称赋值
            let opt= {};
            opt= this.qualityLevelList.find((item)=>{
              return item.id === this.tmpQualityLevelList[i].levelId;
            });
            this.tmpQualityLevelList[i].levelName=opt.name;
            //给原因名称赋值
            let reasonList = this.tmpQualityLevelList[i].reasonList;
            for (let j = 0; j < reasonList.length; j++) {
              let optB= {};
              optB=this.reasonList.find((item)=>{
                return item.id === reasonList[j].reasonId;
              });
              reasonList[j].reasonName =optB.name;
            }
            this.reportQuality.qualityLevelList.push(this.tmpQualityLevelList[i])
          }
          for (let i = 0; i < this.tmpUnQualityLevelList.length; i++) {
            //给等级名称赋值
            let opt= {};
            opt= this.unQualityLevelList.find((item)=>{
              return item.id === this.tmpUnQualityLevelList[i].levelId;
            });
            this.tmpUnQualityLevelList[i].levelName=opt.name;
            //给原因名称赋值
            let reasonList = this.tmpUnQualityLevelList[i].reasonList;
            for (let j = 0; j < reasonList.length; j++) {
              let optB= {};
              optB=this.unReasonList.find((item)=>{
                return item.id === reasonList[j].reasonId;
              });
              reasonList[j].reasonName =optB.name;
            }

            this.reportQuality.qualityLevelList.push(this.tmpUnQualityLevelList[i])
          }

          addReportQuality(this.reportQuality).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.reportQualityCancel();
            }
          );

        }
      });

    }
  }
}
</script>

<style scoped>
.bg-purple-light {
  border-radius: 4px;
  min-height: 36px;
  background: #FFFFFF;
}
</style>
