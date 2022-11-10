package com.ruyuan.jiangzh.protol.infrastructure.protocol.common;

import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.PostTelemetryMsg;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.vo.KeyValueProtoVO;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.vo.KeyValueType;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.vo.TsKvListProtoVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.checkerframework.checker.units.qual.K;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonConverter {

    private static int maxStringValueLength = 0;

    /*
            给后面的内容做预留

            {"key":"testKey","value":"testValue"}
            PostTelemetryMsg
                kvproto
                    timestamp
                    kv -> map

            PostTelemetryMsg
                kvproto
                    timestamp : 1293812398123
                    kv
                        key - > testKey
                        type -> long
                        value -> 40

            {"key":"testKey","value": 40}
         */
    public static PostTelemetryMsg convertToTelemetryMsg(JsonElement jsonObject) throws JsonSyntaxException {
        long reviceTime = System.currentTimeMillis();
        PostTelemetryMsg postTelemetryMsg  = new PostTelemetryMsg();
        if(jsonObject.isJsonObject()){
            parseObject(postTelemetryMsg, reviceTime, jsonObject);
        }else if(jsonObject.isJsonArray()){
            jsonObject.getAsJsonArray().forEach(je -> {
                if(je.isJsonObject()){
                    parseObject(postTelemetryMsg, reviceTime, jsonObject);
                }else{
                    throw new JsonSyntaxException("Can't parse value : " + jsonObject);
                }
            });
        } else {
            throw new JsonSyntaxException("Can't parse value : " + jsonObject);
        }

        return postTelemetryMsg;
    }

    private static void parseObject(PostTelemetryMsg postTelemetryMsg, long reviceTime, JsonElement jsonObject) {
        // 将jsonObject转换成JsonObject
        JsonObject input = jsonObject.getAsJsonObject();
        // 组织TsKvListProtoVO对象\
        addTsKvList(postTelemetryMsg, reviceTime, input);
    }

    /*
        {"name": "Allen", "age": 18}
        KeyValueProtoVO : name Allen
        KeyValueProtoVO : age 18

     */
    private static void addTsKvList(PostTelemetryMsg postTelemetryMsg, long reviceTime, JsonObject input) {
        // 组织 TsKvListProtoVO 对象
        TsKvListProtoVO tsKvListProtoVO = new TsKvListProtoVO();

        // 写入接收时间
        tsKvListProtoVO.setReviceTime(reviceTime);

        // 将json的内容转换成KeyValueProtoVO 的集合
        List<KeyValueProtoVO> keyValueProtos = parseKeyValueProtoVO(input.getAsJsonObject());
        tsKvListProtoVO.setKvs(keyValueProtos);

        // 将 TsKvListProtoVO 对象放入PostTelemetryMsg
        postTelemetryMsg.setKvList(tsKvListProtoVO);
    }

    private static List<KeyValueProtoVO> parseKeyValueProtoVO(JsonObject input) {
        List<KeyValueProtoVO> result = Lists.newArrayList();
        if(input.isJsonNull()){
           return result;
        }

        for(Map.Entry<String, JsonElement> valueEntry : input.entrySet()){
            JsonElement element = valueEntry.getValue();
            if(element.isJsonPrimitive()){
                JsonPrimitive value = element.getAsJsonPrimitive();
                if(value.isString()){
                    // 数据长度过长，返回异常信息
                    if(maxStringValueLength > 0 && value.getAsString().length() > maxStringValueLength){
                        String message = "The data is too large, now data size : " + value.getAsString().length() + " , limit : "+ maxStringValueLength;
                        throw new JsonSyntaxException(message);
                    }
                    // 看是否可以转换为数字类型
                    if(NumberUtils.isParsable(value.getAsString())){
                        try {
                            result.add(buildNumberKeyValueProtoVO(value, valueEntry.getKey()));
                        } catch (Exception e){
                            // 容错功能的实现
                            result.add(new KeyValueProtoVO.NewBuilder()
                                    .setKey(valueEntry.getKey())
                                    .setType(KeyValueType.STRING_V)
                                    .setStringValue(value.getAsString()).build());
                        }
                    } else {
                        // 如果输入的是字符串类型
                        result.add(new KeyValueProtoVO.NewBuilder()
                                .setKey(valueEntry.getKey())
                                .setType(KeyValueType.STRING_V)
                                .setStringValue(value.getAsString()).build());
                    }
                    // boolean 类型的处理
                } else if(value.isBoolean()){
                    result.add(new KeyValueProtoVO.NewBuilder()
                            .setKey(valueEntry.getKey())
                            .setType(KeyValueType.BOOLEAN_V)
                            .setBoolValue(value.getAsBoolean()).build());
                } else if (value.isNumber()) {
                    result.add(buildNumberKeyValueProtoVO(value, valueEntry.getKey()));
                } else {
                    // 无法识别的类型
                    throw new JsonSyntaxException("Can't parse value : " + value);
                }

            } else {
                // 无法识别的类型
                throw new JsonSyntaxException("Can't parse value : " + element);
            }
        }

        return result;
    }

    private static KeyValueProtoVO buildNumberKeyValueProtoVO(JsonPrimitive value, String key) {
        // 数字暂时就区分成两种  double -> 0.32  long -> 6565656
        // double 有 .
        if(value.getAsString().contains(".")){
            return new KeyValueProtoVO.NewBuilder()
                    .setKey(key)
                    .setType(KeyValueType.DOUBLE_V)
                    .setDoubleValue(value.getAsDouble())
                    .build();
        } else {
            try {
                long longValue = Long.parseLong(value.getAsString());
                return new KeyValueProtoVO.NewBuilder()
                        .setKey(key)
                        .setType(KeyValueType.LONG_V)
                        .setLongValue(longValue)
                        .build();
            } catch (RuntimeException ex){
                throw new JsonSyntaxException(" Big integer values are not supported ! ");
            }

        }
    }


    public static void setMaxStringValueLength(int maxLength) {
        maxStringValueLength = maxLength;
    }


}
