package com.ruyuan.jiangzh.iot.device.interfaces.controller;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceSercetEntity;
import com.ruyuan.jiangzh.iot.device.domain.vo.ProductId;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;

import com.ruyuan.jiangzh.iot.base.web.BaseController;
import com.ruyuan.jiangzh.iot.base.web.RespDTO;
import com.ruyuan.jiangzh.iot.base.security.IoTSecurityUser;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import com.ruyuan.jiangzh.iot.device.interfaces.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class DeviceController extends BaseController {

    @Autowired
    private AggrDeviceFactory deviceFactory;
    /*
    http://localhost:8082/api/v1/devices?ruyuan_name=ruyuan_00
 */
    @RequestMapping(value = "device", method = RequestMethod.POST)
    public RespDTO saveDevice(@RequestBody DeviceDTO deviceDTO){
        // 验证deviceDTO数据的准确性

        IoTSecurityUser currentUser = getCurrentUser();

        AggrDeviceEntity deviceEntity = deviceFactory.getDevice();
        deviceEntity.setTenantId(currentUser.getTenantId());
        deviceEntity.setUserId(currentUser.getUserId());
        deviceEntity.setProductId(new ProductId(toUUID(deviceDTO.getProductId())));
        deviceEntity.setProductName(deviceDTO.getProductName());
        deviceEntity.setDeviceType(DeviceTypeEnums.getByCode(deviceDTO.getDeviceType()));
        deviceEntity.setDeviceName(deviceDTO.getDeviceName());
        deviceEntity.setCnName(deviceDTO.getCnName());
        deviceEntity.setDeviceStatus(DeviceStatusEnums.NOT_ACTIVE);

        // 跟产品有关的相关参数
        // 跟产品属性有关
        AggrDeviceSercetEntity deviceSercet = deviceEntity.getDeviceSercetEntity();
        deviceSercet.setProductKey("jiangzh");
        deviceSercet.setProductSecret("jiangzh");
        deviceSercet.setAutoActive(false);

        // 直接进行保存操作
        deviceEntity.saveDeviceEntity();

        deviceDTO.setDeviceId(deviceEntity.getId().toString());

        return RespDTO.success(deviceDTO);
    }

    /*
        http://localhost:8082/api/v1/devices?ruyuan_name=ruyuan_00
     */
    @RequestMapping(value = "devices", method = RequestMethod.GET)
    public RespDTO devices(){
        IoTSecurityUser currentUser = getCurrentUser();

        return RespDTO.success(currentUser);
    }

}
