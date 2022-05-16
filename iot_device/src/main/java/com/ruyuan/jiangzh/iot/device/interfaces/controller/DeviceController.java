package com.ruyuan.jiangzh.iot.device.interfaces.controller;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceSercetEntity;
import com.ruyuan.jiangzh.iot.device.domain.domainservice.DeviceDomainService;
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

    @Autowired
    private DeviceDomainService deviceDomainService;

    /*
        http://localhost:8082/api/v1/device?ruyuan_name=ruyuan_00

        {
            "productId":"9a97f910-d28f-11ec-a05f-dbd50d7d93eb",
            "deviceName":"ry_device_01",
            "cnName":"儒猿设备第一台"
        }
     */
    @RequestMapping(value = "device", method = RequestMethod.POST)
    public RespDTO saveDevice(@RequestBody DeviceDTO deviceDTO){
        // 验证deviceDTO数据的准确性

        IoTSecurityUser currentUser = getCurrentUser();

        // 所有跟接口层有关的输入参数，在这里进行赋值操作
        AggrDeviceEntity deviceEntity = deviceFactory.getDevice();
        deviceEntity.setTenantId(currentUser.getTenantId());
        deviceEntity.setUserId(currentUser.getUserId());
        deviceEntity.setProductId(new ProductId(toUUID(deviceDTO.getProductId())));
        deviceEntity.setDeviceName(deviceDTO.getDeviceName());
        deviceEntity.setCnName(deviceDTO.getCnName());

        // 直接进行保存操作【domainService的一个最主要的用法，就是同一个限界上下文的不同实体的操作】
        deviceDomainService.saveDeviceEntity(deviceEntity);

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
