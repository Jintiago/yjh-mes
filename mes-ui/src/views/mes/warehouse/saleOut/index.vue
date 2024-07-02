<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="出库单编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入出库单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出货仓库" prop="warehouseName">
        <el-input
          v-model="queryParams.warehouseName"
          placeholder="请输入仓库名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出库时间" prop="outTime">
        <el-date-picker
          v-model="queryParams.outTimeQuery"
          type="daterange"
          unlink-panels
          value-format="yyyy-MM-dd"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">

        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['warehouse:saleOut:add']"
        >新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['warehouse:saleOut:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['warehouse:saleOut:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="saleOutList" @selection-change="handleSelectionChange" class="tableBox">
      <el-table-column type="selection" width="55" align="center" />
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
            <span v-else-if="item.type==='annex'">
                   <el-link type="primary" @click="fileDownload(scope.row)">{{fieldFormat(scope.row,item)!=null?'下载':''}}</el-link>
            </span>
            <span v-else>{{fieldFormat(scope.row,item)}}</span>
          </template>
        </el-table-column>
      </div>
<!--      <el-table-column label="副单位" align="center" prop="deputyUnit" />-->
<!--      <el-table-column label="副单位数量" align="center" prop="deputyUnitQuantity" />-->
<!--      <el-table-column label="开票标记" align="center" prop="saleOut.invoiceMark" >-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.invoice_mark" :value="scope.row.saleOut.invoiceMark"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="备注" align="center" prop="saleOut.remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row.mainId)"
            v-hasPermi="['warehouse:saleOut:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['warehouse:saleOut:remove']"
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

  </div>
</template>

<script>
import { listSaleOut, getSaleOut, delSaleOut, addSaleOut, updateSaleOut } from "@/api/mes/warehouse/saleOut";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import {dictFormat, fieldFormat} from "@/utils/jinzhong";
export default {
  name: "SaleOut",
  dicts: ['bill_status','invoice_mark','procure_method'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],

      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 销售出库单主表表格数据
      saleOutList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 时间范围
      daterangeOutTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        customerName: null,
        invoiceMark: null,
        method: null,
        customerId: null,
        number: null,
        warehouseId: null,
        warehouseName: null,
        keeperId: null,
        keeperName: null,
        outTime: null,
        checker: null,
        checkDate: null,
        status: null,
        outTimeQuery:[],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        number: [
          { required: true, message: "入库单编号不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        createBy: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      },
      entryFieldExtendList:{},
    };
  },
  created() {
    this.getFieldExtendList();
    this.getList();
  },
  methods: {
    /** 查询销售出库单主表列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeOutTime && '' != this.daterangeOutTime) {
        this.queryParams.params["beginOutTime"] = this.daterangeOutTime[0];
        this.queryParams.params["endOutTime"] = this.daterangeOutTime[1];
      }
      listSaleOut(this.queryParams).then(response => {
        this.saleOutList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getFieldExtendList(){
      //获取销售订单子表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'saleOutEntry'}).then(response => {
        this.entryFieldExtendList = response.rows;
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
        customerName: null,
        logisticsInfo: null,
        invoiceMark: null,
        method: null,
        customerId: null,
        shipLocation: null,
        number: null,
        workshopId: null,
        workshopName: null,
        stationId: null,
        stationName: null,
        workOrderEntryIds: null,
        warehouseId: null,
        warehouseNumber: null,
        warehouseName: null,
        regionId: null,
        regionNumber: null,
        regionName: null,
        areaId: null,
        areaNumber: null,
        areaName: null,
        keeperId: null,
        keeperName: null,
        outTime: null,
        checker: null,
        checkDate: null,
        status: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        snArray: null
      };
      this.saleOutEntryList = [];
      this.resetForm("form");
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
      this.ids = selection.map(item => item.mainId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({ path: "/mes/saleOut-add/index" });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({ path: "/mes/saleOut-edit/index/"+row });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delSaleOut(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('warehouse/saleOut/export', {
        ...this.queryParams
      }, `saleOut_${new Date().getTime()}.xlsx`)
    },
    fieldFormat(row,item){
      return fieldFormat(row,item);
    },
    dictFormat(item){
      return dictFormat(item);
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
</style>
