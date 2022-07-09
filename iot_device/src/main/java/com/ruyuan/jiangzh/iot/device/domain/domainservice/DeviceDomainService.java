package com.ruyuan.jiangzh.iot.device.domain.domainservice;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;

public interface DeviceDomainService {

    AggrDeviceEntity saveDeviceEntity(AggrDeviceEntity deviceEntity);

    boolean updateAutoActive(ProductId productId, boolean autoActive);

}
