package com.ruyuan.jiangzh.iot.device.interfaces.controller;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespCodeEnum;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.common.AuthorityRole;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrThingEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class ThingController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AggrDeviceFactory deviceFactory;

    private static final String JSON_FORMAT_ERROR = "thing.thing_format_error";


    /*
        http://localhost:8082/api/v1/productId/{productId}/deviceId/{deviceId}/thing:save?ruyuan_name=ruyuan_00

        http://localhost:8082/api/v1/productId/9a97f910-d28f-11ec-a05f-dbd50d7d93eb/deviceId/5ad5f8d0-d90c-11ec-9f6a-7fff0967ec80/thing:save?ruyuan_name=ruyuan_00

        见附件
     */
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/productId/{productId}/deviceId/{deviceId}/thing:save", method = RequestMethod.POST)
    public RespDTO saveThingModel(
            @PathVariable("productId") String productIdStr,
            @PathVariable("deviceId") String deviceIdStr,
            @RequestBody String thingModelJsonStr){
        try {
            DeviceId deviceId = new DeviceId(toUUID(deviceIdStr));
            ProductId productId = new ProductId(toUUID(productIdStr));
            AggrDeviceEntity deviceEntity = deviceFactory.getDeviceById(deviceId);
            IoTSecurityUser currentUser = getCurrentUser();
            if(!deviceEntity.getProductId().equals(productId)){
                // 应该给一个权限不足， 或者没有数据的提示[AppException]
            }
            if(currentUser.getAuthorityRole().equals(AuthorityRole.TENANT_ADMIN)){
                if(!deviceEntity.getTenantId().equals(currentUser.getTenantId())){
                    // 应该给一个权限不足， 或者没有数据的提示[AppException]
                }
            }else if(currentUser.getAuthorityRole().equals(AuthorityRole.USER)){
                // 应该给一个权限不足， 或者没有数据的提示[AppException]
            }


            JsonElement thingModelJsonElement = new JsonParser().parse(thingModelJsonStr);

            // 判断json是否有效
            if(!thingModelJsonElement.isJsonNull()){
                // 具体保存物模型的逻辑
                deviceEntity.saveThingModel(thingModelJsonElement);
            }else{
                throw new AppException(RespCodeEnum.PARAM_IS_NULL.getCode(), JSON_FORMAT_ERROR);
            }

        } catch (JsonParseException e){
            throw new AppException(RespCodeEnum.PARAM_IS_NULL.getCode(), JSON_FORMAT_ERROR);
        }
        return RespDTO.success();
    }


    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN', 'USER')")
    @RequestMapping(value = "/productId/{productId}/deviceId/{deviceId}/thing", method = RequestMethod.GET)
    public RespDTO queryThingModel(
            @PathVariable("productId") String productIdStr,
            @PathVariable("deviceId") String deviceIdStr){
        DeviceId deviceId = new DeviceId(toUUID(deviceIdStr));
        ProductId productId = new ProductId(toUUID(productIdStr));

        AggrDeviceEntity deviceEntity = deviceFactory.getDeviceById(deviceId);
        Map<String, String> result = Maps.newHashMap();

        if(deviceEntity != null && deviceEntity.getThingEntity() != null){
            AggrThingEntity thingEntity = deviceEntity.getThingEntity();

            result.put("shadow", thingEntity.getShadowJsonStr());
            result.put("properties", thingEntity.getPropertiesJsonStr());
            result.put("services", thingEntity.getServicesJsonStr());
            result.put("events", thingEntity.getEventsJsonStr());
        }

        return RespDTO.success(result);
    }

}
