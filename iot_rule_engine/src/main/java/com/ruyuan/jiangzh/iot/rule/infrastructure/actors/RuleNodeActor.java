package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ServiceToRuleEngineMsg;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages.RuleChainToRuleNodeMsg;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process.RuleNodeMsgProcessor;

public class RuleNodeActor extends ComponentActor<RuleNodeId, RuleNodeMsgProcessor>{

    private final RuleChainId ruleChainId;

    public RuleNodeActor(ActorSystemContext actorSystemContext, TenantId tenantId, RuleChainId ruleChainId, RuleNodeId ruleNodeId) {
        super(actorSystemContext, tenantId, ruleNodeId);
        this.ruleChainId = ruleChainId;
        setProcessor(new RuleNodeMsgProcessor(actorSystemContext,tenantId,ruleNodeId, context().parent(), context().self()));
    }

    @Override
    public void preStart() throws Exception {
        getProcessor().start(getContext());
    }

    @Override
    public void postStop() throws Exception {
        try {
            getProcessor().stop(getContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean process(IoTActorMessage msg) {
        switch (msg.getMsgType()) {
            case RULE_CHAIN_TO_RULE_NODE_MSG:
                onRuleChainToRuleNodeMsg((RuleChainToRuleNodeMsg)msg);
                break;
            default:
                return false;
        }
        return true;
    }

    private void onRuleChainToRuleNodeMsg(RuleChainToRuleNodeMsg msg){
        getProcessor().onRuleChainToRuleNodeMsg(msg);
    }

    public static class ActorCreator extends ContextBaseCreator<RuleNodeActor> {

        private final TenantId tenantId;
        private final RuleChainId ruleChainId;
        private final RuleNodeId ruleNodeId;

        public ActorCreator(ActorSystemContext actorSystemContext,TenantId tenantId,RuleChainId ruleChainId,RuleNodeId ruleNodeId){
            super(actorSystemContext);
            this.tenantId = tenantId;
            this.ruleChainId = ruleChainId;
            this.ruleNodeId = ruleNodeId;
        }

        @Override
        public RuleNodeActor create() throws Exception {
            return new RuleNodeActor(actorSystemContext, tenantId, ruleChainId, ruleNodeId);
        }
    }
}
