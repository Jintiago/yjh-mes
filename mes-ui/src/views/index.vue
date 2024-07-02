<template>
  <div style="margin: 15px" >
    <el-row>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
        <el-form-item label="数据日期" style="margin-left: 10px">
          <el-date-picker
            v-model="queryParams.dateQuery"
            type="daterange"
            unlink-panels
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            :clearable="false"
            @change="handleQuery"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item style="margin-left: 20px">
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="14">
        <el-card header="产量走势图" style="font-weight: bold;">
          <div ref="gdjfzsEchart" style="height: 320px;"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card header="工单延期率" style="font-weight: bold;">
          <div ref="yanqiEchart" style="height: 320px;"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 12px">
      <el-col :span="10">
        <el-card header="生产合格率" style="font-weight: bold;">
          <div ref="hegeEchart" style="height: 320px;"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card header="不合格原因" style="font-weight: bold;">
          <el-table
            :data="qualityList"
            style="width: 100%"
          >
            <el-table-column
              prop="reportTime"
              label="报工时间"
              width="150"
            >
            </el-table-column>
            <el-table-column
              prop="reportUserName"
              label="报工人"
              width="100"
            >
            </el-table-column>
            <el-table-column
              prop="checkTime"
              label="质检时间"
              width="150"
            >
            </el-table-column>
            <el-table-column
              prop="text"
              label="不合格数"
            >
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

<!--    <el-row :gutter="20" style="margin-top: 12px">-->
<!--      <el-card header="不合格品走势图" style="font-weight: bold;">-->
<!--        <div ref="cpzlzsEchart" style="height: 320px;"></div>-->
<!--      </el-card>-->
<!--    </el-row>-->

  </div>
</template>

<script>

import { defermentFactor, outputTrend,qualificationRate,unqualifiedReason } from '@/api/mes/index'

export default {
  name: 'index',
  data() {
    return {
      queryParams: {
        dateQuery: []
      },
      pickerOptions: {
        shortcuts: [
          {
            text: '今天',
            onClick(picker) {

              picker.$emit('pick', [new Date(), new Date()])
            }
          },
          {
            text: '七天内',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setDate(end.getDay() - 7)
              picker.$emit('pick', [start, end])
            }
          },
          {

            text: '本周',
            onClick(picker) {
              // 获取今天
              const end = new Date()
              // 获取当前周的第一天
              const start = new Date(
                end.getFullYear(),
                end.getMonth(),
                end.getDate() - ((end.getDay() + 6) % 7)
              )
              start.setTime(start.getTime())
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '本月',
            onClick(picker) {
              const end = new Date()
              // 获取当前月的第一天
              const start = new Date(end.getFullYear(), end.getMonth(), 1)
              start.setTime(start.getTime())
              picker.$emit('pick', [start, end])
            }
          }
        ]
      },
      qualityList: []

    }
  },
  created() {
    this.queryParamsInit();
  },
  mounted() {
    this.init()
  },
  methods: {
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
     this.queryParamsInit();
      this.handleQuery();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.initPic();
    },
    init() {
      this.$nextTick(() => {
        this.initPic()
      })
    },
    initPic() {
      //调用接口获取数据
      outputTrend(this.queryParams).then(response => {
        console.log('response===>', response)
        let responseDate = response.data;
        //产能走势图
        var gdjfzsEchart = this.$echarts.init(this.$refs.gdjfzsEchart)
        var gdjfzsOption = {
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: [],
              type: 'line',
              smooth: true
            }
          ]
        }
        gdjfzsOption.series[0].data=responseDate.value;
        gdjfzsOption.xAxis.data=responseDate.text;
        gdjfzsOption && gdjfzsEchart.setOption(gdjfzsOption)
      })

      //延期率饼状图
      defermentFactor(this.queryParams).then(response =>{
        let responseData = response.data;
        var yanqiEchart = this.$echarts.init(this.$refs.yanqiEchart)
        var yanqiOption = {
          title: {
            text: '',
            left: 'center'
          },
          color: ['#ee6666','#5470c6','#91cc75', '#73c0de','#fac858' ],
          series: [
            {
              type: 'pie',
              radius: '55%',
              //百分比
              label: {
                normal: {
                  show: true,
                  formatter: '{b}: {c} ({d}%)' //自定义显示格式(b:name, c:value, d:百分比)
                }
              },
              data: [
              ]
            }
          ]
        }
        yanqiOption.title.text="工单总数: "+responseData.zongshu;
        yanqiOption.series[0].data=responseData.array;
        yanqiOption && yanqiEchart.setOption(yanqiOption)

      })

      //合格率饼状图
      qualificationRate(this.queryParams).then(response=>{
        let resDate = response.data;
        var hegeEchart = this.$echarts.init(this.$refs.hegeEchart)
        var hegeOption = {
          title: {
            text: '生产总数: 0',
            left: 'center'
          },
          color: ['#91cc75', '#ee6666'],
          series: [
            {
              type: 'pie',
              radius: '65%',
              //百分比
              label: {
                normal: {
                  show: true,
                  formatter: '{b}: {c} ({d}%)' //自定义显示格式(b:name, c:value, d:百分比)
                }
              },
              data: [
                { name:'合格' ,value:0},
                { name:'不合格' ,value:0}
              ]
            }
          ]
        }
        if(typeof (resDate)!=='undefined'){
          let hege={ name:'合格' ,value: resDate.hege };
          let buhege={ name:'不合格' ,value: resDate.buhege};
          hegeOption.series[0].data[0]=hege;
          hegeOption.series[0].data[1]=buhege;
          hegeOption.title.text='生产总数: '+resDate.zongshu;

        }
        hegeOption && hegeEchart.setOption(hegeOption)
      })
      //不合格原因列表
      unqualifiedReason(this.queryParams).then(res =>{

        this.qualityList=res.data;

      })


      //不合格品走势图
        var cpzlzsEchart = this.$echarts.init(this.$refs.cpzlzsEchart)
        var cpzlzsOption = {
          color: ['#ee6666'],
          xAxis: {
            type: 'category',
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: [3, 5, 7, 30, 6, 13, 50],
              type: 'line',
              smooth: true
            }
          ]
        }
        cpzlzsOption && cpzlzsEchart.setOption(cpzlzsOption)


    },
    queryParamsInit(){
      // 获取今天
      const end = new Date()
      // 获取上个月
      const start = new Date(
        end.getFullYear(),
        end.getMonth()-1,
        end.getDate()
      )
      start.setTime(start.getTime())
      this.queryParams={
        dateQuery: [start.format('yyyy-MM-dd'), end.format('yyyy-MM-dd')]
      }

    }

  }
}
</script>

<style scoped>

</style>
