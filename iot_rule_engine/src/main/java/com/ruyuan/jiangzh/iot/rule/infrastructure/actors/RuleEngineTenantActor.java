package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import akka.actor.ActorRef;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ComponentEventMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ServiceToRuleEngineMsg;
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
        switch (msg.getMsgType()){
            case SERVICE_TO_RULE_ENGINE_MSG:
                onServiceToRuleEngineMsg((ServiceToRuleEngineMsg)msg);
                break;
            case COMPONENT_EVENT_MSG:
                // 新增，修改或删除等事件变更的通知
                onComponentEventMsg((ComponentEventMsg)msg);
                break;
            default:
                return false;
        }
        return true;
    }

    private void onComponentEventMsg(ComponentEventMsg msg) {
        ActorRef ruleChainActor = getRuleChainActorRef(msg.getRuleChainId());
        if(ruleChainActor != null){
            ruleChainActor.tell(msg, ActorRef.noSender());
        }
    }

    /*
        ry_user_01
            rulechain -> 01
            rulechain -> 02
     */
    private void onServiceToRuleEngineMsg(ServiceToRuleEngineMsg msg) {
        broadcast(msg);
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
