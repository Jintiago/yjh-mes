package cn.sourceplan.equipment.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.utils.MesDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.equipment.mapper.EquipmentMapper;
import cn.sourceplan.equipment.domain.Equipment;
import cn.sourceplan.equipment.service.IEquipmentService;

import javax.servlet.http.HttpServletRequest;

/**
 * 设备信息Service业务层处理
 *
 * @author jinzhong
 * @date 2023-04-06
 */
@Service
public class EquipmentServiceImpl implements IEquipmentService
{
    @Autowired
    private EquipmentMapper equipmentMapper;

    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    @Override
    public Equipment selectEquipmentById(Long id)
    {

        return equipmentMapper.selectById(id);
    }

    /**
     * 查询设备信息列表
     *
     * @param equipment 设备信息
     * @return 设备信息
     */
    @Override
    public List<Equipment> selectEquipmentList(Equipment equipment)
    {
        QueryWrapper qw = new QueryWrapper<>(equipment);
        return equipmentMapper.selectList(qw);
    }

    /**
     * 新增设备信息
     *
     * @param equipment 设备信息
     * @return 结果
     */
    @Override
    public int insertEquipment(Equipment equipment)
    {
        return equipmentMapper.insert(equipment);
    }

    /**
     * 修改设备信息
     *
     * @param equipment 设备信息
     * @return 结果
     */
    @Override
    public int updateEquipment(Equipment equipment)
    {
        return equipmentMapper.updateById(equipment);
    }

    /**
     * 批量删除设备信息
     *
     * @param ids 需要删除的设备信息主键
     * @return 结果
     */
    @Override
    public int deleteEquipmentByIds(Long[] ids)
    {
        return equipmentMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除设备信息信息
     *
     * @param id 设备信息主键
     * @return 结果
     */
    @Override
    public int deleteEquipmentById(Long id)
    {
        return equipmentMapper.deleteById(id);
    }

    /**
     * 查询设备OEE稼动率时序图
     *
     */
    @Override
    public JSONObject oeeUtilizationList(HttpServletRequest httpServletRequest) {

        Map<String, Object> stringObjectMap = MesDateUtil.queryDateAddOneDay(httpServletRequest);
        List<Map<String,Object>> list = equipmentMapper.oeeUtilizationList(stringObjectMap);
        JSONArray jsonArray = new JSONArray();
        //yAxis  Y轴 data: ['设备一', '设备二', '设备三', '设备四']
        List<String> yAxisList = new ArrayList<>();
        String yAxisTmp = "";
        //当前时间
        Date oldTime = DateUtil.date();
        long minTmp = 0;
        //设备ID分组
        Long eIdTmp = null;
        //设备索引
        int eIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            //拿到数据库数据
            Map<String, Object> dbMap = list.get(i);
            Long equipmentId = (Long)dbMap.get("equipment_id");
            String equipmentName = (String)dbMap.get("equipment_name");
            String equipmentStatus = (String)dbMap.get("equipment_status");
            LocalDateTime recordTime = (LocalDateTime )dbMap.get("record_time");
            String format = recordTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Date dbTime = DateUtil.parse(format, "yyyy-MM-dd HH:mm:ss");

            //封装数据  格式为下面  colors[0] -[3] 分别代表  关机，待机，运行中，故障
            //value[A，B，C] 分别代表设备ID索引,开始数值，结束数值
            /*{
                itemStyle: { normal: { color: colors[0] } },
                name: '关机',
                //value: [X轴坐标, 开始数值, 结束数值]
                value: [0, 0, 10],
                //设备ID
                equipmentId:19950402
            },*/
            if(!equipmentId.equals(eIdTmp)){
                //如果不为同一设备，将minTmp清零,并将设备索引加1
                oldTime=DateUtil.date();
                minTmp=0;
                eIndex+=1;
            }
            eIdTmp=equipmentId;
            //封装数据
            JSONObject jsonObject = new JSONObject();
                    JSONObject normal =new JSONObject();
                    normal.put("color","A".equals(equipmentStatus)? "#909399":
                            "B".equals(equipmentStatus)?"#E6A23C":
                             "C".equals(equipmentStatus)?"#67C23A":
                             "D".equals(equipmentStatus)?"#F56C6C":"#909399");
                    JSONObject itemStyle =new JSONObject();
                itemStyle.put("normal",normal);
            jsonObject.put("itemStyle",itemStyle);
            jsonObject.put("name","A".equals(equipmentStatus)? "关机":
                                "B".equals(equipmentStatus)?"待机":
                                "C".equals(equipmentStatus)?"运行中":
                                 "D".equals(equipmentStatus)?"故障":"关机");
            //将新日期减旧日期,再转换为分钟
            long between = DateUtil.between(oldTime,dbTime , DateUnit.HOUR);
            long[] value = new long[]{eIndex,minTmp-between,minTmp};
            oldTime = dbTime;
            minTmp -=between;
            jsonObject.put("value",value);
            jsonObject.put("equipmentId",equipmentId);
            jsonArray.add(jsonObject);
            if(!yAxisTmp.equals(equipmentName)){
                yAxisList.add(equipmentName);
                yAxisTmp=equipmentName;
            }

        }
        //封装Y轴数据
        JSONObject  yAxis = new JSONObject();
        yAxis.put("data",yAxisList.toArray());
        //封装最终数据
        JSONObject resultJson = new JSONObject();
        // 倒转
        resultJson.put("data",reverseJsonArray(jsonArray));
        resultJson.put("yAxis",yAxis);
        return resultJson;
    }


    private JSONArray reverseJsonArray(JSONArray jsonArray){

        long tmpL = 0;
        Long eIdTmp = null;
        for (int i = jsonArray.size()-1; i >=0; i--) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Long equipmentId = (Long)jsonObject.get("equipmentId");
            if(!equipmentId.equals(eIdTmp)){
                tmpL=0;
            }
            long[] longArr = (long[]) jsonObject.get("value");
            long a = Math.abs(longArr[1]) ;
            long b = Math.abs(longArr[2]);
            longArr[1] = tmpL;
            longArr[2] = a-b+tmpL;
            tmpL= longArr[2];
            jsonObject.put("value",longArr);
            eIdTmp=equipmentId;
        }

        return jsonArray;
    }
}
