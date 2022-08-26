package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.app.AppActor;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ComponentEventMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ServiceToRuleEngineMsg;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.infrastructure.configs.ActorConfigs;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;

import java.util.List;
import java.util.Map;

public class RuleEngineAppActor  extends AppActor {

    private final TenantServiceAPI tenantService;

    private Map<TenantId, ActorRef> tenantActos = null;

    public RuleEngineAppActor(ActorSystemContext actorSystemContext) {
        super(actorSystemContext);
        if(actorSystemContext instanceof RuleEngineActorSystemContext){
            RuleEngineActorSystemContext systemContext = (RuleEngineActorSystemContext) actorSystemContext;
            this.tenantService = systemContext.getTenantService();
        }else{
            this.tenantService = null;
        }
        tenantActos = Maps.newHashMap();
    }

    @Override
    protected boolean process(IoTActorMessage msg) {
        switch (msg.getMsgType()){
            case SERVICE_TO_RULE_ENGINE_MSG:
                // service调用ruleEngine
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
        ActorRef tenantActor = getOrCreateTenants(msg.getTenantId());
        if(tenantActor != null){
            tenantActor.tell(msg, ActorRef.noSender());
        }
    }


    private void onServiceToRuleEngineMsg(ServiceToRuleEngineMsg msg) {
        ActorRef tenantActor = getOrCreateTenants(msg.getTenantId());
        tenantActor.tell(msg, self());
    }

    @Override
    public void doStart() {
        List<TenantId> tenantIds = tenantService.describeAllTenans();
        // 初始化TenantId的Actor
        tenantIds.stream().forEach(
                tenantId -> {
                    getOrCreateTenants(tenantId);
                }
        );
    }

    private ActorRef getOrCreateTenants(TenantId tenantId) {
        ActorRef tenantActor = tenantActos.get(tenantId);
        if(tenantActor != null){
            return tenantActor;
        }

        tenantActor = getContext().actorOf(
                Props.create(
                        new RuleEngineTenantActor.ActorCreator(actorSystemContext, tenantId)).withDispatcher(ActorConfigs.TENANT_DISPATCHER_NAME),
                tenantId.toString()
        );

        tenantActos.put(tenantId, tenantActor);

        return tenantActor;
    }

    public static class ActorCreator extends ContextBaseCreator<RuleEngineAppActor> {
        public ActorCreator(ActorSystemContext actorSystemContext){
            super(actorSystemContext);
        }

        @Override
        public RuleEngineAppActor create() throws Exception {
            return new RuleEngineAppActor(actorSystemContext);
        }
    }

}
