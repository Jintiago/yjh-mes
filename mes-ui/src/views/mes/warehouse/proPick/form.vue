<template>
  <div class="app-container">
    <!-- 添加或修改销售订单对话框 -->
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">

      <el-row>
        <el-col :span="8">
          <el-form-item label="出库单号" prop="number">
            <el-input v-model="form.number" placeholder="不输入可自动生成" style="width: 220px"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="领料仓库" prop="warehouseId">
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

<!--      <el-form-item label="工位名称" prop="stationName">-->
<!--        <el-input v-model="form.stationName" placeholder="请输入工位名称" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="车间" prop="workshopName">-->
<!--        <el-input v-model="form.workshopName" placeholder="请输入车间名称" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="工单子表IDs" prop="workOrderEntryIds">-->
<!--        <el-input v-model="form.workOrderEntryIds" placeholder="请输入工单子表IDs" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="状态" prop="status">-->
<!--        <el-select v-model="form.status" placeholder="请选择状态">-->
<!--          <el-option-->
<!--            v-for="dict in dict.type.bill_status"-->
<!--            :key="dict.value"-->
<!--            :label="dict.label"-->
<!--            :value="dict.value"-->
<!--          ></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="审核人" prop="checker">-->
<!--        <el-input v-model="form.checker" placeholder="请输入审核人" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="审核日期" prop="checkDate">-->
<!--        <el-date-picker clearable-->
<!--                        v-model="form.checkDate"-->
<!--                        type="date"-->
<!--                        value-format="yyyy-MM-dd"-->
<!--                        placeholder="请选择审核日期">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->

      <el-row>
        <el-col :span="8">
          <el-form-item label="领料时间" prop="pickTime">
            <el-date-picker clearable
                            v-model="form.pickTime"
                            type="date"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="请选择领料时间">
            </el-date-picker>
          </el-form-item>

        </el-col>
        <el-col :span="8">
          <el-form-item label="领料人" prop="pickUserId">
            <el-select v-model="form.pickUserId" clearable filterable  placeholder="请选择领料人" @change="pickUserChange">
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
          <el-form-item label="仓管员" prop="keeperId">


            <el-select v-model="form.keeperId" clearable filterable  placeholder="请选择仓管员" @change="keeperChange">
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
        <el-col :span="8">
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="center">生产领料单子表信息</el-divider>
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
      <el-table :data="form.proPickEntryList"  @selection-change="handleSelectionChange" ref="entryTable">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" prop="sort" width="50"  >
          <template slot-scope="scope">
            <el-form-item label-width="0"  style="margin-bottom: 12px;">
              <el-input v-model="scope.row.sort"  :disabled="true" />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="产品名"  width="240" >
          <template slot-scope="scope">
            <el-form-item   :prop="'proPickEntryList.'+scope.$index+'.materialName'" :rules="rules.materialName" label-width="0"  style="margin-bottom: 12px;">
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
                          :prop="'proPickEntryList.'+scope.$index+'.quantity'">

              <el-input v-model="scope.row.quantity"   />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="批次编号" prop="batchNumber" >
          <template slot-scope="scope">
            <el-form-item label-width="0"  style="margin-bottom: 12px;">
              <el-input v-model="scope.row.batchNumber"  />
            </el-form-item>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <MaterialSelect ref="materialSelect" @onMaterialSelect="onMaterialSelect" > </MaterialSelect>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </div>
</template>

