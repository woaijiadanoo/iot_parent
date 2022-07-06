package com.ruyuan.jiangzh.iot.actors;

import akka.actor.ActorSystem;

import javax.annotation.PostConstruct;

public class DefaultActorService implements ActorService{

    private static final String ACTOR_SYSTEM_NAME = "akka";

    private ActorSystem actorSystem;

    @PostConstruct
    public void initActorSystem(ActorSystemContext systemContext){
        systemContext.setActorService(this);

        // 初始化ActorSystem
        actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME, systemContext.getConfig());

        systemContext.setActorSystem(actorSystem);
    }

}
