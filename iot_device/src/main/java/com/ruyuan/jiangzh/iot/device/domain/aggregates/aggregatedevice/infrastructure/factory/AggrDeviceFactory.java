package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory;

import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceSercetEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrThingEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceSercetRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrThingModelRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceThingCasePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AggrDeviceFactory {

    private AggrDeviceFactory(){}

    @Autowired
    private AggrDeviceRepository deviceRepository;

    @Autowired
    private AggrDeviceSercetRepository deviceSercetRepository;

    @Autowired
    private AggrThingModelRepository thingModelRepository;

    public AggrDeviceEntity getDevice(){
        AggrDeviceSercetEntity deviceSercetEntity = new AggrDeviceSercetEntity();
        AggrDeviceEntity deviceEntity = new AggrDeviceEntity(deviceRepository, deviceSercetRepository, thingModelRepository);
        deviceEntity.setDeviceSercetEntity(deviceSercetEntity);

        return deviceEntity;
    }

    public AggrDeviceEntity getDeviceById(DeviceId deviceId){
        DevicePO devicePO = deviceRepository.findDeviceById(deviceId);
        DeviceSercetInfoPO deviceSercetInfoPO = deviceSercetRepository.findDeviceSercetById(deviceId);

        // 获取设备对应的物模型，并且放入到设备聚合实体中
        Optional<DeviceThingCasePO> thingModelOptional = thingModelRepository.findThingModelByDeviceId(deviceId);
        AggrThingEntity aggrThingEntity = new AggrThingEntity();
        // 设备是有可能没有物模型的
        if(thingModelOptional.isPresent()){
            DeviceThingCasePO thingCasePO = thingModelOptional.get();
            aggrThingEntity.poToEntity(thingCasePO);
        }

        AggrDeviceSercetEntity deviceSercetEntity = new AggrDeviceSercetEntity();
        deviceSercetEntity.poToEntity(deviceSercetInfoPO);

        AggrDeviceEntity deviceEntity = new AggrDeviceEntity(deviceRepository, deviceSercetRepository, thingModelRepository);
        deviceEntity.poToEntity(devicePO);

        deviceEntity.setDeviceSercetEntity(deviceSercetEntity);
        deviceEntity.setThingEntity(aggrThingEntity);

        return deviceEntity;
    }


    public AggrDeviceEntity getDeviceBySercet(String productKey, String deviceName, String deviceSercet){

        DeviceSercetInfoPO deviceSercetInfoPO = deviceSercetRepository.findDeviceSercetByInfo(productKey, deviceName, deviceSercet);
        // TODO 记得加非空验证
        DeviceId deviceId = new DeviceId(UUIDHelper.fromStringId(deviceSercetInfoPO.getUuid()));
        DevicePO devicePO = deviceRepository.findDeviceById(deviceId);
        AggrDeviceSercetEntity deviceSercetEntity = new AggrDeviceSercetEntity();
        deviceSercetEntity.poToEntity(deviceSercetInfoPO);

        AggrDeviceEntity deviceEntity = new AggrDeviceEntity(deviceRepository, deviceSercetRepository, thingModelRepository);
        deviceEntity.poToEntity(devicePO);

        deviceEntity.setDeviceSercetEntity(deviceSercetEntity);

        return deviceEntity;
    }

}
