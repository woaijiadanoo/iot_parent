package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper.DeviceMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo.DeviceInfosVO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.vo.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.vo.ProductId;
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

    @Override
    public PageDTO<DevicePO> findDevices(PageDTO<DevicePO> pageDTO) {
        return null;
    }

    @Override
    public DeviceInfosVO findDeviceInfos(ProductId productId) {
        return null;
    }

    @Override
    public DevicePO findDeviceById(DeviceId deviceId) {
        return null;
    }

    @Override
    public boolean delDeviceById(DeviceId deviceId) {
        return false;
    }

    @Override
    public boolean updateDeviceStatus(DeviceId deviceId, DeviceStatusEnums deviceStatusEnums) {
        return false;
    }

    @Override
    public boolean activeDevice() {
        return false;
    }

    @Override
    public boolean updateAutoActive(DeviceId deviceId, boolean autoActive) {
        return false;
    }
}
