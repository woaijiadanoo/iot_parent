package com.ruyuan.jiangzh.iot.actors;

import akka.japi.Creator;

public abstract class ContextBaseCreator<T> implements Creator<T> {

    protected final ActorSystemContext actorSystemContext;

    public ContextBaseCreator(ActorSystemContext actorSystemContext){
        super();
        this.actorSystemContext = actorSystemContext;
    }

}
