<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="合同编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入销售单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="销售员" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入销售员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称"  >
        <el-input
          v-model="queryParams.params.materialName"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生产状态" prop="statusArr">
        <el-select v-model="queryParams.statusArr" placeholder="请选择生产状态" clearable multiple>
          <el-option
            v-for="dict in dict.type.salorder_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="销售部门" prop="deptId" style="margin-left: 10px">-->
<!--        <treeselect v-model="queryParams.deptId" :options="deptOptions" :show-count="true" placeholder="请选择销售部门" style="width: 240px" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="物流单号" prop="logisticsNumber"  >-->
<!--        <el-input-->
<!--          v-model="queryParams.logisticsNumber"-->
<!--          placeholder="请输入物流单号"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="销售日期"  style="margin-left: 10px" >
        <el-date-picker
          v-model="queryParams.saleDateQuery"
          type="daterange"
          unlink-panels
          value-format="yyyy-MM-dd"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="发货日期"  style="margin-left: 10px" >
        <el-date-picker
          v-model="queryParams.deliveryDateQuery"
          type="daterange"
          unlink-panels
          value-format="yyyy-MM-dd"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item style="margin-left: 20px">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['sale:saleOrder:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['sale:saleOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['sale:saleOrder:export']"
        >导出</el-button>
      </el-col>
<!--   标准计划排产    -->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="workOrderPreview"-->
<!--          :disabled="multiple"-->
<!--        >计划排产</el-button>-->
<!--      </el-col>-->
      <!--   全手工计划排产    -->
      <el-col :span="1.5">
        <el-button
          plain
          style="color: #6978B5;  background-color: #C7CEEB"
          icon="el-icon-download"
          size="mini"
          @click="openProcessPlan"
          :disabled="multiple"
        >工序排产</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="primary"
          size="mini"
          @click="openSalOutForm()"
          :disabled="multiple"
        >销售出库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          style="color: #12737E;  background-color: #24E4BE"
          icon="el-icon-finished"
          size="mini"
          :disabled="single"
          @click="updateStatus('C')"
        >发货完成</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="saleOrderList" @selection-change="handleSelectionChange"
              :row-class-name="tableRowClassName"
              @row-click="onRowClick" highlight-current-row class="tableBox" height="550" >
      <el-table-column type="selection" width="40" align="center" />
      <div v-for="(item,index) in entryFieldExtendList" >
          <el-table-column :label="item.fieldName" align="center"  :width="item.width" show-overflow-tooltip='true' >
            <template slot-scope="scope">
              <!--  动态属性        -->
              <span v-if="item.type==='date'">
                  {{ parseTime(fieldFormat(scope.row,item), '{y}-{m}-{d}') }}
              </span>
              <span v-else-if="item.type.includes('dict:')">
                  <dict-tag :options="dict.type[dictFormat(item)]" :value="fieldFormat(scope.row,item)"/>
              </span>
              <span v-else-if="item.field==='quantity'">
                 已发:{{scope.row.saleOutQuantity==null?0:scope.row.saleOutQuantity}}/{{scope.row.quantity}}
              </span>
              <span v-else>{{fieldFormat(scope.row,item)}}</span>
            </template>
          </el-table-column>
      </div>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row.mainId)"
            v-hasPermi="['sale:saleOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sale:saleOrder:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-table :data="processReportListTMP">
        <div v-for="(item,index) of processReportList">
          <el-table-column :label="item.processName" align="center"  >
            {{item.color==='green'?'生产完成':'生产中'}} ( {{item.schedule}})
          </el-table-column>
        </div>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <el-dialog title="选择工序" :visible.sync="dialogFormVisible">
      <el-select v-model="queryParams.processId" placeholder="请选择">
        <el-option
          v-for="item in processList"
          :key="item.processId"
          :label="item.processName"
          :value="item.processId">
        </el-option>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="workOrderPreview">确 定</el-button>
      </div>
    </el-dialog>

  </div>


</template>

