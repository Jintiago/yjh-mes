<!-- 根据bom及工艺路线半自动生成计划排产页面 -->
<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-table  :data="form.workOrderList"
                 v-loading="loading"
                 element-loading-text="排产计划生产中,请耐心等待"
                 element-loading-spinner="el-icon-loading"
                 element-loading-background="rgba(0, 0, 0, 0.8)"
                 row-key="materialId"
                 :default-expand-all="isExpandAll"
                 :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                 :row-style="tableRowClassName"
      >
        <el-table-column  label="BOM展开" align="center" width="80" />
        <el-table-column label="工单编号" align="center"  width="110" >
          <template slot-scope="scope">
            <el-form-item   label-width="0" style="margin-bottom: 7px;" >
              <el-input  style="width: 105px" v-model="scope.row.number"  placeholder="可自动生成"/>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="产品编号" align="center" prop="materialNumber" />
        <el-table-column label="产品名称" align="center" prop="materialName" />
        <el-table-column label="批次号" align="center" prop="batchNumber" >
          <template slot-scope="scope">
            <el-form-item   label-width="0" style="margin-bottom: 7px;" >
              <el-input  style="width: 105px" v-model="scope.row.batchNumber"  />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="单位" align="center" prop="materialUnitName" width="50" />
        <el-table-column label="生产数量" align="center"  >
          <template slot-scope="scope">
            <el-form-item v-if="scope.row.parentIndex==null" label-width="0" style="margin-bottom: 12px;"
                          :rules="rules.quantity"
                          :prop="'workOrderList.'+scope.row.index+'.quantity'">
              <el-input   style="width: 105px" v-model="scope.row.quantity" />
            </el-form-item>
            <!--当改工单存在BOM清单时，begin-->
            <el-form-item  v-else label-width="0" style="margin-bottom: 12px;"
                           :rules="rules.quantity"
                           :prop="'workOrderList.'+scope.row.parentIndex+'.children.'+scope.row.index+'.quantity'">
              <el-input   style="width: 105px" v-model="scope.row.quantity"
                          />
            </el-form-item>
            <!--当改工单存在BOM清单时，end-->
          </template>
        </el-table-column>
        <el-table-column label="库存" align="center" prop="inventory" width="50"/>
        <el-table-column label="计划完成日期" align="center"  width="160">
          <template slot-scope="scope">
            <el-form-item v-if="scope.row.parentIndex==null" label-width="0"  style="margin-bottom: 7px;"
                          :prop="'workOrderList.'+scope.row.index+'.planFinishDate'">
              <el-date-picker  :clearable="false" v-model="scope.row.planFinishDate" type="date" value-format="yyyy-MM-dd" style="width: 140px" />
            </el-form-item>
            <!--当改工单存在BOM清单时，begin-->
            <el-form-item  v-else label-width="0" style="margin-bottom: 12px;"
                           :prop="'workOrderList.'+scope.row.parentIndex+'.children.'+scope.row.index+'.planFinishDate'">
              <el-date-picker :clearable="false" v-model="scope.row.planFinishDate" type="date" value-format="yyyy-MM-dd" style="width: 140px" />
            </el-form-item>
            <!--当改工单存在BOM清单时，end-->
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center"  >
          <template slot-scope="scope">
            <el-form-item   label-width="0" style="margin-bottom: 7px;" >
              <el-input   v-model="scope.row.remark"  />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              v-hasPermi="['production:workOrder:edit']"
              @click="dispatchButtonClick(scope.row)"
            >生产派工</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['production:workOrder:remove']"
            >移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary"  style="margin-top: 20px;"  @click="closeButton">关闭</el-button>
      <el-button type="primary"  style="margin-top: 20px;"  @click="handleAdd" :disabled="buttonDisabled">保存工单</el-button>
    </div>

    <el-dialog title="生产派工" :visible.sync="dialogTableVisible" :close-on-click-modal="false" width="700px">
      <el-table :data="dispatchRow.workOrderEntryList">
        <el-table-column label="排序" prop="sort"  width="70" align="center" />
        <el-table-column label="工序" prop="processName"  align="center"/>
        <el-table-column label="车间" prop="workshopId"  >
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
        <el-table-column label="工位" prop="stationId">
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="dispatchSaveClick">保存</el-button>
      </div>
    </el-dialog>

  </div>


