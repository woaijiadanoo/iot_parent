package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

public class RuleEngineTenantActor extends RuleChainManagerActor{
    private final TenantId tenantId;
    public RuleEngineTenantActor(ActorSystemContext actorSystemContext, TenantId tenantId) {
        super(actorSystemContext);
        this.tenantId = tenantId;
    }

    @Override
    public void preStart() throws Exception {
        initRuleChains(tenantId);
    }

    @Override
    protected boolean process(IoTActorMessage msg) {
        return false;
    }

    public static class ActorCreator extends ContextBaseCreator<RuleEngineTenantActor> {
        private final TenantId tenantId;

        public ActorCreator(ActorSystemContext actorSystemContext, TenantId tenantId){
            super(actorSystemContext);
            this.tenantId = tenantId;
        }

        @Override
        public RuleEngineTenantActor create() throws Exception {
            return new RuleEngineTenantActor(actorSystemContext,tenantId);
        }
    }

}
