package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

public class DeviceActor extends ContextAwareActor {

    private TenantId tenantId;
    private DeviceId deviceId;

    public DeviceActor(ActorSystemContext actorSystemContext, TenantId tenantId,DeviceId deviceId) {
        super(actorSystemContext);
        this.tenantId = tenantId;
        this.deviceId = deviceId;
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("tenantId = " + tenantId + " , deviceId = "+ deviceId);
    }

    @Override
    protected boolean process(IoTActorMessage msg) {
        return false;
    }

    public static class ActorCreator extends ContextBaseCreator<DeviceActor>{
        private final TenantId tenantId;
        private final DeviceId deviceId;

        public ActorCreator(ActorSystemContext actorSystemContext,TenantId tenantId,DeviceId deviceId) {
            super(actorSystemContext);
            this.tenantId = tenantId;
            this.deviceId = deviceId;
        }



        @Override
        public DeviceActor create() throws Exception {
            return new DeviceActor(actorSystemContext,tenantId,deviceId);
        }
    }

}
