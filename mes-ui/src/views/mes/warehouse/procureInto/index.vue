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

      <el-form-item label="入库日期" prop="intoDate">
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
      <el-form-item label="供应商"  >
        <el-input
          v-model="queryParams.supplierName"
          placeholder="请输入"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格型号"  >
        <el-input
          v-model="queryParams.params.specification"
          placeholder="请输入"
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
          v-hasPermi="['warehouse:procureInto:add']"
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
          v-hasPermi="['warehouse:procureInto:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['warehouse:procureInto:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="procureIntoList" @selection-change="handleSelectionChange" class="tableBox" height="630px">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="入库单编号" align="center" prop="procureInto.number" width="110"/>
      <el-table-column label="入库日期" align="center" prop="procureInto.intoDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.procureInto.intoDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="仓库" align="center" prop="procureInto.warehouseName" width="75"/>
      <el-table-column label="物料名称" align="center" prop="materialName" width="150"/>
      <el-table-column label="物料编号" align="center" prop="materialNumber" width="80"/>
      <el-table-column label="规格型号" align="center" prop="specification" />
      <el-table-column label="数量" align="center" prop="quantity" width="85">
        <template slot-scope="scope">
            {{scope.row.quantity}} ({{scope.row.materialUnitName}})
        </template>
      </el-table-column>
      <el-table-column label="单价" align="center" prop="price" width="75"/>
      <el-table-column label="金额" align="center" prop="totalPrice" width="75" />
<!--      <el-table-column label="状态" align="center" prop="procureInto.status" />-->
      <el-table-column label="供应商" align="center" prop="procureInto.supplierName" width="190"/>
      <el-table-column label="备注" align="center" prop="procureInto.remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row.mainId)"
            v-hasPermi="['warehouse:procureInto:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['warehouse:procureInto:remove']"
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
import { listProcureInto, getProcureInto, delProcureInto, addProcureInto, updateProcureInto } from "@/api/mes/warehouse/procureInto";
import { listWarehouse } from "@/api/mes/warehouse/warehouse";
export default {
  name: "ProcureInto",
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
      // 采购入库单表格数据
      procureIntoList: [],
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
        number: null,
        supplierId: null,
        supplierName: null,
        method: null,
        warehouseNumber: null,
        warehouseName: null,
        intoDate: null,
        status: null,
        keeper: null,
        checker: null,
        checkDate: null,
        params:{
          materialName:null,
          supplierName:null,
          specification:null
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
    /** 获取仓库数据*/
    getWarehouseList(){
      let qp =  {pageSize:99999};
      listWarehouse(qp).then(response => {
        this.warehouseList = response.rows;
      });
    },
    /** 查询采购入库单列表 */
    getList() {
      this.loading = true;
      if (null != this.daterangeIntoDate && '' != this.daterangeIntoDate) {
        this.queryParams.params["beginIntoDate"] = this.daterangeIntoDate[0];
        this.queryParams.params["endIntoDate"] = this.daterangeIntoDate[1];
      }else{
        delete this.queryParams.params["beginIntoDate"];
        delete this.queryParams.params["endIntoDate"];
      }
      listProcureInto(this.queryParams).then(response => {
        this.procureIntoList = response.rows;
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
        id: null,
        number: null,
        supplierId: null,
        supplierName: null,
        amount: null,
        price: null,
        method: null,
        warehouseId: null,
        warehouseNumber: null,
        warehouseName: null,
        regionId: null,
        regionNumber: null,
        regionName: null,
        areaId: null,
        areaNumber: null,
        areaName: null,
        intoDate: null,
        status: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        keeper: null,
        checker: null,
        checkDate: null,
        snArray: null
      };
      this.procureIntoEntryList = [];
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
      this.$router.push({ path: "/mes/procureInto-add/index" });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row
      console.log("id====>",id)
      this.$router.push({ path: "/mes/procureInto-edit/index/"+id });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.procureIntoEntryList = this.procureIntoEntryList;
          if (this.form.id != null) {
            updateProcureInto(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProcureInto(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除采购入库单编号为"' + ids + '"的数据项？').then(function() {
        return delProcureInto(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 采购入库单子表序号 */
    rowProcureIntoEntryIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 采购入库单子表添加按钮操作 */
    handleAddProcureIntoEntry() {
      let obj = {};
      obj.sort = "";
      obj.materialId = "";
      obj.materialNumber = "";
      obj.materialName = "";
      obj.specification = "";
      obj.materialUnitId = "";
      obj.materialUnitName = "";
      obj.quantity = "";
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
      obj.manufactureDate = "";
      obj.remark = "";
      this.procureIntoEntryList.push(obj);
    },
    /** 采购入库单子表删除按钮操作 */
    handleDeleteProcureIntoEntry() {
      if (this.checkedProcureIntoEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的采购入库单子表数据");
      } else {
        const procureIntoEntryList = this.procureIntoEntryList;
        const checkedProcureIntoEntry = this.checkedProcureIntoEntry;
        this.procureIntoEntryList = procureIntoEntryList.filter(function(item) {
          return checkedProcureIntoEntry.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleProcureIntoEntrySelectionChange(selection) {
      this.checkedProcureIntoEntry = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('warehouse/procureInto/export', {
        ...this.queryParams
      }, `采购入库${new Date().getTime()}.xlsx`)
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
