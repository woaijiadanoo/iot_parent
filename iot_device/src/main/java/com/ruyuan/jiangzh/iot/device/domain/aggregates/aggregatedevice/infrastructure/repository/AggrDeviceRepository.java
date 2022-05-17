package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.repository.po.DevicePO;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo.DeviceInfosVO;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;
import com.ruyuan.jiangzh.iot.device.domain.vo.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.vo.ProductId;

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
        TODO， 物模型模块时会处理这一部分。激活设备
     */
    boolean activeDevice();


    /*
        修改自动注册状态
     */
    boolean updateAutoActive(DeviceId deviceId, boolean autoActive);

}
