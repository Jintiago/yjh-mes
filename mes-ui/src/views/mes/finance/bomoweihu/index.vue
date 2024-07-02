<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['finance:bomoweihu:add']"
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
          v-hasPermi="['finance:bomoweihu:edit']"
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
          v-hasPermi="['finance:bomoweihu:remove']"
        >删除</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bomoweihuList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="类型" align="center" prop="type" />
      <el-table-column label="原料价格" align="center" prop="yuanliaojg" />
      <el-table-column label="普通胶水价格" align="center" prop="jiaoshuijg" />
      <el-table-column label="特殊胶水价格" align="center" prop="jiaoshuijgB" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['finance:bomoweihu:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['finance:bomoweihu:remove']"
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

    <!-- 添加或修改天润薄膜核算维护对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item    label="类型"  prop="type" >
          <el-select v-model="form.type"    >
            <el-option
              v-for="item in typeList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="原料价格" prop="yuanliaojg">
          <el-input type="number" v-model="form.yuanliaojg" placeholder="请输入原料价格" />
        </el-form-item>
        <el-form-item label="普通胶水价格" prop="jiaoshuijg">
          <el-input type="number" v-model="form.jiaoshuijg" placeholder="请输入胶水价格" />
        </el-form-item>
        <el-form-item label="特殊胶水价格" prop="jiaoshuijgB">
          <el-input type="number" v-model="form.jiaoshuijgB" placeholder="请输入胶水价格" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBomoweihu, getBomoweihu, delBomoweihu, addBomoweihu, updateBomoweihu } from "@/api/mes/finance/bomoweihu";

export default {
  name: "Bomoweihu",
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
      // 天润薄膜核算维护表格数据
      bomoweihuList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        priceType: null,
        type: null,
        yuanliaojg: null,
        jiaoshuijg: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        priceType: [
          { required: true, message: "核算方式不能为空", trigger: "change" }
        ],
        type: [
          { required: true, message: "类型不能为空", trigger: "change" }
        ],
        yuanliaojg: [
          { required: true, message: "请输入0以上的数字", trigger: "blur" }
        ],
        jiaoshuijg: [
          { required: true, message: "请输入0以上的数字", trigger: "blur" }
        ]
      },
      priceTypeList :[{
        value: '公斤价',
        label: '公斤价'
      }, {
        value: '平方价',
        label: '平方价'
      }],
      typeList :[{
        value: '透明',
        label: '透明'
      }, {
        value: '黑白',
        label: '黑白'
      },{
        value: '黑白加填充',
        label: '黑白加填充'
      }]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询天润薄膜核算维护列表 */
    getList() {
      this.loading = true;
      listBomoweihu(this.queryParams).then(response => {
        this.bomoweihuList = response.rows;
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
        priceType: '平方价',
        type: '透明',
        yuanliaojg: 0,
        jiaoshuijgB:0,
        jiaoshuijg: 0
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
      this.title = "添加天润薄膜核算维护";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBomoweihu(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改天润薄膜核算维护";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBomoweihu(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBomoweihu(this.form).then(response => {
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
      const id = row.id || this.ids;
      this.$modal.confirm('是否确认删除数据项？').then(function() {
        return delBomoweihu(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/bomoweihu/export', {
        ...this.queryParams
      }, `bomoweihu_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
