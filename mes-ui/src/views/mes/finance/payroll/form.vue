<template>
  <!-- 添加或修改工资单对话框 -->
  <el-dialog title="工资单" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
    <el-form ref="form" :model="form"  label-width="85px">

          <el-form-item label="工资单编号" >
            <el-input v-model="form.number" :disabled="true" />
          </el-form-item>

          <el-form-item label="工资单日期" >
            <el-input v-model="form.payrollDate" :disabled="true" />
          </el-form-item>
          <el-form-item label="人员" >
            <el-input v-model="form.userName" :disabled="true" />
          </el-form-item>
          <el-form-item label="应发工资">
            <el-input v-model="form.shouldPay"  :disabled="true"/>
          </el-form-item>
          <el-form-item label="实发工资" >
            <el-input v-model="form.realPay" :disabled="true" />
          </el-form-item>
          <el-form-item label="状态" >
            <dict-tag :options="dict.type.bill_status" :value="form.status" :disabled="true"/>
          </el-form-item>
          <el-form-item label="备注" >
            <el-input
              type="textarea"
              :rows="2"
              v-model="form.remark" :disabled="true">
            </el-input>
          </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="reportQualityCancel" >取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listPayroll, getPayroll, delPayroll, addPayroll, updatePayroll,payRollPreview } from "@/api/mes/finance/payroll";

export default {
  name: 'payrollForm',
  dicts: ['production_status','report_channel','sys_yes_no','qc_type','bill_status'],
  data() {
    return {

      //工资单
      form:{
      },
      // 是否显示弹出层
      open: false,

    };
  },
  activated() {

  },
  created() {

  },
  methods: {


    //详情
    clickInfo(row){
      listPayroll({ pageNum:1,pageSize:1, reportIds: row.id,status:'A'}).then(res => {
        let list = res.rows;
        if(list.length>0){
          this.form = list[0]
          this.resetForm("form");
          this.open = true;
        }else{
          this.$modal.msgWarning("还未生成工资单");
        }
      })

    },
    // 取消按钮
    reportQualityCancel() {
      this.form=[];
      this.resetForm("reportQualityForm");
      this.open = false;
    },


  }
}
</script>

<style scoped>
.bg-purple-light {
  border-radius: 4px;
  min-height: 36px;
  background: #FFFFFF;
}
</style>
