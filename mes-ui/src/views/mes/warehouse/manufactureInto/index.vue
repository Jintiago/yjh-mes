<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="入库单编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入入库单编号"
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
      <el-form-item label="物料名称"  >
        <el-input
          v-model="queryParams.params.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['warehouse:manufactureInto:add']"
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
          v-hasPermi="['warehouse:manufactureInto:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['warehouse:manufactureInto:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="manufactureIntoList" @selection-change="handleSelectionChange" height="650">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="入库单编号" align="center" prop="manufactureInto.number" width="100"/>
      <el-table-column label="入库日期" align="center" prop="manufactureInto.intoDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.manufactureInto.intoDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交货人" align="center" prop="manufactureInto.delivererName" width="80"/>
      <el-table-column label="仓库名称" align="center" prop="manufactureInto.warehouseName" width="80"/>
      <el-table-column label="物料编号" align="center" prop="materialNumber" width="100"/>
      <el-table-column label="物料名称" align="center" prop="materialName" width="140"/>
      <el-table-column label="规格型号" align="center" prop="specification" width="160"/>
      <el-table-column label="数量" align="center" prop="quantity" width="85">
        <template slot-scope="scope">
          {{scope.row.quantity}} ({{scope.row.materialUnitName}})
        </template>
      </el-table-column>
<!--      <el-table-column label="状态" align="center" prop="manufactureInto.status" />-->
      <el-table-column label="备注" align="center" prop="manufactureInto.remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row.mainId)"
            v-hasPermi="['warehouse:manufactureInto:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['warehouse:manufactureInto:remove']"
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
import { listManufactureInto, getManufactureInto, delManufactureInto, addManufactureInto, updateManufactureInto } from "@/api/mes/warehouse/manufactureInto";
import { listWarehouse } from "@/api/mes/warehouse/warehouse";

export default {
  name: "ManufactureInto",
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
      // 完工入库单表格数据
      manufactureIntoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 更新时间时间范围
      daterangeIntoDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        orderByColumn:null,
        isAsc:null,
        number: null,
        warehouseNumber: null,
        warehouseId: null,
        intoDate: null,
        status: null,
        params:{
          materialName:null,
        }
      },
      // 表单参数
      form: {},
      warehouseList:[],
    };
  },
  created() {
    this.getWarehouseList();
    this.getList();
  },
  methods: {
    /** 查询完工入库单列表 */
    getList() {
      this.loading = true;
      if (null != this.daterangeIntoDate && '' != this.daterangeIntoDate) {
        this.queryParams.params["beginIntoDate"] = this.daterangeIntoDate[0];
        this.queryParams.params["endIntoDate"] = this.daterangeIntoDate[1];
      }else{
        delete this.queryParams.params["beginIntoDate"];
        delete this.queryParams.params["endIntoDate"];
      }
      listManufactureInto(this.queryParams).then(response => {
        this.manufactureIntoList = response.rows;
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
      this.$router.push({ path: "/mes/manufactureInto-add/index" });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row
      this.$router.push({ path: "/mes/manufactureInto-edit/index/"+id });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.mainId || this.ids;
      this.$modal.confirm('此动作将删除整张单据，且不可恢复，请谨慎操作!!!').then(function() {
        return delManufactureInto(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('warehouse/manufactureInto/export', {
        ...this.queryParams
      }, `manufactureInto_${new Date().getTime()}.xlsx`)
    },
    /** 获取计量单位数据*/
    getWarehouseList(){
      let qp =  {pageSize:99999};
      listWarehouse(qp).then(response => {
        this.warehouseList = response.rows;
      });
    },
  }
};
</script>
