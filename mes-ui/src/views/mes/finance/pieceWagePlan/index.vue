<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--分类数据-->
      <el-col :span="3" :xs="24">
        <div class="head-container">
          <el-tree
            class="filter-tree"
            :data="processList"
            :props="{children: 'children',label: 'name'}"
            default-expand-all
            :highlight-current="true"
            :filter-node-method="filterNode"
            @current-change="processQueryChange"
            ref="tree">
          </el-tree>
        </div>
      </el-col>

      <el-col :span="21" :xs="24">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
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
          v-hasPermi="['finance:pieceWagePlan:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['finance:pieceWagePlan:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
        <el-form ref="form" :model="form" :rules="rules" label-width="120">
    <el-table  :data="form.pieceWagePlanList" @selection-change="handleSelectionChange">
      <el-table-column label="工序" align="center"  width="150" >
        <template slot-scope="scope">
          <el-form-item    label-width="120"   style="margin-bottom: 12px;">
            <el-input v-model="scope.row.processName" :disabled="true"/>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="物料名称" align="center" width="200" >
        <template slot-scope="scope">
          <el-form-item   :prop="'pieceWagePlanList.'+scope.$index+'.materialName'" :rules="rules.materialName" label-width="240"  style="margin-bottom: 12px;">
            <el-input v-model="scope.row.materialName"  >
              <el-button slot="append" @click="handleSelectProduct(scope.row)" icon="el-icon-search"></el-button>
            </el-input>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="物料编号" align="center" width="120" >
        <template slot-scope="scope">
          <el-form-item     label-width="120"   style="margin-bottom: 12px;">
            <el-input v-model="scope.row.materialNumber"  :disabled="true"/>
          </el-form-item>
        </template>
      </el-table-column>

      <el-table-column label="规格型号" align="center" width="120" >
        <template slot-scope="scope">
          <el-form-item     label-width="120"   style="margin-bottom: 12px;">
            <el-input v-model="scope.row.specification" :disabled="true"/>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="单位" align="center" width="55" >
        <template slot-scope="scope">
          <el-form-item       style="margin-bottom: 12px;">
            <el-input v-model="scope.row.materialUnitName" :disabled="true"/>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="单价" align="center"  width="80">
        <template slot-scope="scope">
          <el-form-item       style="margin-bottom: 12px;">
            <el-input v-model="scope.row.price" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="岗位" align="center"  width="120">
        <template slot-scope="scope">
          <el-form-item       style="margin-bottom: 12px;">
            <el-select v-model="scope.row.postId"  @change="postChange($event,scope.row)" :clearable="true" >
              <el-option
                v-for="item in postList"
                :key="item.postId"
                :label="item.postName"
                :value="item.postId"
              />
            </el-select>

          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center"  >
        <template slot-scope="scope">
          <el-form-item   :prop="'pieceWagePlanList.'+scope.$index+'.status'"    style="margin-bottom: 12px;">
            <el-select v-model="scope.row.status"   >
              <el-option
                v-for="dict in dict.type.bill_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
                placeholder=""
              />
            </el-select>

          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="submitForm(scope.$index)"
            v-hasPermi="['finance:pieceWagePlan:edit']"
          >保存</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['finance:pieceWagePlan:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
        </el-form>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
      </el-col>
      <MaterialSelect ref="materialSelect" @onMaterialSelect="onMaterialSelect" > </MaterialSelect>
    </el-row>

  </div>
</template>

<script>
import {
  listPieceWagePlan,
  getPieceWagePlan,
  delPieceWagePlan,
  addPieceWagePlan,
  updatePieceWagePlan,
  savePieceWagePlan
} from '@/api/mes/finance/pieceWagePlan'
import { listProcess } from '@/api/mes/production/process'
import MaterialSelect  from "@/components/mes/materialSelect.vue";
import Treeselect from '@riophae/vue-treeselect'
import { listPost, getPost, delPost, addPost, updatePost } from "@/api/system/post";

export default {
  dicts: ['bill_status'],
  components: { Treeselect,MaterialSelect },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  name: "PieceWagePlan",
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

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
        processId: null,
        processName: null,
        materialName: null,
        status: null,
      },
      // 表单参数
      form: {
        // 计件工资方案表格数据
        pieceWagePlanList: [],
      },
      // 表单校验
      rules: {
        processId: [
          { required: true, message: "工序id不能为空", trigger: "change" }
        ],
        materialId: [
          { required: true, message: "物料ID不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "单价不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      },

      filterText: '',
      processList :[],
      currentRow: '',
      processName : null,
      postList: []
    };
  },
  created() {
    this.getList();
    this.getProcessList();
    this.getPostList();
  },
  methods: {
    /** 查询计件工资方案列表 */
    getList() {
      listPieceWagePlan(this.queryParams).then(response => {
        this.form.pieceWagePlanList = response.rows;
        this.total = response.total;
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
      if(this.queryParams.processId===null){
        this.$modal.msgError("请先在左侧选择对应工序");
        return ;
      }
      let count=0;
      for (let i = 0; i < this.form.pieceWagePlanList.length; i++) {
        let id = this.form.pieceWagePlanList[i].id;
        console.log("id===>",id);
        if(typeof (id)==="undefined"){
          count++;
        }
      }
      if(count>0){
        this.$modal.msgError("还有方案未保存，请先保存");
        return ;
      }
      let obj = {};
      obj.processId=this.queryParams.processId,
      obj.processName= this.processName,
      obj.materialId= null,
      obj.materialNumber= null,
      obj.materialName= null,
      obj.specification=null,
      obj.materialUnitName= null,
      obj.price= 1,
      obj.postId= null,
      obj.postName= null,
      obj.timeBegin= null,
      obj.timeEnd= null,
      obj.status= 'A',
      obj.remark= null
      this.form.pieceWagePlanList.push(obj);
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPieceWagePlan(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改计件工资方案";
      });
    },
    /** 提交按钮 */
    submitForm(index) {
      this.$refs["form"].validate(valid => {
        if (valid) {
          savePieceWagePlan(this.form.pieceWagePlanList[index]).then(response => {
              this.$modal.msgSuccess("保存成功");
              this.open = false;
              this.getList();
            });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id
      let _this =this;
      this.$modal.confirm('是否确认删除？').then(function() {
        if(  typeof(ids) ==="undefined"){
          _this.form.pieceWagePlanList = _this.form.pieceWagePlanList.filter(function(item) {
            return item.sort != ids
          });
          _this.$forceUpdate();
        }else{
          return delPieceWagePlan(ids);
        }

      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('finance/pieceWagePlan/export', {
        ...this.queryParams
      }, `pieceWagePlan_${new Date().getTime()}.xlsx`)
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    selectProcess(value,row){
      let currentRow = row;
      let res = this.processList.filter((item) => {
        return item.id ===  value;
      });
      currentRow.processName = res[0].name;
      this.$refs["form"].validate();
    },
    /** 查询工序列表 */
    getProcessList() {
      listProcess({pageNum: 1,pageSize: 999999 ,status: '0'} ).then(response => {
        let tmp =[{id:null,name:"全部工序"}]
        this.processList = tmp.concat(response.rows);
        console.log("this.processList",this.processList)
      });
    },

    getPostList(){
      listPost({pageNum: 1,pageSize: 999999 ,status: '0'} ).then(response => {
        this.postList = response.rows;
      });

    },
    //物料选择弹出框
    handleSelectProduct(row){
      this.currentRow = row;
      this.$refs.materialSelect.showFlag = true;
    },
    //接收物料弹框返回参数
    onMaterialSelect(obj){
      if(obj != undefined && obj != null){
        let entryList = this.form.pieceWagePlanList;
        for (let i = 0; i < entryList.length; i++) {
          let materialName = entryList[i].materialName;
          if(materialName === obj.name){
            this.$modal.msgError("请不要选择重复的产品");
            return ;
          }
        }
        let row = this.currentRow;
        row.materialId = obj.id;
        row.materialName = obj.name;
        row.materialNumber = obj.number;
        row.specification= obj.specification;
        row.materialUnitName = obj.unitName;
      }
      this.$refs["form"].validate();
    },
    processQueryChange(data){
      this.queryParams.processId=data.id;
      this.processName = data.name;
      this.getList();
    },
    postChange(value,row){

      if(value===""){
        row.postId=null;
        row.postName=null;
        return;
      }
      let opt= {};
      opt= this.postList.find((item)=>{
        return item.postId === value;
      });
      row.postName=opt.postName
    }
  }
};
</script>
