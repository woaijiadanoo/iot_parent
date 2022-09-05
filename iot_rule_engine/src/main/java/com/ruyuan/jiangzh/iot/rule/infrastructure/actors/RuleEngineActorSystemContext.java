package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.rule.domain.domainservice.RuleChainDomainService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsInvokeService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.impls.JsExecutorService;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;

public class RuleEngineActorSystemContext extends ActorSystemContext {

    private TenantServiceAPI tenantService;

    private RuleChainDomainService ruleChainService;

    private JsExecutorService jsExecutorService;

    private JsInvokeService jsInvokeService;

    public RuleEngineActorSystemContext(RuleChainDomainService ruleChainService, TenantServiceAPI tenantService){
        super();
        this.ruleChainService = ruleChainService;
        this.tenantService = tenantService;
    }

    public RuleChainDomainService getRuleChainService() {
        return ruleChainService;
    }

    public TenantServiceAPI getTenantService() {
        return tenantService;
    }

    public JsExecutorService getJsExecutorService() {
        return jsExecutorService;
    }

    public void setJsExecutorService(JsExecutorService jsExecutorService) {
        this.jsExecutorService = jsExecutorService;
    }

    public JsInvokeService getJsInvokeService() {
        return jsInvokeService;
    }

    public void setJsInvokeService(JsInvokeService jsInvokeService) {
        this.jsInvokeService = jsInvokeService;
    }
}
