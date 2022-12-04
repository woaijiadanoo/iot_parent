package com.ruyuan.jiangzh.iot.device.interfaces.controller;

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
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class ThingController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AggrDeviceFactory deviceFactory;

    private static final String JSON_FORMAT_ERROR = "thing.thing_format_error";

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

}
