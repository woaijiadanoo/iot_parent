package com.ruyuan.jiangzh.iot.rule.infrastructure.configs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ruyuan.jiangzh.iot.actors.ActorService;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.DefaultActorService;
import com.ruyuan.jiangzh.iot.actors.RpcManager;
import com.ruyuan.jiangzh.iot.rule.domain.domainservice.RuleChainDomainService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.RuleEngineActorSystemContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.RuleEngineAppActor;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.rpc.RuleEngineRpcManager;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsInvokeService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.impls.JsExecutorService;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ActorConfigs {

    @Resource
    private TenantServiceAPI tenantServiceAPI;

    @Resource
    private RuleChainDomainService ruleChainDomainService;

    @Autowired
    private JsExecutorService jsExecutorService;

    @Autowired
    private JsInvokeService jsInvokeService;

    private static final String APP_DISPATCHER_NAME = "app-dispatcher";
    public static final String TENANT_DISPATCHER_NAME = "app-dispatcher";

    public static final String APP_ACTOR_NAME = "ruleEngineActor";

    @Bean(name = "actorService")
    public ActorService actorService(){
        DefaultActorService actorService = new DefaultActorService();
        RuleEngineActorSystemContext actorSystemContext  =new RuleEngineActorSystemContext(ruleChainDomainService,tenantServiceAPI);
        // 初始化ActorSystem和ActorSystemContext
        actorService.initActorSystem(actorSystemContext);

        ActorSystem actorSystem = actorSystemContext.getActorSystem();
        // 获取AppActor
        ActorRef deviceAppActor = actorSystem.actorOf(
                Props.create(
                        new RuleEngineAppActor.ActorCreator(actorSystemContext)).withDispatcher(APP_DISPATCHER_NAME),
                        APP_ACTOR_NAME
        );
        actorSystemContext.setAppActor(deviceAppActor);

        // 完成根结构的Actor初始化
        actorService.initActor(actorSystemContext);

        // 初始化RpcManager
        RpcManager rpcManager = new RuleEngineRpcManager();
        actorService.setRpcManager(rpcManager);

        // 注入与JS Engine相关的内容
        actorSystemContext.setJsExecutorService(jsExecutorService);
        actorSystemContext.setJsInvokeService(jsInvokeService);

        return actorService;
    }



}
