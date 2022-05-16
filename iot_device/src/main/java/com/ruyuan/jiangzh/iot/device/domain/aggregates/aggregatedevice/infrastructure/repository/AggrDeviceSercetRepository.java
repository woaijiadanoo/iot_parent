package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;

public interface AggrDeviceSercetRepository {

    DeviceSercetInfoPO insertEntity(DeviceSercetInfoPO deviceSercetInfoPO);

}
