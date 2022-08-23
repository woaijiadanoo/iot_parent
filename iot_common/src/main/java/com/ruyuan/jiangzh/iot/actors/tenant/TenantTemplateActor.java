package com.ruyuan.jiangzh.iot.actors.tenant;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

public class TenantTemplateActor extends ContextAwareActor {

    private final TenantId tenantId;

    public TenantTemplateActor(ActorSystemContext actorSystemContext, TenantId tenantId){
        super(actorSystemContext);
        this.tenantId = tenantId;
    }

    @Override
    protected boolean process(IoTActorMessage msg) {
        return false;
    }

    public final class ActorCreator extends ContextBaseCreator<TenantTemplateActor>{

        private final TenantId tenantId;

        public ActorCreator(ActorSystemContext actorSystemContext, TenantId tenantId){
            super(actorSystemContext);
            this.tenantId = tenantId;
        }

        @Override
        public TenantTemplateActor create() throws Exception {
            return new TenantTemplateActor(actorSystemContext, tenantId);
        }
    }

}
