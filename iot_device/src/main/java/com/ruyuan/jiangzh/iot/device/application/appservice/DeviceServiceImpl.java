package com.ruyuan.jiangzh.iot.device.application.appservice;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import com.ruyuan.jiangzh.service.dto.DeviceSercetDTO;
import com.ruyuan.jiangzh.service.sdk.DeviceServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "deviceServiceImpl")
public class DeviceServiceImpl implements DeviceServiceAPI {

    @Autowired
    private AggrDeviceFactory deviceFactory;

    @Override
    public DeviceSercetDTO findDeviceBySercet(String productKey, String deviceName, String deviceSercet) {
        AggrDeviceEntity deviceEntity = deviceFactory.getDeviceBySercet(productKey, deviceName, deviceSercet);

        // 这里应该加入deviceEntity和deviceEntity.getDeviceSercetEntity为空的判断，如果有任何一个为空，则直接返回空

        DeviceSercetDTO deviceSercetDTO = new DeviceSercetDTO();
        deviceSercetDTO.setDeviceId(deviceEntity.getId().toString());
        deviceSercetDTO.setProductId(deviceEntity.getProductId().toString());
        deviceSercetDTO.setProductKey(deviceEntity.getDeviceSercetEntity().getProductKey());
        deviceSercetDTO.setDeviceName(deviceEntity.getDeviceName());
        deviceSercetDTO.setDeviceSercet(deviceEntity.getDeviceSercetEntity().getDeviceSecret());
        deviceSercetDTO.setProductSecret(deviceEntity.getDeviceSercetEntity().getProductSecret());
        deviceSercetDTO.setAutoActive(deviceEntity.getDeviceSercetEntity().getAutoActive());
        deviceSercetDTO.setDeviceStatus(deviceEntity.getDeviceStatus().getCode());

        return deviceSercetDTO;
    }
}
