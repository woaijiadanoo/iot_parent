package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceThingCasePO;

import java.util.Optional;

public interface AggrThingModelRepository {

    void saveThingModel(DeviceThingCasePO thingCase);

    Optional<DeviceThingCasePO> findThingModelByDeviceId(DeviceId deviceId);

}
