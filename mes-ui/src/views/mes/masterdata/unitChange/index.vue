<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主单位" >
        <el-select v-model="queryParams.mainUnit" clearable filterable  placeholder="请选择主单位">
          <el-option
            v-for="item in unitList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="副单位" >
        <el-select v-model="queryParams.deputyUnit" clearable filterable  placeholder="请选择主单位">
          <el-option
            v-for="item in unitList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
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
          v-hasPermi="['masterdata:unitChange:add']"
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
          v-hasPermi="['masterdata:unitChange:edit']"
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
          v-hasPermi="['masterdata:unitChange:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['masterdata:unitChange:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="unitChangeList" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主单位" align="center" prop="mainUnit">
        <template slot-scope="scope">
          {{unitFormat(scope.row.mainUnit)}}
        </template>
      </el-table-column>
      <el-table-column label="比例" align="center" prop="proportion" />
      <el-table-column label="副单位" align="center" prop="deputyUnit" >
        <template slot-scope="scope">
          {{unitFormat(scope.row.deputyUnit)}}
        </template>
      </el-table-column>

      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['masterdata:unitChange:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['masterdata:unitChange:remove']"
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

    <!-- 添加或修改计量单位转换对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主单位" prop="mainUnit">
          <el-select v-model="form.mainUnit" clearable filterable  placeholder="请选择主单位">
            <el-option
              v-for="item in unitList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="副单位" prop="deputyUnit">
          <el-select v-model="form.deputyUnit" clearable filterable  placeholder="请选择副单位">
            <el-option
              v-for="item in unitList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="比例" prop="proportion">
          <el-input v-model="form.proportion" placeholder="请输入比例" style="width: 220px" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :row="2" v-model="form.remark" placeholder="请输入备注" />
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
import { listUnitChange, getUnitChange, delUnitChange, addUnitChange, updateUnitChange } from "@/api/mes/masterdata/unitChange";
import { listUnit } from "@/api/mes/masterdata/unit";
import {selectDictLabel} from "@/utils/jinzhong"


export default {
  name: "UnitChange",
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
      // 计量单位转换表格数据
      unitChangeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        mainUnit: null,
        deputyUnit: null,
      },
      // 表单参数
      form: {

      },
      // 表单校验
      rules: {
        mainUnit: [
          { required: true, message: "主单位不能为空", trigger: "blur" }
        ],
        deputyUnit: [
          { required: true, message: "副单位不能为空", trigger: "blur" }
        ],
        proportion: [
          { required: true, message: "比例不能为空", trigger: "blur" },
          {
            pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
            message: '请输入正数',
            trigger: 'blur'
          }
        ],
      },
      unitList: [],
    };
  },
  created() {
    this.getUnitList();
    this.getList();
  },
  methods: {
    /** 查询计量单位转换列表 */
    getList() {
      this.loading = true;
      listUnitChange(this.queryParams).then(response => {
        this.unitChangeList = response.rows;
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
        mainUnit: null,
        deputyUnit: null,
        proportion: null,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getUnitList();
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
      this.title = "添加计量单位转换";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUnitChange(id).then(response => {
        this.form =response.data;
        this.open = true;
        this.title = "修改计量单位转换";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          if(this.form.mainUnit==this.form.deputyUnit){
            this.$modal.msgWarning("主副计量单位不能相同");
            return;
          }
          //调用后端校验接口，判断数据库中有无该方案
          let queryP = {};
          queryP.pageNum=1;
          queryP.pagesize=10;
          queryP.mainUnit = this.form.mainUnit;
          queryP.deputyUnit = this.form.deputyUnit;
          let tmpList = [];
          listUnitChange(queryP).then(response => {
            tmpList =response.rows;
            if(tmpList.length>1){
              this.$modal.msgWarning("已存在该主副单位的转换关系,请核对");
              return;
            }
            //编辑时
            if (this.form.id != null) {
              if(tmpList.length==1){
                if(tmpList[0].id!=this.form.id){
                  this.$modal.msgWarning("已存在该主副单位的转换关系,请核对");
                  return;
                }
                let mUnit = tmpList[0].mainUnit;
                let pUnit = tmpList[0].deputyUnit;
                if (mUnit!=queryP.mainUnit||pUnit!=queryP.deputyUnit){
                  this.$modal.msgWarning("已存在该主副单位的转换关系,请核对");
                  return;
                }else{
                  updateUnitChange(this.form).then(response => {
                    this.$modal.msgSuccess("修改成功");
                    this.open = false;
                    this.getUnitList();
                    this.getList();
                  });
                }
              }else{
                updateUnitChange(this.form).then(response => {
                  this.$modal.msgSuccess("修改成功");
                  this.open = false;
                  this.getUnitList();
                  this.getList();
                });
              }
            }else{
              //新增时
              if(tmpList.length!=0){
                this.$modal.msgWarning("已存在该主副单位的转换关系,请核对");
                return;
              }else{
                addUnitChange(this.form).then(response => {
                  this.$modal.msgSuccess("新增成功");
                  this.open = false;
                  this.getUnitList();
                  this.getList();
                });
              }
            }
          })
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('请在次确认是否删除？').then(function() {
        return delUnitChange(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('masterdata/unitChange/export', {
        ...this.queryParams
      }, `unitChange_${new Date().getTime()}.xlsx`)
    },
    /** 获取计量单位数据*/
    getUnitList(){
      let qp =  this.queryParams;
      qp.pageSize = 99999;
      listUnit(qp).then(response => {
        this.unitList = response.rows;
        this.loading = false;
      });
    },
    unitFormat(id){
      return selectDictLabel(this.unitList, id);
    }
  }
};
</script>
