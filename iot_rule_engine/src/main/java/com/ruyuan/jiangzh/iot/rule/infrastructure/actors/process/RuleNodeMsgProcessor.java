package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;
import com.ruyuan.jiangzh.iot.rule.domain.domainservice.RuleChainDomainService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.ComponentState;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.RuleEngineActorSystemContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNode;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNodeConfiguration;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.DefaultRuleEngineContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineContext;

public class RuleNodeMsgProcessor extends ComponentMsgProcessor<RuleNodeId> {

    private RuleChainDomainService ruleChainDomainService;

    private final ActorRef parent;

    private final ActorRef self;

    private RuleNodeEntity ruleNode;
    private RuleEngineNode ruleEngineNode;
    private RuleEngineContext ruleEngineCtx;

    public RuleNodeMsgProcessor(
            ActorSystemContext systemContext, TenantId tenantId, RuleNodeId ruleNodeId,
            ActorRef parent, ActorRef self) {
        super(systemContext, tenantId, ruleNodeId);
        this.parent = parent;
        this.self = self;

        if(systemContext instanceof RuleEngineActorSystemContext){
            RuleEngineActorSystemContext actorSystemContext = (RuleEngineActorSystemContext) systemContext;
            this.ruleChainDomainService = actorSystemContext.getRuleChainService();
            this.ruleNode = ruleChainDomainService.findRuleNodeById(tenantId, ruleNodeId);
            this.ruleEngineCtx = new DefaultRuleEngineContext(systemContext, new RuleNodeContext(tenantId, parent, self, ruleNode));
        }else{
            this.ruleChainDomainService = null;
        }
    }

    @Override
    public void start(ActorContext context) throws Exception {
        ruleEngineNode = initComponent(ruleNode);
        if (ruleEngineNode != null){
           state = ComponentState.ACTIVE;
        }
    }

    private RuleEngineNode initComponent(RuleNodeEntity ruleNode) {
        RuleEngineNode ruleEngine = null;
        if(ruleNode != null){
            Class<?> componentClazz = null;
            try {
                // com.ruyuan.jiangzh.iot.rule.infrastructure.engine.extension.kafka.ExtensionKafkaNode
                componentClazz = Class.forName(ruleNode.getNodeType());
                ruleEngine = (RuleEngineNode) componentClazz.newInstance();
                ruleEngine.init(ruleEngineCtx, new RuleEngineNodeConfiguration(ruleNode.getConfiguration()));
            } catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return ruleEngine;
    }

    @Override
    public void stop(ActorContext context) throws Exception {
        if(ruleEngineNode != null){
            ruleEngineNode.destory();
        }
        context.stop(self);
    }

    @Override
    public void onUpdate(ActorContext context) throws Exception {
        RuleNodeEntity newRuleNode = ruleChainDomainService.findRuleNodeById(tenantId, entityId);
        // 需要判断到底是不是需要更新
        boolean newRestart =  ! (ruleNode.getNodeType().equals(newRuleNode.getNodeType())
                && ruleNode.getConfiguration().equals(newRuleNode.getConfiguration()));

        this.ruleNode = newRuleNode;
        this.ruleEngineCtx.updateSelf(newRuleNode);
        if(newRestart){
            if(ruleEngineNode != null){
                ruleEngineNode.destory();
            }
            start(context);
        }
    }

    @Override
    public String componentName() throws Exception {
        return ruleNode.getNodeName();
    }
}