</template>

<script>

import { addWorkOrder, batchAddWorkOrder, listWorkOrderPreview, updateWorkOrder } from '@/api/mes/production/workOrder'
import Treeselect from "@riophae/vue-treeselect";
import MaterialSelect from '@/components/mes/materialSelect'
import { delSaleOrder } from '@/api/mes/sale/saleOrder'
import { listWorkshop } from '@/api/mes/masterdata/workshop'
import { getStation, listStation } from '@/api/mes/masterdata/station'
export default {
  name: "workOrderPreview",
  dicts: ['production_status'],
  components: {
    Treeselect,
  },
  data() {
    return {

      // 是否展开，默认全部展开
      isExpandAll: false,

      // 工单表格数据
      form: {
        workOrderList: [{
          quantity: null,
          children: [{}]
        }],
      },

      // 表单校验
      rules: {
        quantity: [
          { required: true,
            pattern: /^\d+(\.\d+)?$/g,
            message: "生产数量不能为空",
            trigger: "blur"
          }
        ],
      },
      loading: true,
      dialogTableVisible :false,
      dispatchRow:{},
      //车间选项
      workshopOptions:[],
      //工位选项
      stationOptions:[],
      buttonDisabled:false,
    };
  },
  created() {
    this.form.workOrderList =[];
    this.getList();
    this.getStation();
    this.getWorkshops();
  },
  methods: {
    /** 查询工单列表 */
    getList() {
      let billType =  this.$route.params.billType;
      let ids =  this.$route.params.ids;
      if(billType===undefined||ids===undefined||ids==null||ids===""){
        this.$confirm('此页面无任何可生成的排产计划,请重新操作', '提示', {
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          const obj = { path: "/index" };
          this.$tab.closeOpenPage(obj);
        })
      }
      let params = {ids: ids,billType: billType}
      listWorkOrderPreview(params).then(response => {
        this.form.workOrderList=response.data;
        this.loading =false;
      }).catch(()=>{
        this.loading =false;
        let path ="/index";
        if(billType==="saleOrder"){
          path="/sal/saleOrder"
        }
        const obj = { path: path };
        this.$tab.closeOpenPage(obj);
      });

    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonDisabled=true;
          batchAddWorkOrder(this.form.workOrderList).then(response => {
              //二次确认弹框
              this.$modal.confirm('工单新增成功,是否跳转至工单页?', '提示', {
                confirmButtonText: '跳转',
                cancelButtonText: '返回',
                type: 'warning'
              }).then(() => {
                const obj = { path: "/pro/workOrder" };
                this.$tab.closeOpenPage(obj);
              }).catch(() => {
                this.closeButton();
              });
            });
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      //二次确认弹框
      this.$modal.confirm('是否确认移除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.formatList(row,false);
        if(this.form.workOrderList.length<1){
          this.closeButton();
        }
      }).catch(() => {});

    },

    formatList(row,value){
      if(row.parentIndex==null){
        this.form.workOrderList[row.index].searchValue=value;
        if(this.form.workOrderList[row.index].children.length>0){
          for (let i = 0; i < this.form.workOrderList[row.index].children.length; i++) {
            this.formatList(this.form.workOrderList[row.index].children[i],value)
          }
        }
      }else{
        this.form.workOrderList[row.parentIndex].children[row.index].searchValue=value;
      }
    },
    tableRowClassName({row,rowIndex} ){
      if(row.searchValue==false){
        let styleRes = {
          "display": "none !important"
        }
        return styleRes;
      }
    },
    closeButton(){
      let billType =  this.$route.params.billType;
      let path ="/index";
      if(billType==="saleOrder"){
        path="/sal/saleOrder"
      }
      const obj = { path: path };
      this.$tab.closeOpenPage(obj);
    },

    dispatchButtonClick(row){
      this.getStation();
      this.dispatchRow =row;
      this.dialogTableVisible = true
    },
    dispatchSaveClick(){
      if(this.dispatchRow.parentIndex==null){
        this.form.workOrderList[this.dispatchRow.index].workOrderEntryList= this.dispatchRow.workOrderEntryList;
      }else{
        this.form.workOrderList[this.dispatchRow.parentIndex].children[this.dispatchRow.index]=this.dispatchRow.workOrderEntryList;
      }
      this.dialogTableVisible = false;
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
};
</script>

