package cn.sourceplan.common.mqtt;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.utils.StringUtils;
import cn.sourceplan.equipment.domain.Equipment;
import cn.sourceplan.equipment.mapper.EquipmentMapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jinzhong
 */
@Component
public class MqttConsumerCallBack implements MqttCallback{

    private static EquipmentMapper equipmentMapper;

    @Autowired
    public void setEquipmentMapper(EquipmentMapper equipmentMapper) {
        MqttConsumerCallBack.equipmentMapper = equipmentMapper;
    }

    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("与服务器断开连接，可重连");
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        //返回的数据
        String s = String.format(new String( message.getPayload()));
        if(s.contains("cqs")){
            return;
        }
        System.out.println(String.format("接收消息主题 : %s",topic));
        //通过接收到的消息主题，查找对应的设备,修改对应设备的状态
        QueryWrapper<Equipment> qw = new QueryWrapper<>();
        qw.eq("iot_sn",topic);
        qw.orderByDesc("create_time","update_time");
        List<Equipment> equipmentList = equipmentMapper.selectList(qw);
        //找到符合条件的第一个
        Equipment equipment = equipmentList.get(0);
        //修改他的状态
        int iA = s.indexOf(":");
        String sA = s.substring(iA);
        int iB = sA.indexOf("}");
        s = sA.substring(1, iB);
        if("0".equals(s)){
            //灭灯
            equipment.setEquipmentStatus("A");
        }
        if("1".equals(s)){
            //红灯
            equipment.setEquipmentStatus("D");
        }
        if("2".equals(s)){
            //黄灯
            equipment.setEquipmentStatus("B");
        }
        if("3".equals(s)){
            //绿灯
            equipment.setEquipmentStatus("C");
        }
        equipmentMapper.updateById(equipment);
        //保存该记录
        Map<String,Object> map = new HashMap<>();
        map.put("equipmentId",equipment.getId());
        String oldStatus= equipmentMapper.selectStatusRecord(map);
        //状态改变时再保存
        if(StringUtils.isBlank(oldStatus) ||  !oldStatus.equals(equipment.getEquipmentStatus())){
            map.put("equipmentNumber",equipment.getNumber());
            map.put("equipmentName",equipment.getName());
            map.put("stationName",equipment.getStationName());
            map.put("equipmentStatus",equipment.getEquipmentStatus());
            equipmentMapper.insertStatusRecord(map);
        }
        System.out.println(DateUtil.now()+"   :"+s);
//      System.out.println(String.format("接收消息retained : %b",message.isRetained()));
//      System.out.println(String.format("接收消息Qos : %d",message.getQos()));
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}


