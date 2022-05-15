package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceSercetEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AggrDeviceFactory {

    private AggrDeviceFactory(){}

    @Autowired
    private AggrDeviceRepository deviceRepository;


    public AggrDeviceEntity getDevice(){
        AggrDeviceSercetEntity deviceSercetEntity = new AggrDeviceSercetEntity();
        AggrDeviceEntity deviceEntity = new AggrDeviceEntity(deviceRepository);

        return deviceEntity;
    }

}
