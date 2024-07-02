<template>
  <div style="margin: 15px">
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
    <div class="container">
      <el-row :gutter="10">
        <el-col :span="16">
          <div ref="oeeUtilizationEchart" style="height: 320px;"></div>
        </el-col>
        <el-col :span="8">
          <el-card style="font-weight: bold;">
            <div ref="oeePieEchart" style="height: 280px;"></div>
          </el-card>
        </el-col>
      </el-row>

    </div>
  </div>
</template>

<script>

import { listEquipment, getEquipment, oeeUtilizationList } from '@/api/mes/equipment/equipment'
import { outputTrend } from '@/api/mes'
import * as echarts from 'echarts'
import GanttChar from './ganttx.vue'

export default {
  name: 'index',
  components: {
    GanttChar
  },
  data() {
    return {
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
      queryParams: {
        dateQuery: []
      },

    }
  },
  created() {
    this.queryParamsInit()
  },
  mounted() {
    this.init()
  },
  methods: {
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParamsInit()
      this.handleQuery()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.initPic()
    },
    init() {
      this.$nextTick(() => {
        this.initPic()
      })
    },
    initPic() {

      //设备OEE饼状图
      var oeePieEchart = this.$echarts.init(this.$refs.oeePieEchart)
      var oeePieOption = {
        title: {
          text: '',
          left: 'center'
        },
        series: [
          {
            type: 'pie',
            radius: '45%',
            //百分比
            label: {
              normal: {
                show: true,
                formatter: '{b}{c}小时({d}%)' //自定义显示格式(b:name, c:value, d:百分比)
              }
            },
            data: []

          }
        ]
      }
      // oeePieOption.series[0].data=responseDate.value;
      // oeePieOption.xAxis.data=responseDate.text;
      oeePieOption && oeePieEchart.setOption(oeePieOption)

      //调用后台接口
      oeeUtilizationList(this.queryParams).then(response => {
        let resData = response.data
        let resYAxis = response.yAxis
        //设备OEE时序图
        var oeeUtilizationEchart = this.$echarts.init(this.$refs.oeeUtilizationEchart)
        // 各状态的颜色
        var colors = ['#909399', '#E6A23C', '#67C23A', '#F56C6C']

        // 四种状态
        var state = ['关机', '待机', '运行中', '故障']
        // echart配置
        var opt = {
          color: colors,
          tooltip: {
            formatter: function(params) {
              return params.name + ':' + (params.value[2] - params.value[1]) + '小时'
            }
          },
          legend: {
            data: state,
            bottom: '1%',
            selectedMode: false, // 图例设为不可点击
            textStyle: {
              color: '#000'
            }
          },
          grid: {
            left: '3%',
            right: '3%',
            top: '1%',
            bottom: '10%',
            containLabel: true
          },
          xAxis: {
            min: 0 // x轴零刻度对应的实际值
          },
          yAxis: {
            data: ['设备一', '设备二', '设备三', '设备四']
          },
          series: [
            // 用空bar来显示四个图例
            { name: state[0], type: 'bar', data: [] },
            { name: state[1], type: 'bar', data: [] },
            { name: state[2], type: 'bar', data: [] },
            { name: state[3], type: 'bar', data: [] },
            {
              type: 'custom',
              renderItem: function(params, api) {
                var categoryIndex = api.value(0)
                var start = api.coord([api.value(1), categoryIndex])
                var end = api.coord([api.value(2), categoryIndex])
                var height = 24

                return {
                  type: 'rect',
                  shape: echarts.graphic.clipRectByRect({
                    x: start[0],
                    y: start[1] - height / 2,
                    width: end[0] - start[0],
                    height: height
                  }, {
                    x: params.coordSys.x,
                    y: params.coordSys.y,
                    width: params.coordSys.width,
                    height: params.coordSys.height
                  }),
                  style: api.style()
                }
              },
              encode: {
                x: [1, 2, 3, 4],
                y: 0
              },
              data: [
                {
                  itemStyle: { normal: { color: colors[0] } },
                  name: '关机',
                  //value: [X轴坐标, 开始数值, 结束数值]
                  value: [0, 0, 10],
                  //设备ID
                  equipmentId: 19950402
                }]
            }
          ]
        }
        opt.yAxis = resYAxis
        opt.series[4].data = resData
        opt && oeeUtilizationEchart.setOption(opt)
        //设备OEE饼状图
        oeeUtilizationEchart.on('click', function(params) {
          let oeePieData =[{
            value: 0,
            name: '关机',
            itemStyle: {
              normal: { color: '#909399' }
            }
          },
            { value: 0, name: '待机', itemStyle: { normal: { color: '#E6A23C' } } },
            { value: 0, name: '运行中', itemStyle: { normal: { color: '#67C23A' } } },
            { value: 0, name: '故障', itemStyle: { normal: { color: '#F56C6C' } } }
          ]
          //获取设备ID
          let i = params.data.equipmentId
          //获取设备名称
          let eIndex = params.value[0];
          let eName = opt.yAxis.data[eIndex];
          oeePieOption.title.text = eName;
          // 将选中的设备,同一状态的数值加起来
          console.log("resData===>",resData)
          for (let j = 0; j < resData.length; j++) {
            let eTmp =  resData[j];
            if(i === eTmp.equipmentId){
              let statusName = eTmp.name;
              if("关机"===statusName){
                oeePieData[0].value +=eTmp.value[2]-eTmp.value[1];
              }
              if("待机"===statusName){
                oeePieData[1].value +=eTmp.value[2]-eTmp.value[1];
              }
              if("运行中"===statusName){
                oeePieData[2].value +=eTmp.value[2]-eTmp.value[1];
              }
              if("故障"===statusName){
                oeePieData[3].value +=eTmp.value[2]-eTmp.value[1];
              }
            }
          }
          oeePieOption.series[0].data=oeePieData;
          oeePieOption && oeePieEchart.setOption(oeePieOption)
        })



      })

    },
    queryParamsInit() {
      // 获取今天
      const end = new Date()
      // 获取上个月
      const start = new Date(
        end.getFullYear(),
        end.getMonth() - 1,
        end.getDate()
      )
      start.setTime(start.getTime())
      this.queryParams = {
        dateQuery: [start.format('yyyy-MM-dd'), end.format('yyyy-MM-dd')]
      }
    }
  }

}
</script>

<style>
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

.container {
  height: 100vh;
  width: 100%;
}

.left-container {
  overflow: hidden;
  position: relative;
  height: 100%;
}
</style>
