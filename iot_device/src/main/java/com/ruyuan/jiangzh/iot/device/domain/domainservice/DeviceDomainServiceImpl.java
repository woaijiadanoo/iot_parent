package com.ruyuan.jiangzh.iot.device.domain.domainservice;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceSercetEntity;
import com.ruyuan.jiangzh.iot.device.domain.entity.ProductEntity;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceDomainServiceImpl implements DeviceDomainService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public AggrDeviceEntity saveDeviceEntity(AggrDeviceEntity deviceEntity) {
        // 根据productId获取product
        ProductEntity productEntity = productRepository.findProductById(deviceEntity.getProductId());

        // 跟产品有关的相关参数
        deviceEntity.setProductName(productEntity.getProductName());
        deviceEntity.setDeviceType(productEntity.getDeviceType());
        // 跟产品属性有关
        AggrDeviceSercetEntity deviceSercet = deviceEntity.getDeviceSercetEntity();
        deviceSercet.setProductKey(productEntity.getProductKey());
        deviceSercet.setProductSecret(productEntity.getProductSecret());
        deviceSercet.setAutoActive(productEntity.isAutoActive());
        // 跟设备有关的
        deviceSercet.setDeviceName(deviceEntity.getDeviceName());

        // 执行保存操作
        deviceEntity.saveDeviceEntity();

        return deviceEntity;
    }
}
