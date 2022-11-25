package com.ruyuan.jiangzh.iot.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.ruyuan.jiangzh.iot.actors.msg.BaseMessage;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.InvokeDeviceAttributeMsg;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Future;


public class DefaultActorService implements ActorService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        logger.info("BaseMessage onMsg : [{}]", msg);
        appActor.tell(msg, ActorRef.noSender());
    }

    @Override
    public void onMsg(ToDeviceMsg msg) {
        logger.info("ToDeviceMsg onMsg : [{}]", msg);
        if(msg.getServerAddress() == null){
            Future<Object> future = Patterns.ask(appActor, msg, 1000L);
            future.onSuccess(new OnSuccess<Object>() {
                @Override
                public void onSuccess(Object result) throws Throwable {
                    if(result == null){
                        logger.error("ToDeviceMsg onMsg device offline");
                    }
                    // 当询问到Address和sessionId时，进行的操作
                    if(result instanceof InvokeDeviceAttributeMsg){
                        logger.info("ToDeviceMsg onMsg onSuccess : [{}]", result);
                        rpcManager.onMsg((InvokeDeviceAttributeMsg) result);
                    }
                }
            }, actorSystem.dispatcher());
        } else {
            rpcManager.onMsg(msg);
        }
    }

    @Override
    public void broadcast(ToAllNodesMsg msg) {
        // 通知其他所有的同类型节点进行处理
        rpcManager.broadcast(msg);
    }

    @Override
    public void onBroadcast(ToAllNodesMsg msg) {
        // 通知当前JVM进行处理
        appActor.tell(msg, ActorRef.noSender());
    }

    @Override
    public void setRpcManager(RpcManager rpcManager) {
        this.rpcManager = rpcManager;
    }

}
