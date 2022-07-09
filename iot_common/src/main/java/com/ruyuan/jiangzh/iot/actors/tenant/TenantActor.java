package com.ruyuan.jiangzh.iot.actors.tenant;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

public class TenantActor extends ContextAwareActor {

    private final TenantId tenantId;

    public TenantActor(ActorSystemContext actorSystemContext, TenantId tenantId){
        super(actorSystemContext);
        this.tenantId = tenantId;
    }

    @Override
    public void onReceive(Object o) throws Exception {

    }

    public final class ActorCreator extends ContextBaseCreator<TenantActor>{

        private final TenantId tenantId;

        public ActorCreator(ActorSystemContext actorSystemContext, TenantId tenantId){
            super(actorSystemContext);
            this.tenantId = tenantId;
        }

        @Override
        public TenantActor create() throws Exception {
            return new TenantActor(actorSystemContext, tenantId);
        }
    }

}
