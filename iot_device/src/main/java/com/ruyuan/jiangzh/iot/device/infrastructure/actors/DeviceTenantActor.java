package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.util.List;
import java.util.Map;

public class DeviceTenantActor extends ContextAwareActor {

    private final TenantId tenantId;


    public DeviceTenantActor(ActorSystemContext actorSystemContext, TenantId tenantId){
        super(actorSystemContext);
        this.tenantId = tenantId;
    }

    @Override
    public void onReceive(Object o) throws Exception {

    }

    public final class ActorCreator extends ContextBaseCreator<DeviceTenantActor>{

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
