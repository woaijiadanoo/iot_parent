package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.ServerAddress;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceActorMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.FromDeviceOnlineMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ToDeviceSessionEventMsg;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceTenantActor extends ContextAwareActor {

    private final TenantId tenantId;

    private static final String DEVICE_DISPATCHER_NAME="device-dispatcher";

    private final Map<DeviceId, ActorRef> deviceActors;

    public DeviceTenantActor(ActorSystemContext actorSystemContext, TenantId tenantId){
        super(actorSystemContext);
        this.tenantId = tenantId;
        this.deviceActors = new HashMap<>();
    }

    /*
        获取或创建DeviceActor
     */
    private ActorRef getOrCreateDeviceActor(DeviceId deviceId, ServerAddress serverAddress){
        ActorRef deviceActor = deviceActors.get(deviceId);
        if(deviceActor == null){
            deviceActor = getContext().actorOf(
                    Props.create(
                            new DeviceActor
                                    .ActorCreator(actorSystemContext,tenantId,deviceId, serverAddress))
                            .withDispatcher(DEVICE_DISPATCHER_NAME),
                            deviceId.toString()
            );
            deviceActors.put(deviceId, deviceActor);
        }

        return deviceActor;
    }


    @Override
    public void preStart() throws Exception {
        System.out.println("DeviceTenantActor preStart tenantId = " + tenantId);
    }

    @Override
    protected boolean process(IoTActorMessage msg) {
        switch (msg.getMsgType()) {
            case PROTOCOL_ONLINE_MSG:
                // 设备上线通知消息
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

    // 设备关键事件通知
    private void onToDeviceSessionEventMsg(ToDeviceSessionEventMsg msg) {
        System.err.println("DeviceTenantActor msg : "+msg);
        // 设备已经离线
        if(msg.getSessionEventCode() == 1){
            ActorRef deviceActorRef = deviceActors.get(msg.getDeviceId());
            if(deviceActorRef != null){
                deviceActorRef.tell(msg, getSelf());
            }else{
                System.err.println("onToDeviceSessionEventMsg no device actor" + msg.getDeviceId());
            }
        }
    }

    // 设备上线通知消息
    private void onProtocolOnlineMsg(FromDeviceOnlineMsg msg) {
        ActorRef deviceActor = getOrCreateDeviceActor(msg.getDeviceId(), msg.getServerAddress());
        if(deviceActor != null){
            deviceActor.tell(msg, getSelf());
        }
    }

    public static class ActorCreator extends ContextBaseCreator<DeviceTenantActor>{

        private final TenantId tenantId;

        public ActorCreator(ActorSystemContext actorSystemContext, TenantId tenantId){
            super(actorSystemContext);
            this.tenantId = tenantId;
        }

        @Override
        public DeviceTenantActor create() throws Exception {
            return new DeviceTenantActor(actorSystemContext, tenantId);
        }
    }

}
