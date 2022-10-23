package com.ruyuan.jiangzh.iot.actors.msg.messages;

import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.device.FromDeviceMsg;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.device.ProductId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class FromDeviceOnlineMsg implements FromDeviceMsg {

    private DeviceId deviceId;

    private TenantId tenantId;

    private UserId userId;

    private ProductId productId;

    // 长链接对应的容器地址
    private ServerAddress serverAddress;

    private long onlineTime;

    @Override
    public MsgType getMsgType() {
        return MsgType.PROTOCOL_ONLINE_MSG;
    }


    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public ServerAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    @Override
    public String toString() {
        return "FromDeviceOnlineMsg{" +
                "deviceId=" + deviceId +
                ", tenantId=" + tenantId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", serverAddress=" + serverAddress +
                ", onlineTime=" + onlineTime +
                '}';
    }
}
