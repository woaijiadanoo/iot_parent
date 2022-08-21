package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.app.AppActor;
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

    @Override
    public void doReceive(Object msg) {

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
