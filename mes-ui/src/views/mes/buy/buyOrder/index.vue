<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入单号"
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
      <el-form-item label="采购员" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入采购员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采购日期">
        <el-date-picker
          v-model="daterangeBuyDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="供应商" prop="supplierName">
        <el-input
          v-model="queryParams.supplierName"
          placeholder="请输入供应商"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核人" prop="checker">
        <el-input
          v-model="queryParams.checker"
          placeholder="请输入审核人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="金额" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入金额"
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
          v-hasPermi="['buy:buyOrder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['buy:buyOrder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['buy:buyOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['buy:buyOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="buyOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单号" align="center" prop="number" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bill_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="采购员" align="center" prop="userName" />
      <el-table-column label="采购日期" align="center" prop="buyDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.buyDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="供应商" align="center" prop="supplierName" />
      <el-table-column label="审核人" align="center" prop="checker" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="金额" align="center" prop="amount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['buy:buyOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['buy:buyOrder:remove']"
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

    <!-- 添加或修改采购订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1350px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-row>
          <el-col :span="6">
            <el-form-item label="单号" prop="number">
              <el-input v-model="form.number" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="采购日期" prop="buyDate">
              <el-date-picker clearable
                              v-model="form.buyDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择采购日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="采购员" prop="userName">
              <el-input v-model="form.userName" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="采购部门" prop="deptName">
              <el-input v-model="form.deptName" placeholder="" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="供应商" prop="supplierName">
              <el-input v-model="form.supplierName" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="审核人" prop="checker">
              <el-input v-model="form.checker" placeholder="" />
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
          <el-col :span="6">
            <el-form-item label="总金额" prop="amount">
              <el-input v-model="form.amount" placeholder="请输入金额" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>

        <el-divider content-position="center">采购订单明细信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddBuyOrderEntry">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteBuyOrderEntry">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="buyOrderEntryList" :row-class-name="rowBuyOrderEntryIndex" @selection-change="handleBuyOrderEntrySelectionChange" ref="buyOrderEntry">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" prop="sort" width="70">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sort" placeholder="" />
            </template>
          </el-table-column>
          <el-table-column label="紧急度" prop="materialNumber" width="70">
            <template slot-scope="scope">
              <el-input v-model="scope.row.materialNumber" placeholder="" />
            </template>
          </el-table-column>
          <el-table-column label="要货日期" prop="needDate" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.needDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择要货日期" />
            </template>
          </el-table-column>
          <el-table-column label="物料编号" prop="materialNumber" width="80">
            <template slot-scope="scope">
              <el-input v-model="scope.row.materialNumber" placeholder="" />
            </template>
          </el-table-column>
          <el-table-column label="物料名" prop="materialName" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.materialName" placeholder="" />
            </template>
          </el-table-column>
          <el-table-column label="规格型号" prop="materialSpecification" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.materialSpecification" placeholder="" />
            </template>
          </el-table-column>
          <el-table-column label="数量" prop="quantity" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.quantity" placeholder="" />
            </template>
          </el-table-column>
          <el-table-column label="单位" prop="unitName" width="70">
            <template slot-scope="scope">
              <el-input v-model="scope.row.unitName" placeholder="" />
            </template>
          </el-table-column>
          <el-table-column label="单价" prop="taxPrice" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.taxPrice" placeholder="" />
            </template>
          </el-table-column>
          <el-table-column label="是否赠品" prop="isGift" width="150" v-if="false">
            <template slot-scope="scope">
              <el-input v-model="scope.row.isGift" placeholder="请输入是否赠品" />
            </template>
          </el-table-column>
          <el-table-column label="税率" prop="taxRate" width="150" v-if="false">
            <template slot-scope="scope">
              <el-input v-model="scope.row.taxRate" placeholder="请输入税率" />
            </template>
          </el-table-column>
          <el-table-column label="税额" prop="totalTax" width="150" v-if="false">
            <template slot-scope="scope">
              <el-input v-model="scope.row.totalTax" placeholder="请输入税额" />
            </template>
          </el-table-column>
          <el-table-column label="金额" prop="totalPrice" width="150" v-if="false">
            <template slot-scope="scope">
              <el-input v-model="scope.row.totalPrice" placeholder="请输入金额" />
            </template>
          </el-table-column>
          <el-table-column label="金额" prop="totalTaxPrice" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.totalTaxPrice" :disabled="true" />
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark" >
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="请输入备注" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBuyOrder, getBuyOrder, delBuyOrder, addBuyOrder, updateBuyOrder } from "@/api/mes/buy/buyOrder";

export default {
  name: "BuyOrder",
  dicts: ['bill_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedBuyOrderEntry: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 采购订单表格数据
      buyOrderList: [],
      // 采购订单明细表格数据
      buyOrderEntryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 扩展字段时间范围
      daterangeBuyDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        number: null,
        status: null,
        userName: null,
        buyDate: null,
        supplierName: null,
        checker: null,
        amount: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        number: [],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        buyDate: [
          { required: true, message: "采购日期不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "金额不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询采购订单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeBuyDate && '' != this.daterangeBuyDate) {
        this.queryParams.params["beginBuyDate"] = this.daterangeBuyDate[0];
        this.queryParams.params["endBuyDate"] = this.daterangeBuyDate[1];
      }
      listBuyOrder(this.queryParams).then(response => {
        this.buyOrderList = response.rows;
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
        status: null,
        deptId: null,
        deptName: null,
        userId: null,
        userName: null,
        buyDate: null,
        supplierId: null,
        supplierName: null,
        checker: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        amount: null
      };
      this.buyOrderEntryList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeBuyDate = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加采购订单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBuyOrder(id).then(response => {
        this.form = response.data;
        this.buyOrderEntryList = response.data.buyOrderEntryList;
        this.open = true;
        this.title = "修改采购订单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.buyOrderEntryList = this.buyOrderEntryList;
          if (this.form.id != null) {
            updateBuyOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBuyOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除采购订单编号为"' + ids + '"的数据项？').then(function() {
        return delBuyOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 采购订单明细序号 */
    rowBuyOrderEntryIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 采购订单明细添加按钮操作 */
    handleAddBuyOrderEntry() {
      let obj = {};
      obj.sort = "";
      obj.materialId = "";
      obj.materialNumber = "";
      obj.materialName = "";
      obj.materialSpecification = "";
      obj.unitId = "";
      obj.unitName = "";
      obj.quantity = "";
      obj.price = "";
      obj.taxPrice = "";
      obj.isGift = "";
      obj.taxRate = "";
      obj.totalTax = "";
      obj.totalPrice = "";
      obj.totalTaxPrice = "";
      obj.needDate = "";
      obj.remark = "";
      obj.status = "";
      obj.extendField = "";
      this.buyOrderEntryList.push(obj);
    },
    /** 采购订单明细删除按钮操作 */
    handleDeleteBuyOrderEntry() {
      if (this.checkedBuyOrderEntry.length == 0) {
        this.$modal.msgError("请先选择要删除的采购订单明细数据");
      } else {
        const buyOrderEntryList = this.buyOrderEntryList;
        const checkedBuyOrderEntry = this.checkedBuyOrderEntry;
        this.buyOrderEntryList = buyOrderEntryList.filter(function(item) {
          return checkedBuyOrderEntry.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleBuyOrderEntrySelectionChange(selection) {
      this.checkedBuyOrderEntry = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('buy/buyOrder/export', {
        ...this.queryParams
      }, `buyOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
