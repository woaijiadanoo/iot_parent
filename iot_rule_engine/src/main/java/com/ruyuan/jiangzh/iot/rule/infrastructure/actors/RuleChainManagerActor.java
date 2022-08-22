package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import akka.actor.ActorRef;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

public abstract class RuleChainManagerActor extends ContextAwareActor {

    private RuleChainManager ruleChainManager;

    public RuleChainManagerActor(ActorSystemContext actorSystemContext) {
        super(actorSystemContext);
        this.ruleChainManager = new RuleChainManager(actorSystemContext);
    }

    /*
        进行ruleChains列表的初始化
     */
    protected void initRuleChains(TenantId tenantId){
        ruleChainManager.init(this.context(),tenantId);
    }

    protected ActorRef getRuleChainActorRef(RuleChainId ruleChainId){
        ActorRef ruleChainActor = ruleChainManager.getOrCreateActor(this.context(), ruleChainId);
        return ruleChainActor;
    }

    protected void broadcast(Object msg){
        ruleChainManager.broadcast(msg);
    }

}
