package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl;

import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceSercetRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper.DeviceSercetInfoMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
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
}
