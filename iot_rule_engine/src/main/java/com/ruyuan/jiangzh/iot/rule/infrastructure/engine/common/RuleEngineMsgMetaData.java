package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common;

import com.ruyuan.jiangzh.iot.common.IoTStringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuleEngineMsgMetaData implements Serializable {

    private final Map<String, String> data = new ConcurrentHashMap<>();

    public RuleEngineMsgMetaData(Map<String, String> data) {
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
    public RuleEngineMsgMetaData copy(){
        return new RuleEngineMsgMetaData(new ConcurrentHashMap<>(data));
    }

}
