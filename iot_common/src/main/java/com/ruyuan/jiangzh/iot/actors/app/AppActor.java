package com.ruyuan.jiangzh.iot.actors.app;

import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextAwareActor;

public abstract class AppActor extends ContextAwareActor {

    public AppActor(ActorSystemContext actorSystemContext){
        super(actorSystemContext);
    }

    @Override
    public void preStart() throws Exception {
        doStart();
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        doReceive(msg);
    }

    public abstract void doStart();
    public abstract void doReceive(Object msg);

}
