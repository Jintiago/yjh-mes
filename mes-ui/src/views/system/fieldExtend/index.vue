<template>
  <div class="app-container">

    <el-row :gutter="12">
      <el-col :span="6">
        <el-card shadow="hover" @click.native="openTable('material')">
          物料信息
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          销售订单主表
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" @click.native="openTable('saleOrderEntry')">
          销售订单子表
        </el-card>
      </el-col>
      <el-col :span="6" >
        <el-card shadow="hover">
          生产工单子表
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="12" style="margin-top: 20px">
      <el-col :span="6">
        <el-card shadow="hover">
          采购入库单主表
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          采购入库单子表
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          生产领料单主表
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          生产领料单子表
        </el-card>
      </el-col>
    </el-row>


    <!-- 添加或修改字段扩展对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-card>
        <el-button type="primary" @click="addFieldExtend">新增</el-button>
            <el-table ref="dragTable" :data="fieldExtendList" >
              <el-table-column label="序号" width="60" >
                <template slot-scope="scope">
                  <el-input v-model="scope.row.sort"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="字段名" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.field"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="字段文本"  width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.fieldName"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="字段类型" width="150">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type">
                    <el-option label="文本框" value="input" />
                    <el-option label="数字框" value="number" />
                    <el-option label="文本域" value="textarea" />
                    <el-option label="下拉框" value="select" />
                    <el-option label="单选框" value="radio" />
                    <el-option label="复选框" value="checkbox" />
                    <el-option label="日期控件" value="datetime" />
                    <el-option label="图片上传" value="imageUpload" />
                    <el-option label="文件上传" value="fileUpload" />
                    <el-option label="富文本控件" value="editor" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="引用数据"  width="260">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.quoteData"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="引用字段"  >
                <template slot-scope="scope">
                  <el-input v-model="scope.row.quoteField"></el-input>
                </template>
              </el-table-column>
            </el-table>
        <el-form label-width="100px">
          <el-form-item style="text-align: center;margin-left:-100px;margin-top:10px;">
<!--            <el-button type="primary" @click="submitForm()">提交</el-button>-->
            <el-button type="primary" @click="saveFieldExtend">保存</el-button>
            <el-button @click="cancel()">返回</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-dialog>

  </div>
</template>

<script>
import { listFieldExtend, getFieldExtend, delFieldExtend, addFieldExtend, updateFieldExtend } from "@/api/mes/system/fieldExtend";

export default {
  name: "FieldExtend",
  dicts: ['sys_normal_disable'],
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
      // 字段扩展表格数据
      fieldExtendList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 30,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        createBy: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      },
      sourceBill: "",
    };
  },
  created() {

  },
  methods: {
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sourceBill: null,
        sort: null,
        field: null,
        fieldName: null,
        type: null,
        quoteData: null,
        quoteField: null,
        formula: null,
        isRequired: null,
        isMust: null,
        status: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFieldExtend(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFieldExtend(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除字段扩展编号为"' + ids + '"的数据项？').then(function() {
        return delFieldExtend(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    openTable(sourceBill){
      this.queryParams.sourceBill=sourceBill;
      this.queryParams.pageNum=1;
      this.queryParams.pageSize=99999;

      if(sourceBill==="material"){
        this.title = "物料信息字段扩展";
      }
      if(sourceBill ==="saleOrderEntry"){
        this.title = "销售订单子表字段扩展";
      }
      this.loading = true;
      listFieldExtend(this.queryParams).then(response => {
        this.fieldExtendList = response.rows;
        if(this.fieldExtendList.length==0){
          //新增操作

        }else{
          //编辑操作
        }
          console.log("11111")
        this.loading = false;
      });
      this.open = true;
    },
    addFieldExtend(){
      let obj = {
        id: null,
        sourceBill: 'saleOrderEntry',
        sort: null,
        field: null,
        fieldName: null,
        type: null,
        quoteData: null,
        quoteField: null,
        formula: null,
        isRequired: null,
        isMust: null,
        status: '0',
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null}
      this.fieldExtendList.push(obj)

    },
    saveFieldExtend(){
      addFieldExtend(this.fieldExtendList).then(response => {
        this.$modal.msgSuccess("保存成功");
        this.open = false;
        this.getList();
      });

    }
  }
};
</script>
