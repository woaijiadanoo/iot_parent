package com.ruyuan.jiangzh.iot.actors;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.japi.Function;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

public abstract class ContextAwareActor extends UntypedActor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final ActorSystemContext actorSystemContext;

    public ContextAwareActor(ActorSystemContext actorSystemContext){
        super();
        this.actorSystemContext = actorSystemContext;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if(msg instanceof IoTActorMessage){
            try {
                if(!process((IoTActorMessage)msg)){
                    logger.error("process service failture msg : [{}]", msg);
                }
            } catch (Exception e) {
                logger.error("process system failture msg : [{}], e: [{}]", msg, e.getMessage());
                throw e;
            }
        } else if (msg instanceof Terminated) {
            processTermination((Terminated)msg);
        } else {
            logger.error("msgType unknown msg : [{}]", msg);
            unhandled(msg);
        }
    }

    protected void processTermination(Terminated msg) {}

    protected abstract boolean process(IoTActorMessage msg);
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
