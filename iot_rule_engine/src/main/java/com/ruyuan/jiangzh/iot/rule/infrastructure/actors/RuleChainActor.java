package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

public class RuleChainActor extends ComponentActor{

    public RuleChainActor(ActorSystemContext actorSystemContext, TenantId tenantId, EntityId entityId) {
        super(actorSystemContext, tenantId, entityId);
    }

    @Override
    public void onReceive(Object o) throws Exception {

    }

    public static class ActorCreator extends ContextBaseCreator<RuleChainActor> {

        private final TenantId tenantId;
        private final EntityId entityId;

        public ActorCreator(ActorSystemContext actorSystemContext, TenantId tenantId, EntityId entityId){
            super(actorSystemContext);
            this.tenantId = tenantId;
            this.entityId = entityId;
        }

        @Override
        public RuleChainActor create() throws Exception {
            return new RuleChainActor(actorSystemContext, tenantId, entityId);
        }
    }
}
