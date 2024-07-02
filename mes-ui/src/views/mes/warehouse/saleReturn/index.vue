<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库名称" prop="warehouseName">
        <el-input
          v-model="queryParams.warehouseName"
          placeholder="请输入仓库名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入库日期">
        <el-date-picker
          v-model="daterangeIntoDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['warehouse:saleReturn:add']"
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
          v-hasPermi="['warehouse:saleReturn:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['warehouse:saleReturn:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="saleReturnList" @selection-change="handleSelectionChange">
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
<!--      <el-table-column label="物料名称" align="center" prop="materialName" />-->
<!--      <el-table-column label="物料编号" align="center" prop="materialNumber" />-->
<!--      <el-table-column label="规格型号" align="center" prop="specification" />-->
<!--      <el-table-column label="单位" align="center" prop="materialUnitName" />-->
<!--      <el-table-column label="数量" align="center" prop="quantity" />-->
<!--      <el-table-column label="入库日期" align="center" prop="saleReturn.intoDate" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.saleReturn.intoDate, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="仓库" align="center" prop="saleReturn.warehouseName" />-->
<!--      <el-table-column label="状态" align="center" prop="saleReturn.status" />-->
<!--      <el-table-column label="备注" align="center" prop="saleReturn.remark" />-->

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row.mainId)"
            v-hasPermi="['warehouse:saleReturn:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['warehouse:saleReturn:remove']"
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
import { listSaleReturn, getSaleReturn, delSaleReturn, addSaleReturn, updateSaleReturn } from "@/api/mes/warehouse/saleReturn";
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import {dictFormat, fieldFormat} from "@/utils/jinzhong";
export default {
  name: "SaleReturn",
  dicts: ['bill_status'],
  data() {
    return {
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
      // 销售退货单表格数据
      saleReturnList: [],
      // $comment时间范围
      daterangeIntoDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        number: null,
        customerName: null,
        warehouseName: null,
        intoDate: null,
        status: null,
      },
      entryFieldExtendList:{},
    };
  },
  created() {
    this.getFieldExtendList();
    this.getList();
  },
  methods: {
    /** 查询销售退货单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeIntoDate && '' != this.daterangeIntoDate) {
        this.queryParams.params["beginIntoDate"] = this.daterangeIntoDate[0];
        this.queryParams.params["endIntoDate"] = this.daterangeIntoDate[1];
      }
      listSaleReturn(this.queryParams).then(response => {
        this.saleReturnList = response.rows;
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
      this.daterangeIntoDate = [];
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
      this.$router.push({ path: "/mes/saleReturn-add/index" });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({ path: "/mes/saleReturn-edit/index/"+row });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除？').then(function() {
        return delSaleReturn(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('warehouse/saleReturn/export', {
        ...this.queryParams
      }, `saleReturn_${new Date().getTime()}.xlsx`)
    },
    getFieldExtendList(){
      //获取销售订单子表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'saleReturnEntry'}).then(response => {
        this.entryFieldExtendList = response.rows;
        this.loading = false;
      });
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
