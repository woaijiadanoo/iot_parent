package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsgMetaData;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.RuleEngineActorSystemContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages.RuleNodeToRuleChainTellNextMsg;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process.RuleNodeContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineRelationTypes;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.ListeningExecutor;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.RuleScriptEngine;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.impls.RuleNodeJsScriptEngine;

import java.util.Collections;
import java.util.Set;

public class DefaultRuleEngineContext implements RuleEngineContext{

    private final RuleNodeContext ruleNodeCtx;
    private final ActorSystemContext systemContext;

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
    public void tellNext(IoTMsg msg, String relationType) {
        tellNext(msg, Collections.singleton(relationType), null);
    }

    private void tellNext(IoTMsg msg, Set<String> relationTypes, Throwable th){
        ruleNodeCtx.getChainActor().tell(
                new RuleNodeToRuleChainTellNextMsg(
                        ruleNodeCtx.getSelf().getId(), relationTypes, msg),
                ruleNodeCtx.getSelfActor()
        );
    }

    @Override
    public void tellSelf(IoTMsg msg, long delayMs) {

    }

    @Override
    public void tellFailure(IoTMsg msg, Throwable throwable) {
        ruleNodeCtx.getChainActor().tell(
                new RuleNodeToRuleChainTellNextMsg(
                        ruleNodeCtx.getSelf().getId(),
                        Collections.singleton(RuleEngineRelationTypes.FAILURE),
                        msg),
                ruleNodeCtx.getSelfActor()
        );
    }

    @Override
    public IoTMsg transformMsg(IoTMsg origMsg, String type, EntityId originator, IoTMsgMetaData metaData, String data) {
        return new IoTMsg(origMsg.getId(), type, originator, data, metaData.copy(), origMsg.getRuleChainId(), origMsg.getRuleNodeId());
    }

    @Override
    public void updateSelf(RuleNodeEntity node) {
        ruleNodeCtx.setSelf(node);
    }

    @Override
    public ListeningExecutor getJsExecutor() {
        if(systemContext instanceof RuleEngineActorSystemContext){
            RuleEngineActorSystemContext actorSystemContext = (RuleEngineActorSystemContext) systemContext;
            return actorSystemContext.getJsExecutorService();
        }else{
            return null;
        }
    }

    @Override
    public RuleScriptEngine createJsScriptEngine(String script, String... argNames) {
        if(systemContext instanceof RuleEngineActorSystemContext){
            RuleEngineActorSystemContext actorSystemContext = (RuleEngineActorSystemContext) systemContext;
            return new RuleNodeJsScriptEngine(actorSystemContext.getJsInvokeService(), ruleNodeCtx.getSelf().getId(), script, argNames);
        }else{
            return null;
        }
    }
}
