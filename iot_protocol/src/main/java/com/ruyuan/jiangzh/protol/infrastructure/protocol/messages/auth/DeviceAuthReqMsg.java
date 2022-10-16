package com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.auth;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: 设备鉴权请求消息体
 */
public class DeviceAuthReqMsg {

    private final String productKey;

    private final String deviceName;

    private final String deviceSecret;

    public DeviceAuthReqMsg(
            String productKey,String deviceName, String deviceSecret){
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.deviceSecret = deviceSecret;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

}
