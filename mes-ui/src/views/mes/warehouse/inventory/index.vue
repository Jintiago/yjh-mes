<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格型号" prop="specification">
        <el-input
          v-model="queryParams.specification"
          placeholder=""
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库" >
        <el-select v-model="queryParams.warehouseId" clearable filterable  placeholder="请选择仓库">
          <el-option
            v-for="item in warehouseList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <!--   天润项目定制化功能begin   -->
      <el-form-item label="厚度区间" >
        <el-input
          v-model="queryParams.params.hdqj"
          placeholder="例如：3.5-8"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--   天润项目定制化功能end  -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inventoryList" show-summary :summary-method="getSummaries">
      <el-table-column label="仓库" align="center" prop="warehouseName" />
      <el-table-column label="物料" align="center" prop="materialName" />
      <el-table-column label="物料编号" align="center" prop="materialNumber" />
      <el-table-column label="规格型号" align="center" prop="specification" />
      <!--  天润项目定制功能Begin    -->
      <el-table-column label="粘度" align="center" prop="customFieldA" />
      <el-table-column label="印刷版" align="center" prop="customFieldB" />
      <!--  天润项目定制功能End  -->
      <el-table-column label="单位" align="center" prop="materialUnitName" />
      <el-table-column label="数量" align="center" prop="quantity" />

      <!--  动态属性        -->
<!--      <div v-for="(item,index) in entryFieldExtendList" >-->
<!--        <el-table-column :label="item.fieldName" align="center"  :width="item.width" show-overflow-tooltip='true' >-->
<!--          <template slot-scope="scope">-->
<!--            <span >{{fieldFormat(scope.row,item)}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--      </div>-->
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
import { listInventory } from "@/api/mes/warehouse/inventory";
import { listWarehouse } from '@/api/mes/warehouse/warehouse'
import { listFieldExtend } from "@/api/mes/system/fieldExtend";
import {dictFormat, fieldFormat} from "@/utils/jinzhong";
export default {
  name: "Inventory",
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
      // 即时库存表格数据
      inventoryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        warehouseName: null,
        materialName: null,
        specification: null,
        params:{}
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      warehouseList:[],
      entryFieldExtendList:{},
    };
  },
  created() {
    this.getFieldExtendList();
    this.getWarehouseList();
  },
  mounted(){
    this.getList();
  },
  methods: {
    /** 查询即时库存列表 */
    getList() {
      this.loading = true;
      listInventory(this.queryParams).then(response => {
        this.inventoryList = response.rows;
        this.total = response.total;
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
        quantity: null,
        warehouseId: null,
        warehouseName: null,
        materialId: null,
        materialNumber: null,
        materialName: null,
        materialUnitName: null
      };
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
    /** 获取计量单位数据*/
    getWarehouseList(){
      let qp =  {pageSize:99999};
      listWarehouse(qp).then(response => {
        this.warehouseList = response.rows;
      });
    },
    getSummaries(param){
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        if("materialNumber" === column.property ||"specification" === column.property||"materialName" === column.property){
          sums[index] = '-';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        console.log("values===>",values)
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0);
        } else {
          sums[index] = '-';
        }
      });
      return sums;
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('warehouse/inventory/export', {
        ...this.queryParams
      }, `即时库存${new Date().getTime()}.xlsx`)
    },
    fieldFormat(row,item){
      return fieldFormat(row,item);
    },
    getFieldExtendList(){
      //获取销售订单子表扩展字段结构
      listFieldExtend({pageSize: 99999,sourceBill:'realTimeInventory'}).then(response => {
        this.entryFieldExtendList = response.rows;
        this.loading = false;
      });
    },
  }
};
</script>
