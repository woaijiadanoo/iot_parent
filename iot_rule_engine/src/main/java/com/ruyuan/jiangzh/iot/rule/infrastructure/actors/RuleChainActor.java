package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ComponentEventMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ServiceToRuleEngineMsg;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages.RuleNodeToRuleChainTellNextMsg;
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
    protected boolean process(IoTActorMessage msg) {
        switch (msg.getMsgType()) {
            case SERVICE_TO_RULE_ENGINE_MSG:
                onServiceToRuleEngineMsg((ServiceToRuleEngineMsg)msg);
                break;
            case RULE_NODE_TO_RULE_CHAIN_TELL_NEXT_MSG:
                onRuleNodeToRuleChainTellNextMsg((RuleNodeToRuleChainTellNextMsg)msg);
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
        try {
            switch (msg.getEvent()) {
                case CREATED:
                    getProcessor().onCreated(context());
                    break;
                case UPDATED:
                    getProcessor().onUpdate(context());
                    break;
                case DELETED:
                    getProcessor().onStop(context());
                    break;
                default:
                    unhandled(msg);
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void onRuleNodeToRuleChainTellNextMsg(RuleNodeToRuleChainTellNextMsg msg){
        getProcessor().onRuleNodeToRuleChainTellNextMsg(msg);
    }

    private void onServiceToRuleEngineMsg(ServiceToRuleEngineMsg msg){
        // 统计获取的数据数量
        getProcessor().onServiceToRuleEngineMsg(msg);
        // 分类型做日志记录
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
