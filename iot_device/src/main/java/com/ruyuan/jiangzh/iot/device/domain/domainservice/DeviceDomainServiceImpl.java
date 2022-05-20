package com.ruyuan.jiangzh.iot.device.domain.domainservice;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceSercetEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import com.ruyuan.jiangzh.iot.device.domain.entity.ProductEntity;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceTypeEnums;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.repository.ProductRepository;
import com.ruyuan.jiangzh.iot.device.domain.vo.ProductId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceDomainServiceImpl implements DeviceDomainService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AggrDeviceFactory deviceFactory;

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

    @Override
    public boolean updateAutoActive(ProductId productId, boolean autoActive) {
        // 根据productId获取product
        ProductEntity productEntity = productRepository.findProductById(productId);
        // 修改产品的自动激活状态
        productRepository.updateAutoActive(productId, autoActive);

        /*
            背景：修改所有的productId下的deviceSercet的autoActive状态
            1、面向对象：通过productId获取所有的device聚合，然后修改状态并保存至持久化存储
            2、面向数据：把product唯一标识传入，直接update所有deviceSercet
         */
        // 先获取productKey
        String productKey = productEntity.getProductKey();

        AggrDeviceEntity device = deviceFactory.getDevice();
        device.updateAutoActive(productKey, autoActive);

        return true;
    }
}