<script>
import { listProPick, getProPick, delProPick, addProPick, updateProPick } from "@/api/mes/warehouse/proPick";
import { listUser } from '@/api/system/user'
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import { listWarehouse } from '@/api/mes/warehouse/warehouse'
import { getUserProfile } from "@/api/system/user";
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
        pickTime : new Date().format("yyyy-MM-dd HH:mm:ss"),
        proPickEntryList: [
        ],
      },
      // 表单校验
      rules: {
        number: [],
        pickTime: [
          { required: true, message: "领料日期不能为空", trigger: "blur" }
        ],
        materialName: [
          {
            required: true,
            message: '产品不能为空',
            trigger: 'blur',
          },
        ],
        pickUserId:[
          {
            required: true,
            message: '领料人不能为空',
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
            pattern:/^([1-9]\d*(\.\d*[1-9][0-9])?)|(0\.\d*[1-9][0-9])|(0\.\d*[1-9])$/g,
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
      warehouseList:[],
    };
  },
  created() {
    this.formType=false;
    this.getUserList();
    this.getWarehouseList();
    const id = this.$route.params && this.$route.params.id;
    if ( typeof(id) !='undefined' ){
      this.getForm(id);
    }
  },
  methods: {
    getForm(id) {
      this.formType=true;
      getProPick(id).then(response => {
        this.form = response.data;
        this.form.proPickEntryList = response.data.proPickEntryList;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let list = this.form.proPickEntryList;
          if(list.length<1){
            this.$modal.msgWarning("请录入领料单明细");
            return ;
          }

          if (this.form.id != null) {
            updateProPick(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              const obj = { path: "/wm/proPick" };
              this.$tab.closeOpenPage(obj);
            });
          } else {
            addProPick(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              const obj = { path: "/wm/proPick" };
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
        for (let i = 0; i < this.form.proPickEntryList.length; i++) {
          if(this.form.proPickEntryList[i].sort>tmp){
            tmp=this.form.proPickEntryList[i].sort;
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
      this.form.proPickEntryList.push(obj);
      this.$refs.entryTable.sort("sort","ascending")
    },

    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteEntry() {
      if (this.checkedEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的数据");
      } else {
        const proPickEntryList = this.form.proPickEntryList;
        const checkedEntry = this.checkedEntry;
        this.form.proPickEntryList = proPickEntryList.filter(function(item) {
          return checkedEntry.indexOf(item.sort) == -1
        });
        console.log("this.checkedEntry========>",this.checkedEntry)
        for (let i = 0; i <this.form.proPickEntryList.length; i++) {
          this.form.proPickEntryList[i].sort=i+1
        }
        this.maxIndex=this.form.proPickEntryList.length;
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
        // let entryList = this.form.proPickEntryList;
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
      let proPickEntryList = this.form.proPickEntryList;
      for (let i = 0; i <proPickEntryList.length; i++) {
        let tmpSort =proPickEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedEntry[0]){
          //与上一个交换顺序
          tmp =this.form.proPickEntryList[i-1];
          proPickEntryList[i-1]=proPickEntryList[i]
          proPickEntryList[i-1].sort =tmpSort-1
          proPickEntryList[i]=tmp;
          proPickEntryList[i].sort=tmpSort;
        }
      }
      this.form.proPickEntryList=proPickEntryList;
      this.$refs.entryTable.sort("sort","ascending")
      this.checkedEntry[0]=checkedEntry[0]-1;
    },
    sortDown(){
      const checkedEntry = this.checkedEntry;
      if(checkedEntry.length>1||checkedEntry.length==0){
        this.$modal.msgError("请选择一条数据");
        return;
      }
      let proPickEntryList = this.form.proPickEntryList;
      if(checkedEntry[0]===proPickEntryList.length){
        this.$modal.msgError("已经在最下面啦");
        return;
      }
      for (let i = 0; i <proPickEntryList.length; i++) {
        let tmpSort =proPickEntryList[i].sort;
        let tmp ={};
        if(tmpSort===checkedEntry[0]){
          //与下一个交换顺序
          tmp =this.form.proPickEntryList[i+1];
          proPickEntryList[i+1]=proPickEntryList[i]
          proPickEntryList[i+1].sort =tmpSort+1
          proPickEntryList[i]=tmp;
          proPickEntryList[i].sort=tmpSort;
        }
      }
      this.form.proPickEntryList=proPickEntryList;
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
    pickUserChange(value){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      this.form.pickUserName =opt.nickName;
    },
    keeperChange(value){
      let opt= {};
      opt= this.userList.find((item)=>{
        return item.userId === value;
      });
      this.form.keeperName =opt.nickName;
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

      console.log("warehouse======>",warehouse)
    },
  }
}
</script>

