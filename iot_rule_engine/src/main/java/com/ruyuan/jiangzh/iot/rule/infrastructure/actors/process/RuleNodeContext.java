package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process;

import akka.actor.ActorRef;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;

public class RuleNodeContext {

    private final TenantId tenantId;
    private final ActorRef chainActor;
    private final ActorRef selfActor;

    private RuleNodeEntity self;

    public RuleNodeContext(TenantId tenantId,ActorRef chainActor,ActorRef selfActor,RuleNodeEntity self){
        this.tenantId = tenantId;
        this.chainActor = chainActor;
        this.selfActor = selfActor;
        this.self = self;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public ActorRef getChainActor() {
        return chainActor;
    }

    public ActorRef getSelfActor() {
        return selfActor;
    }

    public RuleNodeEntity getSelf() {
        return self;
    }
}
