package com.ruyuan.jiangzh.iot.device.domain.domainservice;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;

public interface DeviceDomainService {

    AggrDeviceEntity saveDeviceEntity(AggrDeviceEntity deviceEntity);

}