<script>
import {listSaleOrder, delSaleOrder, batchUpdateEntryStatus} from "@/api/mes/sale/saleOrder";
import {queryProcessBySaleOrderEntryId} from "@/api/mes/statement/productionReport"
import { deptTreeSelect } from '@/api/system/user'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import { listProcess } from "@/api/mes/production/process";
import { getRoute } from "@/api/mes/production/route";
import { fieldFormat,dictFormat } from '@/utils/jinzhong'
export default {
  name: "SaleOrder",
  components: { Treeselect,MaterialSelect },
  dicts: ['salorder_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      entryIds: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 销售订单表格数据
      saleOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        number: null,
        statusArr: ['A','B'],
        status:null,
        logisticsNumber: null,
        deptId:null,
        deptName: null,
        userName: null,
        saleDateQuery:[],
        deliveryDateQuery:[],
        customerName: null,
        params:{
          materialName:null,
        }
      },
      pickerOptions: {
        shortcuts: [
          {
            text: "本周",
            onClick(picker) {
              // 获取今天
              const end = new Date();
              // 获取当前周的第一天
              const start = new Date(
                end.getFullYear(),
                end.getMonth(),
                end.getDate() - ((end.getDay() + 6) % 7)
              );
              start.setTime(start.getTime());
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "本月",
            onClick(picker) {
              const end = new Date();
              // 获取当前月的第一天
              const start = new Date(end.getFullYear(), end.getMonth(), 1);
              start.setTime(start.getTime());
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "本年",
            onClick(picker) {
              const end = new Date();
              // 获取当前年的第一天
              const start = new Date(end.getFullYear(), 0, 1);
              start.setTime(start.getTime());
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "上一年",
            onClick(picker) {
              const now = new Date();
              // 获取当前年的第一天
              const start = new Date(now.getFullYear()-1, 0, 1);
              const end = new Date(now.getFullYear()-1, 11, 31);
              start.setTime(start.getTime());
              picker.$emit("pick", [start, end]);
            },
          },
        ]
      },
      // 部门树选项
      deptOptions: undefined,
      entryFieldExtendList:{},
      dialogFormVisible: false,
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      processList :[],
      processReportList:[],
      processReportListTMP:[{}]
    };
  },
  created() {
    this.getList();
    this.getDeptTree();
  },
  methods: {
    /** 查询销售订单列表 */
    getList() {
      this.loading = true;
      const  statusArr = this.queryParams.statusArr;
      this.queryParams.status = statusArr.join(",");
      //获取销售订单子表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'saleOrderEntry'}).then(response => {
        this.entryFieldExtendList = response.rows;
        this.loading = false;
      });

      listSaleOrder(this.queryParams).then(response => {
        this.saleOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {

      this.ids = selection.map(item => item.mainId);
      this.entryIds=selection.map(item => item.id);
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({ path: "/mes/saleOrder-add/index" });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row
      this.$router.push({ path: "/mes/saleOrder-edit/index/"+id });

    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.mainId || this.ids;
      this.$modal.confirm('此动作将删除整张单据，请确认!!!').then(function() {
        return delSaleOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('sale/saleOrder/export', {
        ...this.queryParams
      }, `saleOrder_${new Date().getTime()}.xlsx`)
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data;
      });
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex <=2 || columnIndex >=10) {
        if (rowIndex % 2 === 0) {
          return {
            rowspan: 2,
            colspan: 1
          };
        } else {
          return {
            rowspan: 0,
            colspan: 0
          };
        }
      }
    },
    // 标准半自动排产
    // workOrderPreview(){
    //   //二次确认弹框
    //   this.$modal.confirm('排产计划保存后会生成工单，请仔细核对数据，若有未完成计划请先保存。', '提示', {
    //     confirmButtonText: '确定',
    //     type: 'warning'
    //   }).then(() => {
    //     this.$router.push({name: "workOrderPreview", params: {"ids": this.entryIds.join(),"billType":"saleOrder"}})
    //   })
    // }
    //纯人工排产
    workOrderPreview(){
      let processId =this.queryParams.processId;
      if(typeof(processId)==="undefined"){
        this.$modal.msgWarning("请选择工序");
        return;
      }
      this.dialogFormVisible=false;
      //校验必须选中同一种物料，但可以不同规格型号
      let order = this.saleOrderList.filter( o=>{
        return this.entryIds.includes(o.id) ;
      })
      let materialIdTemp =-1;
      for (let i = 0; i < order.length; i++) {
        if(i==0){
          materialIdTemp= order[i].materialId;
        }
        console.log("materialIdTemp======>",materialIdTemp)
        if(materialIdTemp !=order[i].materialId){
          this.$modal.msgWarning("请选择相同的产品进行排产");
          return;
        }
      }

      //跳转排产页面
      let ll = this.processList.filter(item => item.processId==processId);
      console.log("ll.sort===>",ll)
      this.$router.push({name: "workOrderPreviewA", params:
          {"ids": this.entryIds.join(),"billType":"saleOrder","processId":processId,"processSort":ll[0].sort,"routeId":ll[0].routeId}})

    },
    openProcessPlan(){
      //查询默认工艺路线
      getRoute(9).then(res =>{
        this.processList = res.data.routeProcessList;
      })
      this.dialogFormVisible=true;
    },
    openSalOutForm(){
      //C=已发货,D=已关闭
      for (let i = 0; i < this.entryIds.length; i++) {
        let saleOrder =  this.saleOrderList.filter(item => item.id ==this.entryIds[i]);
        console.log("saleOrder",saleOrder)
        if(saleOrder[0].status =="C"||saleOrder[0].status =="D"){
          this.$modal.msgWarning("已发货和已关闭的订单无法再次发货!请重新勾选!")
          return;
        }
      }
      this.$router.push({ path: "/mes/saleOut-add/index" ,query: {saleOrderIds:this.entryIds}});
    },
    onRowClick(row, event, column){
      console.log("row.id====>",row.id)
      queryProcessBySaleOrderEntryId({id:row.id}  ).then(res =>{
        this.processReportList=res;
        this.processReportListTMP =[{}];
        console.log("res====>",res)
      })
    },
    fieldFormat(row,item){
      return fieldFormat(row,item);
    },
    dictFormat(item){
      return dictFormat(item);
    },
    updateStatus(status){

      let _this = this;
      this.$modal.confirm('此动作将会把订单强制修改为已发货状态，请确认!!!').then(function() {

        const ids =_this.entryIds;
        let params ={}
        params.ids =ids.join(",");
        params.status = status;
        batchUpdateEntryStatus(params).then(res=>{
          _this.$modal.msgSuccess("修改成功");
          _this.getList();
        })
      })
    },
    tableRowClassName({row, rowIndex}) {
      if (row.status === 'B') {
        return 'warning-row';
      }
      return '';
    }
  }
};
</script>
<style lang="scss">
.tableBox {
  th {
    padding: 0 !important;
    height: 15px;
    line-height: 15px;
  }
  td {
    padding: 0 !important;
    height: 15px;
    line-height: 15px;
  }
}
.el-table .warning-row {
  background: #76CBE0 ;
}

</style>
