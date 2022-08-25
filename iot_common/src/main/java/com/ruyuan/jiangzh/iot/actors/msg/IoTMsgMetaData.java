package com.ruyuan.jiangzh.iot.actors.msg;

import com.ruyuan.jiangzh.iot.common.IoTStringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IoTMsgMetaData implements Serializable {

    private final Map<String, String> data = new ConcurrentHashMap<>();

    public IoTMsgMetaData(Map<String, String> data) {
        data.forEach((key,val) -> putValue(key , val));
    }

    public String getValue(String key){
        return data.get(key);
    }

    public void putValue(String key, String value){
        if(IoTStringUtils.isNotBlank(key) && value !=null){
            data.put(key, value);
        }
    }

    public Map<String , String> values(){
        return new HashMap<>(data);
    }

    // 拷贝
    public IoTMsgMetaData copy(){
        return new IoTMsgMetaData(new ConcurrentHashMap<>(data));
    }

}
