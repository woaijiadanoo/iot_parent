package com.ruyuan.jiangzh.iot.actors;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

public abstract class ContextAwareActor extends UntypedActor {

    protected final ActorSystemContext actorSystemContext;

    public ContextAwareActor(ActorSystemContext actorSystemContext){
        super();
        this.actorSystemContext = actorSystemContext;
    }

    /*
        通用的监管策略
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return commonStrategy;
    }

    private SupervisorStrategy commonStrategy =
            new OneForOneStrategy(3, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
                @Override
                public SupervisorStrategy.Directive apply(Throwable t) throws Exception {
                    if (t instanceof RuntimeException){
                        return SupervisorStrategy.restart();
                    } else {
                       return SupervisorStrategy.stop();
                    }
                }
            });

}
