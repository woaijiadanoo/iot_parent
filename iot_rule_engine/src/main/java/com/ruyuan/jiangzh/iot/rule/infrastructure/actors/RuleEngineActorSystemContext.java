package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.rule.domain.domainservice.RuleChainDomainService;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;

public class RuleEngineActorSystemContext extends ActorSystemContext {

    private TenantServiceAPI tenantService;

    private RuleChainDomainService ruleChainService;

    public RuleEngineActorSystemContext(RuleChainDomainService ruleChainService, TenantServiceAPI tenantService){
        super();
        this.ruleChainService = ruleChainService;
        this.tenantService = tenantService;
    }

    public RuleChainDomainService getRuleChainService() {
        return ruleChainService;
    }
}
