package com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.vo;

public class DeviceInfosVO {

    private Long totalDevices;
    private Long activeDevices;
    private Long onlineDevices;

    public Long getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(Long totalDevices) {
        this.totalDevices = totalDevices;
    }

    public Long getActiveDevices() {
        return activeDevices;
    }

    public void setActiveDevices(Long activeDevices) {
        this.activeDevices = activeDevices;
    }

    public Long getOnlineDevices() {
        return onlineDevices;
    }

    public void setOnlineDevices(Long onlineDevices) {
        this.onlineDevices = onlineDevices;
    }
}
