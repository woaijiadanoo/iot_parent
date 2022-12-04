package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceThingCasePO;

public interface AggrThingModelRepository {

    void saveThingModel(DeviceThingCasePO thingCase);

}
