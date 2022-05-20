package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceSercetEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceSercetRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
import com.ruyuan.jiangzh.iot.device.domain.vo.DeviceId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AggrDeviceFactory {

    private AggrDeviceFactory(){}

    @Autowired
    private AggrDeviceRepository deviceRepository;

    @Autowired
    private AggrDeviceSercetRepository deviceSercetRepository;

    public AggrDeviceEntity getDevice(){
        AggrDeviceSercetEntity deviceSercetEntity = new AggrDeviceSercetEntity();
        AggrDeviceEntity deviceEntity = new AggrDeviceEntity(deviceRepository, deviceSercetRepository);
        deviceEntity.setDeviceSercetEntity(deviceSercetEntity);

        return deviceEntity;
    }

    public AggrDeviceEntity getDeviceById(DeviceId deviceId){
        DevicePO devicePO = deviceRepository.findDeviceById(deviceId);
        DeviceSercetInfoPO deviceSercetInfoPO = deviceSercetRepository.findDeviceSercetById(deviceId);

        AggrDeviceSercetEntity deviceSercetEntity = new AggrDeviceSercetEntity();
        deviceSercetEntity.poToEntity(deviceSercetInfoPO);

        AggrDeviceEntity deviceEntity = new AggrDeviceEntity(deviceRepository, deviceSercetRepository);
        deviceEntity.poToEntity(devicePO);

        deviceEntity.setDeviceSercetEntity(deviceSercetEntity);

        return deviceEntity;
    }

}
