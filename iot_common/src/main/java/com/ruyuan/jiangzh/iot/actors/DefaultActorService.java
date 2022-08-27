package com.ruyuan.jiangzh.iot.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.ruyuan.jiangzh.iot.actors.msg.BaseMessage;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;

public class DefaultActorService implements ActorService{

    private static final String ACTOR_SYSTEM_NAME = "akka";

    private ActorSystem actorSystem;

    private RpcManager rpcManager;

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

    @Override
    public void broadcast(ToAllNodesMsg msg) {
        // 通知其他所有的同类型节点进行处理
        rpcManager.broadcast(msg);
    }

    @Override
    public void onBroadcast(ToAllNodesMsg msg) {
        System.err.println("onBroadcast msg ="+msg);
        // 通知当前JVM进行处理
        appActor.tell(msg, ActorRef.noSender());
    }

    @Override
    public void setRpcManager(RpcManager rpcManager) {
        this.rpcManager = rpcManager;
    }

}
