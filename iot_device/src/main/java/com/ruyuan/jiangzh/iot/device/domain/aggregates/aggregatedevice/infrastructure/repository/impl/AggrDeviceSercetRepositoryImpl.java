package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.AggrDeviceSercetRepository;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.impl.mapper.DeviceSercetInfoMapper;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public DeviceSercetInfoPO findDeviceSercetById(DeviceId deviceId) {
        String dataId = UUIDHelper.fromTimeUUID(deviceId.getUuid());
        DeviceSercetInfoPO deviceSercetInfoPO = deviceSercetInfoMapper.selectById(dataId);
        return deviceSercetInfoPO;
    }

    @Override
    public boolean delDeviceSercetById(DeviceId deviceId) {
        String dataId = UUIDHelper.fromTimeUUID(deviceId.getUuid());
        deviceSercetInfoMapper.deleteById(dataId);
        return true;
    }

    @Override
    public DeviceSercetInfoPO findDeviceSercetByInfo(String productSercet, String deviceName, String deviceSercet) {
        QueryWrapper<DeviceSercetInfoPO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_key", productSercet);
        queryWrapper.eq("device_name", deviceName);
        queryWrapper.eq("device_secret", deviceSercet);
        queryWrapper.in("device_status",
                DeviceStatusEnums.NOT_ACTIVE.getCode()
                ,DeviceStatusEnums.ACTIVED.getCode()
                ,DeviceStatusEnums.OFFLINE.getCode()
                ,DeviceStatusEnums.ONLINE.getCode()
        );

        List<DeviceSercetInfoPO> deviceSercetInfoPOS = deviceSercetInfoMapper.selectList(queryWrapper);
        if(deviceSercetInfoPOS != null && deviceSercetInfoPOS.size() > 0){
            return deviceSercetInfoPOS.stream().findFirst().get();
        }
        return null;
    }

    @Override
    public boolean updateDeviceStatus(DeviceId deviceId, DeviceStatusEnums deviceStatusEnums) {
        DeviceSercetInfoPO po = new DeviceSercetInfoPO();
        po.setUuid(UUIDHelper.fromTimeUUID(deviceId.getUuid()));
        po.setDeviceStatus(deviceStatusEnums.getCode());

        deviceSercetInfoMapper.updateById(po);

        return true;
    }

    @Override
    public boolean updateAutoActive(DeviceId deviceId, boolean autoActive) {
        DeviceSercetInfoPO po = new DeviceSercetInfoPO();
        po.setUuid(UUIDHelper.fromTimeUUID(deviceId.getUuid()));
        po.setAutoActive(autoActive);

        deviceSercetInfoMapper.updateById(po);

        return true;
    }

    @Override
    public boolean updateAutoActiveByProductKey(String productKey, boolean autoActive){
        UpdateWrapper<DeviceSercetInfoPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("product_key", productKey);

        // 根据条件修改数据库信息
        DeviceSercetInfoPO deviceSercetInfoPO = new DeviceSercetInfoPO();
        deviceSercetInfoPO.setAutoActive(autoActive);
        deviceSercetInfoMapper.update(deviceSercetInfoPO, updateWrapper);

        return true;
    }

}
