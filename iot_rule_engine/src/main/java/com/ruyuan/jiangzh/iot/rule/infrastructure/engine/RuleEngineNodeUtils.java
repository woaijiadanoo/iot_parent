package com.ruyuan.jiangzh.iot.rule.infrastructure.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineMsgMetaData;

import java.util.Map;

public class RuleEngineNodeUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String VARIABLE_TEMPLATE="${%s}";

    public static<T> T convert(RuleEngineNodeConfiguration configuration, Class<T> clazz){
        try {
            return mapper.treeToValue(configuration.getData(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        pattern : Hi，${name}， What‘s up?
     */
    public static String processPattern(String pattern, RuleEngineMsgMetaData metaData){
        String result = new String(pattern);
        for(Map.Entry<String,String> entry :  metaData.values().entrySet()){
            result = processVar(result, entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static String processVar(String pattern, String key, String val){
        String varPattern = String.format(VARIABLE_TEMPLATE, key);
        return pattern.replace(varPattern, val);
    }

}
