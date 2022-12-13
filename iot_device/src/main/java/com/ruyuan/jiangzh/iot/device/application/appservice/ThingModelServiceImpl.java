package com.ruyuan.jiangzh.iot.device.application.appservice;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruyuan.jiangzh.iot.actors.msg.rule.vo.KeyValueProtoVO;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrThingEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import com.ruyuan.jiangzh.service.dto.ThingModelTransportDTO;
import com.ruyuan.jiangzh.service.dto.ThingModelType;
import com.ruyuan.jiangzh.service.sdk.ThingModelServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(value = "thingModelServiceImpl")
public class ThingModelServiceImpl implements ThingModelServiceAPI {

    private static final String IDENTIFIER_KEY_NAME = "identifier";
    private static final String TYPE_KEY_NAME = "type";
    private static final String VALUE_KEY_NAME = "value";

    @Autowired
    private AggrDeviceFactory deviceFactory;

    @Override
    public String transportToThingModel(ThingModelTransportDTO dto) {
        String result = "";
        // 通过DeviceID获取对应的Device实体
        DeviceId deviceId = dto.getDeviceId();
        if(deviceId == null){
            return result;
        }

        AggrDeviceEntity deviceEntity = deviceFactory.getDeviceById(deviceId);
        if(deviceEntity ==null || deviceEntity.getId() == null || deviceEntity.getThingEntity() == null){
            return result;
        }

        AggrThingEntity thingEntity = deviceEntity.getThingEntity();
        ThingModelType type = ThingModelType.getByCode(dto.getThingModelTypeCode());
        switch (type) {
            case SHADOW:
                return result;
            case PROPERTIES:
                // 解析properties的物模型
                return parseProperties(thingEntity.getPropertiesJsonStr(), dto.getKvs());
            case EVENTS:
                return result;
            case SERVICES:
                return result;
            default:
                return result;
        }


    }

    // 解析properties的物模型
    private String parseProperties(String propertiesJsonStr, List<KeyValueProtoVO> kvs) {
        // 转换对象
        JsonArray propertyJsonArray = str2JsonArray(propertiesJsonStr);
        Map<String, KeyValueProtoVO> kvsMap = saveKVs(kvs);
        JsonArray resultJsonArray = new JsonArray();
        // 根据identifier获取对应的上传的数据中的KeyValueProtoVO
        for(JsonElement property : propertyJsonArray){
            JsonObject propertyJsonObject = property.getAsJsonObject();
            JsonElement identifierEle = propertyJsonObject.get(IDENTIFIER_KEY_NAME);
            if(property.isJsonNull()){
               continue;
            }

            JsonObject identifierJsonObject = identifierEle.getAsJsonObject();
            String identifier = identifierJsonObject.getAsString();

            if(kvsMap.containsKey(identifier)){
                resultJsonArray.add(protoTransport(kvsMap.get(identifier), propertyJsonObject));
            }

        }

        return null;
    }

    private JsonObject protoTransport(KeyValueProtoVO protoVO, JsonObject jsonObject) {
        // TODO 验证的过程， 是根据物模型的规则进行验证

        // 处理返回值
        JsonObject result = new JsonObject();
        result.addProperty(IDENTIFIER_KEY_NAME, protoVO.getKey());
        result.addProperty(TYPE_KEY_NAME, protoVO.getType().getTypeCode());
        result.addProperty(VALUE_KEY_NAME, getProtoValue(protoVO));

        return result;
    }

    private String getProtoValue(KeyValueProtoVO vo) {
        switch (vo.getType()) {
            case BOOLEAN_V:
               return vo.getBoolValue() + "";
            case LONG_V:
                return vo.getLongValue() + "";
            case DOUBLE_V:
                return vo.getDoubleValue() + "";
            case STRING_V:
                return vo.getStringValue();
            default:
                return null;
        }
    }

    // 缓存整个List，用来查询使用
    private Map<String, KeyValueProtoVO> saveKVs(List<KeyValueProtoVO> kvs){
        Map<String, KeyValueProtoVO> result = Maps.newHashMap();

        for(KeyValueProtoVO vo : kvs){
            result.put(vo.getKey(), vo);
        }

        return result;
    }

    private JsonArray str2JsonArray(String arrayStr) {
        JsonElement rootElement = new JsonParser().parse(arrayStr);
        if(!rootElement.isJsonNull()){
            if(rootElement.isJsonArray()){
                return rootElement.getAsJsonArray();
            }
        }
        return null;
    }


}
