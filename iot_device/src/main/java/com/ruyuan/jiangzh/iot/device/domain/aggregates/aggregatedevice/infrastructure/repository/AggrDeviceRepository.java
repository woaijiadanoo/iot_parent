package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;

public interface AggrDeviceRepository {

    DevicePO insertDevice(DevicePO devicePO);

}
