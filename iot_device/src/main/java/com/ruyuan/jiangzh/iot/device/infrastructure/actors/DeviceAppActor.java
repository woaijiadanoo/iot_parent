package com.ruyuan.jiangzh.iot.device.infrastructure.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.app.AppActor;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.device.InvokeDeviceAttributeMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.FromDeviceOnlineMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ToDeviceSessionEventMsg;
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
    protected boolean process(IoTActorMessage msg) {
        switch (msg.getMsgType()) {
            case PROTOCOL_ONLINE_MSG:
                // 设备上线通知消息
                onProtocolOnlineMsg((FromDeviceOnlineMsg) msg);
                break;
            case TO_DEVICE_SESSION_EVENT_MSG:
                // 设备关键事件通知
                onToDeviceSessionEventMsg((ToDeviceSessionEventMsg) msg);
                break;
            case INVOKE_DEVICE_ATTR_MSG:
                onInvokeDeviceAttrMsg((InvokeDeviceAttributeMsg) msg);
                break;
            default:
                return false;
        }
        return true;
    }

    private void onInvokeDeviceAttrMsg(InvokeDeviceAttributeMsg msg) {
        TenantId tenantId = msg.getTenantId();
        ActorRef tenantActorRef = getOrCreateTenants(tenantId);
        // 要注意， 这里要用getSender()
        tenantActorRef.tell(msg, getSender());
    }

    // 设备关键事件通知
    private void onToDeviceSessionEventMsg(ToDeviceSessionEventMsg msg) {
        ActorRef tenantActorRef = getOrCreateTenants(msg.getTenantId());
        if(tenantActorRef != null){
            tenantActorRef.tell(msg, getSelf());
        }
    }

    // 设备上线通知消息
    private void onProtocolOnlineMsg(FromDeviceOnlineMsg msg) {
        ActorRef tenantActor = getOrCreateTenants(msg.getTenantId());
        if(tenantActor != null){
            tenantActor.tell(msg, getSelf());
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
