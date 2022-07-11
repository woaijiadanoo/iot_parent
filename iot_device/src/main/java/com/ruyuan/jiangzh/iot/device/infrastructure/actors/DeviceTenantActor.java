package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceActorMsg;
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
    private ActorRef getOrCreateDeviceActor(DeviceId deviceId){
        ActorRef deviceActor = deviceActors.get(deviceId);
        if(deviceActor == null){
            deviceActor = getContext().actorOf(
                    Props.create(
                            new DeviceActor.ActorCreator(actorSystemContext,tenantId,deviceId)).withDispatcher(DEVICE_DISPATCHER_NAME),
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
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof ToDeviceActorMsg){
            onToDeviceActorMsg((ToDeviceActorMsg)msg);
        }
    }

    private void onToDeviceActorMsg(ToDeviceActorMsg msg) {
        ActorRef deviceActor = getOrCreateDeviceActor(msg.getDeviceId());
        // 剩下的业务处理就是后话了

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
