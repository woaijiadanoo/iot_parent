package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo.DeviceInfosVO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;

public interface AggrDeviceRepository {

    /*
        新增设备
     */
    DevicePO insertDevice(DevicePO devicePO);

    /*
        查询设备列表
     */
    PageDTO<DevicePO> findDevices(PageDTO<DevicePO> pageDTO);

    /*
        设备总体情况
     */
    DeviceInfosVO findDeviceInfos(ProductId productId);

    /*
        根据主键查询设备详细情况
     */
    DevicePO findDeviceById(DeviceId deviceId);

    /*
        根据主键删除设备
     */
    boolean delDeviceById(DeviceId deviceId);

    /*
        修改设备启用/禁用状态
     */
    boolean updateDeviceStatus(DeviceId deviceId, DeviceStatusEnums deviceStatusEnums);

    /*
        修改状态及在线时间 - connect 和 disconnect
     */
    boolean updateDeviceStatusAndOnlineTime(
            DeviceId deviceId, DeviceStatusEnums deviceStatusEnums, Long onlineTimestamp);

}
