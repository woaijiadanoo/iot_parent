package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.app.AppActor;
import com.ruyuan.jiangzh.iot.actors.msg.device.ToDeviceActorMsg;
import com.ruyuan.jiangzh.iot.actors.msg.test.DeviceMsg;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.device.infrastructure.configs.ActorConfigs;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceAppActor extends AppActor {

    private TenantServiceAPI tenantService;
    private Map<TenantId, ActorRef> tenantActos = null;

    public DeviceAppActor(ActorSystemContext actorSystemContext, TenantServiceAPI tenantService){
        super(actorSystemContext);
        this.tenantService = tenantService;
        tenantActos = new HashMap<>();
    }

    @Override
    public void doStart() {
        List<TenantId> tenantIds = tenantService.describeAllTenans();
        // 初始化TenantId的Actor
        tenantIds.stream().forEach(
                tenantId -> {
                    getOrCreateTenants(tenantId);
                }
        );

//        System.out.println("发送Device消息 --- start");
//
//        ToDeviceActorMsg toDeviceActorMsg = DeviceMsg.getToDeviceActorMsg();
//        ActorRef tenantActor = getOrCreateTenants(toDeviceActorMsg.getTenantId());
//        tenantActor.tell(toDeviceActorMsg, getSelf());
//
//        System.out.println("发送Device消息 --- end");

    }

    public ActorRef getOrCreateTenants(TenantId tenantId){
        ActorRef tenantActor = tenantActos.get(tenantId);
        if(tenantActor != null){
            return tenantActor;
        }

        tenantActor = getContext().actorOf(
                Props.create(
                        new DeviceTenantActor.ActorCreator(actorSystemContext, tenantId)).withDispatcher(ActorConfigs.TENANT_DISPATCHER_NAME),
                        tenantId.toString()
        );

        tenantActos.put(tenantId, tenantActor);

        return tenantActor;
    }

    @Override
    public void doReceive(Object msg) {
        System.out.println("msg = " + msg);
        if(msg instanceof ToDeviceActorMsg){
            ToDeviceActorMsg toDeviceActorMsg = (ToDeviceActorMsg)msg;
            System.out.println("msg.tenantId = " + toDeviceActorMsg.getTenantId());
            System.out.println("msg.deviceId = " + toDeviceActorMsg.getDeviceId());
            // 将ToDeviceActorMsg 交给tenantActor进行处理
            ActorRef tenantActor = getOrCreateTenants(toDeviceActorMsg.getTenantId());
            tenantActor.tell(msg, getSelf());
        }else{
            unhandled(msg);
        }
    }

    public static class ActorCreator extends ContextBaseCreator<DeviceAppActor>{

        private TenantServiceAPI tenantService;

        public ActorCreator(ActorSystemContext actorSystemContext,TenantServiceAPI tenantService){
            super(actorSystemContext);
            this.tenantService = tenantService;
        }

        @Override
        public DeviceAppActor create() throws Exception {
            return new DeviceAppActor(actorSystemContext,tenantService);
        }
    }

}
