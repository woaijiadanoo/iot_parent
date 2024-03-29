package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt.common;

public class MqttTopics {

    /*
        /sys/a1HVQQL2Lvw/${deviceName}/thing/deviceinfo/update
        /sys/${ProductKey}/${deviceName}/thing/deviceinfo/update
     */
    public static final String BASE_DEVICE_TOPIC_PER = "/sys";

    // 上报标签数据
    public static final String UPLOAD_DEVICE_TAG_TOPIC = "/thing/deviceinfo/update";

    /*
        /sys/a1HVQQL2Lvw/${deviceName}/thing/device/attr/update
     */
    // 更改设备属性数据
    public static final String DEVICE_ATTR_UPDATE_SUB = "/thing/device/attr/update";


    public static String attrDeviceTopic(String productKey, String deviceName){
        // /sys/a1HVQQL2Lvw/${deviceName}/thing/device/attr/update
        return BASE_DEVICE_TOPIC_PER+"/"+productKey+"/"+deviceName+DEVICE_ATTR_UPDATE_SUB;
    }

}
