package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import com.google.gson.Gson;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.messages.FromDeviceOnlineMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ToDeviceSessionEventMsg;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.entity.AggrDeviceEntity;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import com.ruyuan.jiangzh.iot.device.domain.infrastructure.enums.DeviceStatusEnums;

public class DeviceActor extends ContextAwareActor {

    private final TenantId tenantId;
    private final DeviceId deviceId;

    private final ServerAddress serverAddress;

    private AggrDeviceFactory deviceFactory;

    public DeviceActor(ActorSystemContext actorSystemContext,
                       TenantId tenantId,DeviceId deviceId,ServerAddress serverAddress) {
        super(actorSystemContext);
        this.tenantId = tenantId;
        this.deviceId = deviceId;
        this.serverAddress = serverAddress;
        if(actorSystemContext instanceof DeviceActorSystemContext){
            DeviceActorSystemContext deviceActorSystemContext = (DeviceActorSystemContext) actorSystemContext;
            deviceFactory = deviceActorSystemContext.getDeviceFactory();
        }
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("tenantId = " + tenantId + " , deviceId = "+ deviceId);
    }

    @Override
    protected boolean process(IoTActorMessage msg) {
        switch (msg.getMsgType()) {
            case PROTOCOL_ONLINE_MSG:
                onProtocolOnlineMsg((FromDeviceOnlineMsg) msg);
                break;
            case TO_DEVICE_SESSION_EVENT_MSG:
                // 设备关键事件通知
                onToDeviceSessionEventMsg((ToDeviceSessionEventMsg) msg);
                break;
            default:
                return false;
        }

        return true;
    }

    private void onToDeviceSessionEventMsg(ToDeviceSessionEventMsg msg) {
        if(msg.getSessionEventCode() == 1){
            // 更新设备的在线状态以及离线时间
            AggrDeviceEntity deviceEntity = deviceFactory.getDeviceById(deviceId);
            deviceEntity.updateDeviceStatusAndOnlineTime(DeviceStatusEnums.OFFLINE, msg.getLastActivityTimestamp());

            // 关闭当前设备对应的Actor
            getContext().stop(getSelf());
        }
    }

    // 设备上线处理
    private void onProtocolOnlineMsg(FromDeviceOnlineMsg msg) {
        // 修改设备的在线状态 & 修改设备的在线时间
        if(deviceFactory != null){
            AggrDeviceEntity deviceEntity = deviceFactory.getDeviceById(deviceId);
            deviceEntity.updateDeviceStatusAndOnlineTime(DeviceStatusEnums.ONLINE, msg.getOnlineTime());
        }

        // 记录设备的连接记录

    }

    @Override
    public void postStop() throws Exception {
        System.err.println("device : " + deviceId + " , 已经关闭");
    }

    public static class ActorCreator extends ContextBaseCreator<DeviceActor>{
        private final TenantId tenantId;
        private final DeviceId deviceId;

        private final ServerAddress serverAddress;

        public ActorCreator(ActorSystemContext actorSystemContext,
                            TenantId tenantId,DeviceId deviceId,ServerAddress serverAddress) {
            super(actorSystemContext);
            this.tenantId = tenantId;
            this.deviceId = deviceId;
            this.serverAddress = serverAddress;
        }



        @Override
        public DeviceActor create() throws Exception {
            return new DeviceActor(actorSystemContext,tenantId,deviceId, serverAddress);
        }
    }

}
