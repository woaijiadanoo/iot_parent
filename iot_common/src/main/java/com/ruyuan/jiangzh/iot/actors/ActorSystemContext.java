package com.ruyuan.jiangzh.iot.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ActorSystemContext {

    private static final String AKKA_CONFIG_FILE_NAME = "actor-system.conf";

    private ActorService actorService;

    private ActorSystem actorSystem;

    private final Config config;

    // 相关的ActorRef
    private ActorRef appActor;

    public ActorSystemContext(){
        config = ConfigFactory
                .parseResources(AKKA_CONFIG_FILE_NAME).withFallback(ConfigFactory.load());
    }

    public Scheduler getScheduler(){
        return actorSystem.scheduler();
    }

    public ActorService getActorService() {
        return actorService;
    }

    public void setActorService(ActorService actorService) {
        this.actorService = actorService;
    }

    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    public void setActorSystem(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public Config getConfig() {
        return config;
    }

    public ActorRef getAppActor() {
        return appActor;
    }

    public void setAppActor(ActorRef appActor) {
        this.appActor = appActor;
    }

}
