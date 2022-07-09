package com.ruyuan.jiangzh.iot.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import javax.annotation.PostConstruct;

public class DefaultActorService implements ActorService{

    private static final String ACTOR_SYSTEM_NAME = "akka";

    private ActorSystem actorSystem;

    private ActorRef appActor;

    private ActorRef tenantActor;

    @PostConstruct
    public void initActorSystem(ActorSystemContext systemContext){
        systemContext.setActorService(this);

        // 初始化ActorRef
        appActor = systemContext.getAppActor();
        tenantActor = systemContext.getTenantActor();

        // 初始化ActorSystem
        actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME, systemContext.getConfig());

        systemContext.setActorSystem(actorSystem);
    }

}
