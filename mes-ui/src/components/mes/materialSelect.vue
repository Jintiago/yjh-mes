<template>
  <el-dialog title="物料产品选择"
    v-if="showFlag"
    :visible.sync="showFlag"
    :modal= false
    width="80%"
    center
  >
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
        <el-form :model="queryParams"  size="small" :inline="true"  v-show="showSearch" label-width="68px" >
          <el-form-item label="名称" >
            <el-input
              v-model="queryParams.name"
              placeholder="请输入名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="编号" >
            <el-input
              v-model="queryParams.number"
              placeholder="请输入编号"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="规格型号" >
            <el-input
              v-model="queryParams.specification"
              placeholder="请输入规格型号"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="物料类型" >
            <el-select v-model="queryParams.typeId" placeholder="请选择物料类型" clearable>
              <el-option
                v-for="dict in dict.type.material_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item style="margin-left: 20px">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="materialList" ref="singleTable" @current-change="handleCurrentChange" highlight-current-row max-height="350">
          <el-table-column label="名称" align="center" prop="name" width="150"/>
          <el-table-column label="编号" align="center" prop="number" />
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
<!--          <el-table-column label="备注" align="center" prop="remark" />-->
<!--          <el-table-column label="更新时间" align="center" prop="updateTime" width="180">-->
<!--            <template slot-scope="scope">-->
<!--              <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>-->
<!--            </template>-->
<!--          </el-table-column>-->
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
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm" >确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listMaterial } from "@/api/mes/masterdata/material";
import {  listUnit } from '@/api/mes/masterdata/unit'
import { listMaterialClass } from '@/api/mes/masterdata/materialClass'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { selectDictLabel } from '@/utils/jinzhong'
export default {
  name: "Material",
  components: {
    Treeselect
  },
  dicts: ['material_type', 'sys_normal_disable'],
  data() {
    return {
      // 遮罩层
      showFlag: false,
      // 当前行
      currentRow: null,
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
        status: 0,
      },
      // 表单参数
      form: {},
      materialClassOptions:[],
      defaultProps: {
        children: "children",
        label: "name"
      },
      unitList:[],

    };
  },
  created() {
    this.getMaterialClassOptions();
    this.getUnitList();
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
      this.showFlag = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        number: null,
        specification: null,
        unitId: null,
        classId: null,
        typeId: "A",
        status: "0",
        remark: null,
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
      let name = selectDictLabel(this.unitList, id);
      return name;
    },
    //确定选中
    submitForm(){
      let result = this.currentRow;
      result.unitName = selectDictLabel(this.unitList, result.unitId);
      this.$emit('onMaterialSelect',result);
      this.showFlag = false;
    },
    handleCurrentChange(val) {
      this.currentRow = val;
    }
  }
};
</script>
<style scoped>

</style>
