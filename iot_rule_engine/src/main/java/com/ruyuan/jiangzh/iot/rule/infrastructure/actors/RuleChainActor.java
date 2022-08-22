package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process.RuleChainMsgProcessor;

public class RuleChainActor extends ComponentActor<RuleChainId, RuleChainMsgProcessor>{

    public RuleChainActor(ActorSystemContext actorSystemContext, TenantId tenantId, RuleChainId ruleChainId) {
        super(actorSystemContext, tenantId, ruleChainId);
        setProcessor(new RuleChainMsgProcessor(actorSystemContext, tenantId, ruleChainId, context().parent(), context().self()));
    }

    @Override
    public void preStart() throws Exception {
        getProcessor().start(getContext());
    }

    @Override
    public void aroundPostStop() {
        try {
            getProcessor().stop(getContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onReceive(Object o) throws Exception {

    }

    public static class ActorCreator extends ContextBaseCreator<RuleChainActor> {

        private final TenantId tenantId;
        private final RuleChainId ruleChainId;

        public ActorCreator(ActorSystemContext actorSystemContext, TenantId tenantId, RuleChainId ruleChainId){
            super(actorSystemContext);
            this.tenantId = tenantId;
            this.ruleChainId = ruleChainId;
        }

        @Override
        public RuleChainActor create() throws Exception {
            return new RuleChainActor(actorSystemContext, tenantId, ruleChainId);
        }
    }
}
