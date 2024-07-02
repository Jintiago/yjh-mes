<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="出库单编号" prop="saleOutNumber">
        <el-input
          v-model="queryParams.saleOutNumber"
          placeholder="请输入出库单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.bill_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="销售方式" prop="method">
        <el-select v-model="queryParams.method" placeholder="请选择销售方式" clearable>
          <el-option
            v-for="dict in dict.type.procure_method"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格型号" prop="specification">
        <el-input
          v-model="specification"
          placeholder="请输入规格型号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收货车间" prop="shipLocation">
        <el-input
          v-model="queryParams.shipLocation"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开票标记" prop="invoiceMark">
        <el-select v-model="queryParams.invoiceMark" placeholder="请选择开票标记" clearable>
          <el-option
            v-for="dict in dict.type.invoice_mark"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="出库日期">
        <el-date-picker
          v-model="daterangeOutTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
<!--  天润定制功能begin    -->
      <el-form-item label="印刷版" >
        <el-input
          v-model="queryParams.extendFieldJson.banxing"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="粘度" >
        <el-input
          v-model="queryParams.extendFieldJson.viscosity"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--  天润定制功能end    -->
      <el-form-item>
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
<!--          v-hasPermi="['finance:saleOutAccountBill:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['finance:saleOutAccountBill:edit']"
        >详情</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['finance:saleOutAccountBill:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['finance:saleOutAccountBill:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="saleOutAccountBillList" @selection-change="handleSelectionChange" show-summary :summary-method="getSummaries" height="550px">
      <el-table-column type="selection" width="55" align="center" />
      <div v-for="(item,index) in entryFieldExtendList" >
        <el-table-column :label="item.fieldName" align="center"  :width="item.width" show-overflow-tooltip='true' :prop="item.field">
          <template slot-scope="scope">
            <!--  动态属性        -->
            <span v-if="item.type==='date'">
                  {{ parseTime(fieldFormat(scope.row,item), '{y}-{m}-{d}') }}
            </span>
            <span v-else-if="item.type.includes('dict:')">
                  <dict-tag :options="dict.type[dictFormat(item)]" :value="fieldFormat(scope.row,item)"/>
            </span>
            <span v-else>{{fieldFormat(scope.row,item)}}</span>
          </template>
        </el-table-column>
      </div>
<!--      <el-table-column label="开票标记" align="center" prop="saleOutAccountBill.invoiceMark">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.invoice_mark" :value="scope.row.saleOutAccountBill.invoiceMark"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="状态" align="center" prop="saleOutAccountBill.status">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.bill_status" :value="scope.row.saleOutAccountBill.status"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['finance:saleOutAccountBill:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['finance:saleOutAccountBill:remove']"
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

    <!-- 添加或修改销售出库对账单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1450px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="出库单号" prop="saleOutNumber">
              <el-input v-model="form.saleOutNumber" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="客户" prop="customerName">
              <el-input v-model="form.customerName" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="出库日期" prop="outTime">
              <el-date-picker clearable
                              v-model="form.outTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择出库日期" :disabled="true">
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
            <el-form-item label="仓管" prop="keeperName">
              <el-input v-model="form.keeperName" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开票标记" prop="invoiceMark">
              <el-select v-model="form.invoiceMark" placeholder="请选择开票标记">
                <el-option
                  v-for="dict in dict.type.invoice_mark"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="销售方式" prop="method">
              <el-select v-model="form.method" placeholder="请选择销售方式">
                <el-option
                  v-for="dict in dict.type.procure_method"
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
            <el-form-item label="收货仓库" prop="shipLocation">
              <el-input v-model="form.shipLocation" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物流信息" prop="logisticsInfo">
              <el-input v-model="form.logisticsInfo" type="textarea" :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="标准金额" prop="standardAmount">
              <el-input v-model="form.standardAmount" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="销售金额" prop="saleAmount">
              <el-input v-model="form.saleAmount" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="折扣金额" prop="discountAmount">
              <el-input v-model="form.discountAmount" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="折扣率" prop="discountRate">
              <el-input v-model="form.discountRate":disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" type="textarea" :disabled="true"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="center">销售出库对账明细信息</el-divider>
        <el-table :data="saleOutAccountEntryList" :row-class-name="rowSaleOutAccountEntryIndex" @selection-change="handleSaleOutAccountEntrySelectionChange" ref="saleOutAccountEntry">
          <el-table-column type="selection" width="50" align="center" />
          <!--  动态属性        -->
          <template v-for="(item,index) in formEntryFieldExtendList" >
            <el-table-column :label="item.fieldName" align="center"  :width="item.width+50" show-overflow-tooltip='true' >
              <template slot-scope="scope">
                <el-form-item label-width="0"  style="margin-bottom: 12px;"
                              :prop="'saleOutAccountEntryList.'+scope.$index+'.'+formPropFormat(item)"
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
                  <span v-else-if="item.field==='quantity'">
                  <el-input v-model="scope.row.quantity"   @input="countPrice($event,scope.row)"/>
              </span>
              <span v-else-if="item.field==='salePrice'">
                  <el-input v-model="scope.row.salePrice"   @input="countPrice($event,scope.row)"/>
              </span>
                  <span v-else-if="item.field==='saleAmount'">
                  <el-input v-model="scope.row.saleAmount"  :disabled="true" />
              </span>
              <span v-else>
                <el-input v-if="item.isSystem!='Y'&&scope.row.extendFieldJson!=null"  v-model="scope.row.extendFieldJson[item.field]" />
                <el-input v-else v-model="scope.row[item.field]"   />
              </span>
                </el-form-item>
              </template>
            </el-table-column>
          </template>
<!--          <el-table-column label="折扣金额" prop="discountAmount" width="100">-->
<!--            <template slot-scope="scope">-->
<!--              <el-input v-model="scope.row.discountAmount" :disabled="true" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="折扣率" prop="discountRate" >-->
<!--            <template slot-scope="scope">-->
<!--              <el-input v-model="scope.row.discountRate" :disabled="true" />-->
<!--            </template>-->
<!--          </el-table-column>-->
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
import { listSaleOutAccountBill, getSaleOutAccountBill, delSaleOutAccountBill, addSaleOutAccountBill, updateSaleOutAccountBill } from "@/api/mes/finance/saleOutAccountBill";
import {dictFormat, fieldFormat, formPropFormat} from "@/utils/jinzhong";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import MaterialSelect  from "@/components/mes/materialSelect.vue";
export default {
  name: "SaleOutAccountBill",
  components: { MaterialSelect },
  dicts: ['invoice_mark', 'procure_method', 'bill_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSaleOutAccountEntry: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 销售出库对账单表格数据
      saleOutAccountBillList: [],
      // 销售出库对账明细表格数据
      saleOutAccountEntryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 折扣率时间范围
      daterangeOutTime: [],
      specification:null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        number: null,
        status: null,
        method: null,
        customerName: null,
        invoiceMark: null,
        outTime: null,
        saleOutNumber: null,
        extendFieldJson:{}
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        customerName: [
          { required: true, message: "客户名称不能为空", trigger: "blur" }
        ],
        outTime: [
          { required: true, message: "出库日期不能为空", trigger: "blur" }
        ],
        standardAmount: [
          { required: true, message: "标准金额不能为空", trigger: "blur" }
        ],
        saleAmount: [
          { required: true, message: "销售金额不能为空", trigger: "blur" }
        ],
        discountAmount: [
          { required: true, message: "折扣金额不能为空", trigger: "blur" }
        ],
        discountRate: [
          { required: true, message: "折扣率不能为空", trigger: "blur" }
        ],
      },
      entryFieldExtendList:{},
      formEntryFieldExtendList:{},
    };
  },
  created() {
    this.getFieldExtendList();
  },
  mounted(){
    this.getList();
  },
  methods: {
    /** 查询销售出库对账单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeOutTime && '' !== this.daterangeOutTime) {
        this.queryParams.params["beginOutTime"] = this.daterangeOutTime[0];
        this.queryParams.params["endOutTime"] = this.daterangeOutTime[1];
      }
      if(null != this.specification && '' !== this.specification){
        this.queryParams.params["specification"] = this.specification;
      }
      console.log("this.queryParams.extendFieldJson===>",this.queryParams.extendFieldJson)
      if(Object.keys(this.queryParams.extendFieldJson).length !== 0) {
        this.queryParams.params["extendFieldJson"]= JSON.stringify(this.queryParams.extendFieldJson) ;
      }
      listSaleOutAccountBill(this.queryParams).then(response => {
        this.saleOutAccountBillList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getFieldExtendList(){
      //获取销售订单子表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'saleOutAccountEntry'}).then(response => {
        let list =response.rows;
        this.entryFieldExtendList = list;
        this.formEntryFieldExtendList  = list.filter(function(fieldExtend) {
          //过滤掉主表的字段
          let field = fieldExtend.field;
          let arr = field.split(".");
          return arr.length==1;
        })
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
        number: null,
        status: null,
        shipLocation: null,
        method: null,
        customerId: null,
        customerName: null,
        invoiceMark: null,
        logisticsInfo: null,
        keeperId: null,
        keeperName: null,
        checker: null,
        checkDate: null,
        outTime: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        standardAmount: null,
        saleAmount: null,
        discountAmount: null,
        discountRate: null,
        saleOutNumber: null,
        saleOutId: null
      };
      this.saleOutAccountEntryList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeOutTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.mainId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加销售出库对账单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.mainId || this.ids
      getSaleOutAccountBill(id).then(response => {
        this.form = response.data;
        this.saleOutAccountEntryList = response.data.saleOutAccountEntryList;
        this.open = true;
        this.title = "修改销售出库对账单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.saleOutAccountEntryList = this.saleOutAccountEntryList;
          if (this.form.id != null) {
            updateSaleOutAccountBill(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSaleOutAccountBill(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除销售出库对账单编号为"' + ids + '"的数据项？').then(function() {
        return delSaleOutAccountBill(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 销售出库对账明细序号 */
    rowSaleOutAccountEntryIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 销售出库对账明细添加按钮操作 */
    handleAddSaleOutAccountEntry() {
      let obj = {};
      obj.sort = "";
      obj.materialId = "";
      obj.materialNumber = "";
      obj.materialName = "";
      obj.specification = "";
      obj.materialUnitId = "";
      obj.materialUnitName = "";
      obj.quantity = "";
      obj.deputyUnit = "";
      obj.deputyUnitQuantity = "";
      obj.remark = "";
      obj.standardPrice = "";
      obj.standardAmount = "";
      obj.salePrice = "";
      obj.saleAmount = "";
      obj.discountAmount = "";
      obj.discountRate = "";
      this.saleOutAccountEntryList.push(obj);
    },
    /** 销售出库对账明细删除按钮操作 */
    handleDeleteSaleOutAccountEntry() {
      if (this.checkedSaleOutAccountEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的销售出库对账明细数据");
      } else {
        const saleOutAccountEntryList = this.saleOutAccountEntryList;
        const checkedSaleOutAccountEntry = this.checkedSaleOutAccountEntry;
        this.saleOutAccountEntryList = saleOutAccountEntryList.filter(function(item) {
          return checkedSaleOutAccountEntry.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleSaleOutAccountEntrySelectionChange(selection) {
      this.checkedSaleOutAccountEntry = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/saleOutAccountBill/export', {
        ...this.queryParams
      }, `saleOutAccountBill_${new Date().getTime()}.xlsx`)
    },
    fieldFormat(row,item){
      return fieldFormat(row,item);
    },
    dictFormat(item){
      return dictFormat(item);
    },
    formPropFormat(item){
      return formPropFormat(item);
    },
    countPrice(value,row){
      //row.saleAmount = (row.quantity * row.salePrice).toFixed(3);
      //天润定制化功能 begin
        //更新平方数
        if(row.materialUnitName==='卷'){
          // 需要再乘以规格型号中的平方  1.25m*500m*6c
          let str = row.specification;
          // 取到第一个m  下标 4
          let indexA = str.indexOf('m');
          // 取到1.25
          let numA = str.substr(0,indexA)
          //取到  500m*6c
          let strB = str.substr(indexA+2);
          //取到 第二个m下标 3
          let indexB = strB.indexOf("m");
          //取到 500
          let numB =strB.substr(0,indexB);
          //长*宽获得每卷面积
          let pingfang = numA*numB*row.quantity;
          row.extendFieldJson.pingfang =  pingfang
          row.saleAmount = (pingfang * row.salePrice).toFixed(3);
        }else{
          row.saleAmount = (row.extendFieldJson.pingfang * row.salePrice).toFixed(3);
        }
      //天润定制化功能 end
      this.counttotoalPrice();
    },
    //计算主表合计金额
    counttotoalPrice(){
      var ic = 0.00;
      let list = this.form.saleOutAccountEntryList;
      for (let i = 0; i < list.length; i++) {
        if(list[i].saleAmount==='NaN'||typeof (list[i].saleAmount)==='undefined'){
          list[i].saleAmount=0;
        }
        ic=parseFloat(ic)+parseFloat(list[i].saleAmount);
      }
      this.form.saleAmount=ic.toFixed(3);
      this.$forceUpdate();
    },
    //物料选择弹出框
    handleSelectProduct(row){
      this.currentRow = row;
      this.$refs.materialSelect.showFlag = true;
    },
    //接收物料弹框返回参数
    onMaterialSelect(obj){
      if(obj != undefined && obj != null){
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
    getSummaries(param){
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(
          item =>
            Number(
              typeof (item[column.property])=="undefined"?
                item.extendFieldJson[column.property]:
                item[column.property]
            )
        );
        console.log("data===>",data)
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0);
          sums[index]= sums[index].toFixed(2);
        } else {
          sums[index] = '-';
        }
      });
      return sums;
    },
  }
};
</script>

