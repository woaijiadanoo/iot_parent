package com.ruyuan.jiangzh.iot.device.interfaces.dto;

import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.vo.DeviceId;
import com.ruyuan.jiangzh.iot.device.domain.vo.ProductId;

public class DeviceDTO {

    // 产品相关
    private String productId;

    // 设备相关
    private String deviceId;
    private String deviceName;
    private String cnName;

    public static void dtoToEntity(AggrDeviceEntity entity, DeviceDTO deviceDTO){
        entity.setId(new DeviceId(IoTStringUtils.toUUID(deviceDTO.getDeviceId())));
        entity.setDeviceName(deviceDTO.getDeviceName());
        entity.setCnName(deviceDTO.getCnName());
        entity.setProductId(new ProductId(IoTStringUtils.toUUID(deviceDTO.getProductId())));
    }

    public static DeviceDTO entityToDTO(AggrDeviceEntity entity){
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(entity.getId().toString());
        deviceDTO.setProductId(entity.getProductId().toString());
        deviceDTO.setDeviceName(entity.getDeviceName());
        deviceDTO.setCnName(entity.getCnName());

        return deviceDTO;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }
}
