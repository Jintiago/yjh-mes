<template>
  <div class="app-container">
    <el-row :gutter="20">

      <!--分类数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-tree
            :data="materialClassOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <!--物料数据-->
      <el-col :span="20" :xs="24">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入编名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格型号" prop="specification">
        <el-input
          v-model="queryParams.specification"
          placeholder="请输入规格型号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计量单位" prop="unitId">
        <el-select v-model="queryParams.unitId" clearable filterable  placeholder="请选择单位">
          <el-option
            v-for="item in unitList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="物料类型" prop="typeId">
        <el-select v-model="queryParams.typeId" placeholder="请选择物料类型" clearable>
          <el-option
            v-for="dict in dict.type.material_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
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
          v-hasPermi="['masterdata:material:add']"
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
          v-hasPermi="['masterdata:material:edit']"
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
          v-hasPermi="['masterdata:material:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['masterdata:material:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange" class="tableBox">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="number" />
      <el-table-column label="名称" width="180" align="center" prop="name" />
      <el-table-column label="规格型号" align="center" prop="specification" />
      <el-table-column label="计量单位" align="center" prop="unitId" >
        <template slot-scope="scope">
          {{unitFormat(scope.row.unitId)}}
        </template>
      </el-table-column>
      <el-table-column label="物料类型" align="center" prop="typeId">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.material_type" :value="scope.row.typeId"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['masterdata:material:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['masterdata:material:remove']"
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
      </el-col>
    </el-row>
    <!-- 添加或修改物料对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="number">
              <el-input v-model="form.number" placeholder="请输入编号" :disabled="disabledFlag"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="名称" prop="name" >
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="规格型号" prop="specification">
              <el-input v-model="form.specification" placeholder="请输入规格型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位" prop="unitId" style="margin-left: 20px">
              <el-select v-model="form.unitId" clearable filterable  placeholder="请选择主单位">
                <el-option
                  v-for="item in unitList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="物料分类" prop="classId">
              <treeselect v-model="form.classId" :options="materialClassOptions" :normalizer="normalizer" placeholder="请输入物料分类" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料类型" prop="typeId" style="margin-left: 20px">
              <el-select v-model="form.typeId" placeholder="请选择物料类型">
                <el-option
                  v-for="dict in dict.type.material_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="绑定工序路线" prop="routeId" label-width="80">
              <el-select v-model="form.routeId" clearable filterable  placeholder="请绑定工序路线" @change="routeChange" style="width: 320px">
                <el-option
                  v-for="item in routeList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark" >
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <el-steps :active="9" align-center>
        <el-step   :title="item.processName"   v-for="item in routeProcessList"></el-step>
      </el-steps>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/mes/masterdata/material";
import { listRoute,getRoute } from "@/api/mes/production/route";
import { listUnit } from '@/api/mes/masterdata/unit'
import { listMaterialClass } from '@/api/mes/masterdata/materialClass'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { selectDictLabel } from '@/utils/jinzhong';
import { listFieldExtend, getFieldExtend, delFieldExtend, addFieldExtend, updateFieldExtend } from "@/api/mes/system/fieldExtend";
export default {
  name: "Material",
  components: {
    Treeselect
  },
  dicts: ['material_type', 'sys_normal_disable'],
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
      // 物料表格数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 50,
        name: null,
        number: null,
        specification: null,
        unitId: null,
        classId: null,
        typeId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        number: [
          { required: true, message: "编号不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        unitId: [
          { required: true, message: "计量单位不能为空", trigger: "blur" }
        ],
        classId: [
          { required: true, message: "物料分类不能为空", trigger: "blur" }
        ],
        typeId: [
          { required: true, message: "物料类型不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      },
      materialClassOptions:[],
      defaultProps: {
        children: "children",
        label: "name"
      },
      unitList:[],
      disabledFlag: true,
      routeList:[],
      routeProcessList: [],
    };
  },
  created() {
    this.getMaterialClassOptions();
    this.getUnitList();
    this.getRouteList();
    this.getList();

  },
  methods: {
    /** 查询物料列表 */
    getList() {
      this.loading = true;
      listMaterial(this.queryParams).then(response => {
        this.materialList = response.rows;
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
        name : null,
        specification: null,
        unitId: null,
        classId: null,
        typeId: "A",
        status: "0",
        remark: null,
        routeId: null,
      };
      this.routeProcessList =[];
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
      this.title = "添加物料";
      this.disabledFlag = false;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMaterial(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物料";
        this.disabledFlag = true;
        if(this.form.routeId!=null){
          this.routeChange(this.form.routeId);
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMaterial(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMaterial(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除？').then(function() {
        return delMaterial(ids);
      }).then(response => {
        this.getList();
        this.$modal.msgSuccess("删除成功"+response.data+"条");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('masterdata/material/export', {
        ...this.queryParams
      }, `material_${new Date().getTime()}.xlsx`)
    },
    handleNodeClick(data) {
      this.queryParams.classId = data.id;
      this.handleQuery();
    },
    /** 获取计量单位数据*/
    getMaterialClassOptions(){
      listMaterialClass().then(response => {
        this.materialClassOptions = [];
        const data = { id: null, name: '全部物料', children: [] };
        data.children = this.handleTree(response.data, "id", "parentId");
        this.materialClassOptions.push(data);
      });
    },
    /** 获取计量单位数据*/
    getUnitList(){
      let qp =  {pageSize:99999};
      listUnit(qp).then(response => {
        this.unitList = response.rows;
      });
    },
    /** 转换物料分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children
      };
    },
    unitFormat(id){
      return selectDictLabel(this.unitList, id);
    },
    /** 查询工序路线列表 */
    getRouteList() {
      listRoute({pageSize:999999}).then(response => {
        this.routeList = response.rows;
      });
    },
    routeChange(value){

      if(value===""){
        this.routeProcessList=[]
        return;
      }

      getRoute(value).then(response => {
        this.routeProcessList = response.data.routeProcessList;
      });
    }
  }
};
</script>
<style lang="scss">
.tableBox {
  th {
    padding: 0 !important;
    height: 45px;
    line-height: 45px;
  }
  td {
    padding: 0 !important;
    height: 45px;
    line-height: 45px;
  }
}
</style>

