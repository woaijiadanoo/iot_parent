package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.domain.domainservice.RuleChainDomainService;
import com.ruyuan.jiangzh.iot.rule.domain.entity.RuleChainEntity;

import java.util.List;
import java.util.Map;

public class RuleChainManager {

    private final static String RULE_CHAIN_DISPATCHER  = "rule-chain-dispatcher";
    private final ActorSystemContext systemContext;

    private final Map<RuleChainId, ActorRef> ruleChainActors;

    private final RuleChainDomainService ruleChainDomainService;

    public RuleChainManager(ActorSystemContext systemContext){
        this.systemContext = systemContext;
        ruleChainActors = Maps.newHashMap();
        if(systemContext instanceof RuleEngineActorSystemContext){
            RuleEngineActorSystemContext actorSystemContext = (RuleEngineActorSystemContext) systemContext;
            this.ruleChainDomainService = actorSystemContext.getRuleChainService();
        }else{
            this.ruleChainDomainService = null;
        }
    }

    public void init(ActorContext actorContext,TenantId tenantId){
        for(RuleChainEntity entity : findRuleChains(tenantId)){
            RuleChainId ruleChainId = entity.getId();
            getOrCreateActor(actorContext, ruleChainId);
        }
    }

    /*
        获取单个Tenant下的所有的RuleChains
     */
    private List<RuleChainEntity> findRuleChains(TenantId tenantId){
        return ruleChainDomainService.findRuleChains(tenantId);
    }

    public ActorRef getOrCreateActor(ActorContext context, RuleChainId ruleChainId) {

        return ruleChainActors.computeIfAbsent(ruleChainId, rid -> {
            return context.actorOf(Props.create(creator(rid)).withDispatcher(RULE_CHAIN_DISPATCHER), rid.toString());
        });
    }

    private TenantId getTenantId(){
        return null;
    }

    private Creator<RuleChainActor> creator(RuleChainId ruleChainId){
        return new RuleChainActor.ActorCreator(systemContext, getTenantId(), ruleChainId);
    }

    /*
        JVM内部所有的ruleChainActor广播
     */
    public void broadcast(Object msg) {
        ruleChainActors.values().forEach(actorRef -> {actorRef.tell(msg, ActorRef.noSender());});
    }


    public void remove(RuleChainId ruleChainId){
        ruleChainActors.remove(ruleChainId);
    }

}
