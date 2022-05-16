package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper.DeviceMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AggrDeviceRepositoryImpl implements AggrDeviceRepository {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public DevicePO insertDevice(DevicePO devicePO) {
        // 数据验证

        // 数据填充

        deviceMapper.insert(devicePO);

        return devicePO;
    }
}
