package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceSercetRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper.DeviceSercetInfoMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.vo.DeviceId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AggrDeviceSercetRepositoryImpl implements AggrDeviceSercetRepository {

    @Resource
    private DeviceSercetInfoMapper deviceSercetInfoMapper;

    @Override
    public DeviceSercetInfoPO insertEntity(DeviceSercetInfoPO deviceSercetInfoPO) {
        // po的数据验证

        deviceSercetInfoMapper.insert(deviceSercetInfoPO);

        return deviceSercetInfoPO;
    }

    @Override
    public DeviceSercetInfoPO findDeviceSercetById(DeviceId deviceId) {
        return null;
    }

    @Override
    public boolean delDeviceSercetById(DeviceId deviceId) {
        return false;
    }

    @Override
    public DeviceSercetInfoPO findDeviceSercetByInfo(String productSercet, String deviceName, String deviceSercet) {
        return null;
    }

    @Override
    public boolean updateDeviceStatus(DeviceId deviceId, DeviceStatusEnums deviceStatusEnums) {
        return false;
    }

    @Override
    public boolean updateAutoActive(DeviceId deviceId, boolean autoActive) {
        return false;
    }
}
