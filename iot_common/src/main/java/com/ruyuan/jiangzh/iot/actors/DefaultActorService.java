package com.ruyuan.jiangzh.iot.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.ruyuan.jiangzh.iot.actors.msg.BaseMessage;

public class DefaultActorService implements ActorService{

    private static final String ACTOR_SYSTEM_NAME = "akka";

    private ActorSystem actorSystem;

    private ActorRef appActor;

    public void initActorSystem(ActorSystemContext systemContext){
        systemContext.setActorService(this);
        // 初始化ActorSystem
        actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME, systemContext.getConfig());

        systemContext.setActorSystem(actorSystem);
    }

    public void initActor(ActorSystemContext systemContext){
        // 初始化ActorRef
        appActor = systemContext.getAppActor();
    }

    @Override
    public void onMsg(BaseMessage msg) {
        appActor.tell(msg, ActorRef.noSender());
    }
}
