package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process.RuleNodeContext;

public class DefaultRuleEngineContext implements RuleEngineContext{

    private final RuleNodeContext ruleNodeCtx;
    private final ActorSystemContext systemContext;

    private RuleNodeEntity self;

    public DefaultRuleEngineContext(ActorSystemContext systemContext, RuleNodeContext ruleNodeContext) {
        this.ruleNodeCtx = ruleNodeContext;
        this.systemContext = systemContext;
    }

    @Override
    public RuleNodeId getSelfId() {
        return ruleNodeCtx.getSelf().getId();
    }

    @Override
    public String getNodeId() {
        return ruleNodeCtx.getTenantId().toString();
    }

    @Override
    public void tellNext(RuleEngineMsg msg, String relationType) {

    }

    @Override
    public void tellSelf(RuleEngineMsg msg, long delayMs) {

    }

    @Override
    public void tellFailure(RuleEngineMsg msg, Throwable throwable) {

    }

    @Override
    public RuleEngineMsg transformMsg(RuleEngineMsg origMsg, String type, EntityId originator, RuleEngineMsgMetaData metaData, String data) {
        return null;
    }

    @Override
    public void updateSelf(RuleNodeEntity node) {
        ruleNodeCtx.setSelf(node);
    }
}
