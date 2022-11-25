package com.ruyuan.jiangzh.iot.device.infrastructure.configs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ruyuan.jiangzh.iot.actors.ActorService;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.DefaultActorService;
import com.ruyuan.jiangzh.iot.actors.RpcManager;
import com.ruyuan.jiangzh.iot.device.domain.aggregates.aggregatedevice.infrastructure.factory.AggrDeviceFactory;
import com.ruyuan.jiangzh.iot.device.infrastructure.actors.DeviceActorSystemContext;
import com.ruyuan.jiangzh.iot.device.infrastructure.actors.DeviceAppActor;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ActorConfigs {

    @Resource
    private TenantServiceAPI tenantServiceAPI;

    @Autowired
    private AggrDeviceFactory deviceFactory;

    @Autowired
    private RpcManager rpcManager;

    private static final String APP_DISPATCHER_NAME = "app-dispatcher";
    public static final String TENANT_DISPATCHER_NAME = "app-dispatcher";

    @Bean(name = "actorService")
    public ActorService actorService(){
        DefaultActorService actorService = new DefaultActorService();
        ActorSystemContext actorSystemContext = new DeviceActorSystemContext(deviceFactory);
        // 初始化ActorSystem和ActorSystemContext
        actorService.initActorSystem(actorSystemContext);

        ActorSystem actorSystem = actorSystemContext.getActorSystem();
        // 获取AppActor
        ActorRef deviceAppActor = actorSystem.actorOf(
                Props.create(
                        new DeviceAppActor.ActorCreator(actorSystemContext, tenantServiceAPI)).withDispatcher(APP_DISPATCHER_NAME),
                        "deviceAppActor"
        );
        actorSystemContext.setAppActor(deviceAppActor);

        // 完成根结构的Actor初始化
        actorService.initActor(actorSystemContext);
        actorService.setRpcManager(rpcManager);

        return actorService;
    }



}
