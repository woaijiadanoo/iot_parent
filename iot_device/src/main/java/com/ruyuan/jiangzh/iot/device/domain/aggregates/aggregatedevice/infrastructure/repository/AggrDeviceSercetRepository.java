package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository;

import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DeviceSercetInfoPO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.vo.DeviceId;

public interface AggrDeviceSercetRepository {

    DeviceSercetInfoPO insertEntity(DeviceSercetInfoPO deviceSercetInfoPO);

    /*
        根据主键查询设备详细情况
     */
    DeviceSercetInfoPO findDeviceSercetById(DeviceId deviceId);

    /*
        根据主键删除设备
     */
    boolean delDeviceSercetById(DeviceId deviceId);

    /*
        通过三元组信息查询设备连接所需的信息
     */
    DeviceSercetInfoPO findDeviceSercetByInfo(
            String productSercet,String deviceName,String deviceSercet);

    /*
            修改设备启用/禁用状态
         */
    boolean updateDeviceStatus(DeviceId deviceId, DeviceStatusEnums deviceStatusEnums);

    /*
        修改自动注册状态
     */
    boolean updateAutoActive(DeviceId deviceId, boolean autoActive);

}
