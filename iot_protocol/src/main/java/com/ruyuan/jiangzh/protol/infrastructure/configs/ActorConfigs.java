package com.ruyuan.jiangzh.protol.infrastructure.configs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.ruyuan.jiangzh.iot.actors.ActorService;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.DefaultActorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ActorConfigs {


    private static final String APP_DISPATCHER_NAME = "app-dispatcher";
    public static final String TENANT_DISPATCHER_NAME = "app-dispatcher";

    public static final String APP_ACTOR_NAME = "ruleEngineActor";

    @Bean(name = "actorService")
    public ActorService actorService(){
        DefaultActorService actorService = new DefaultActorService();
        ActorSystemContext actorSystemContext  =new ActorSystemContext();
        // 初始化ActorSystem和ActorSystemContext
        actorService.initActorSystem(actorSystemContext);

        ActorSystem actorSystem = actorSystemContext.getActorSystem();
        // 获取AppActor
        ActorRef deviceAppActor = null;

        actorSystemContext.setAppActor(deviceAppActor);

        // 完成根结构的Actor初始化
        actorService.initActor(actorSystemContext);

        return actorService;
    }



}
