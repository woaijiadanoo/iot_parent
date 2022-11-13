package com.ruyuan.jiangzh.iot.rule.infrastructure.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;
import com.ruyuan.jiangzh.iot.actors.ContextBaseCreator;
import com.ruyuan.jiangzh.iot.actors.app.AppActor;
import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsgMetaData;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ComponentEventMsg;
import com.ruyuan.jiangzh.iot.actors.msg.messages.ServiceToRuleEngineMsg;
import com.ruyuan.jiangzh.iot.actors.msg.rule.PostTelemetryMsg;
import com.ruyuan.jiangzh.iot.actors.msg.rule.TransportToRuleEngineActorMsg;
import com.ruyuan.jiangzh.iot.actors.msg.rule.TransportToRuleEngineActorMsgWrapper;
import com.ruyuan.jiangzh.iot.actors.msg.rule.vo.KeyValueProtoVO;
import com.ruyuan.jiangzh.iot.actors.msg.rule.vo.TsKvListProtoVO;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.device.DeviceId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages.DeviceToRuleEngineMsg;
import com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages.FromDeviceMsgType;
import com.ruyuan.jiangzh.iot.rule.infrastructure.configs.ActorConfigs;
import com.ruyuan.jiangzh.service.sdk.TenantServiceAPI;

import java.util.List;
import java.util.Map;

import static com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages.FromDeviceMsgType.POST_TELEMETRY_MSG;

public class RuleEngineAppActor  extends AppActor {

    private final TenantServiceAPI tenantService;


    private static final String RECIVE_TIME = "reciveTime";
    private static final String TENANT_ID = "tenantId";
    private static final String DEVICE_ID = "deviceId";

    private final Gson gson = new Gson();


    private Map<TenantId, ActorRef> tenantActos = null;

    public RuleEngineAppActor(ActorSystemContext actorSystemContext) {
        super(actorSystemContext);
        if(actorSystemContext instanceof RuleEngineActorSystemContext){
            RuleEngineActorSystemContext systemContext = (RuleEngineActorSystemContext) actorSystemContext;
            this.tenantService = systemContext.getTenantService();
        }else{
            this.tenantService = null;
        }
        tenantActos = Maps.newHashMap();
    }

    @Override
    protected boolean process(IoTActorMessage msg) {
        switch (msg.getMsgType()){
            case SERVICE_TO_RULE_ENGINE_MSG:
                // service调用ruleEngine
                onServiceToRuleEngineMsg((ServiceToRuleEngineMsg)msg);
                break;
            case COMPONENT_EVENT_MSG:
                // 新增，修改或删除等事件变更的通知
                onComponentEventMsg((ComponentEventMsg)msg);
                break;
            case TRANSPORT_TO_RULE_ENGINE_ACTOR_MSG_WRAPPER:
                // 设备投递给规则引擎的消息
                onTransportToRuleEngineActorMsgWrapper((TransportToRuleEngineActorMsgWrapper) msg);
                break;
            default:
                return false;
        }
        return true;
    }

    /*
        元数据
        ry_device_01 元数据
            tenantId
            deviceId
            deviceName
            productId

        ry_device_01 上报的数据
        {
            "tempature" :  45
        }
     */

    // 设备投递给规则引擎的消息
    private void onTransportToRuleEngineActorMsgWrapper(TransportToRuleEngineActorMsgWrapper wrapper) {
        // 消息转换 Wrapper -> ruleEngineMsg
        TransportToRuleEngineActorMsg msg = wrapper.getMsg();
        if(msg != null){
            // 分类型进行消息处理
            if(msg.getPostTelemetryMsg() != null){
                handlePostTelemetryRequest(wrapper.getTenantId(), wrapper.getDeviceId(), msg.getPostTelemetryMsg());
            }
        }

    }

    private void handlePostTelemetryRequest(TenantId tenantId, DeviceId deviceId, PostTelemetryMsg postTelemetryMsg) {
        // 将转换好的Msg 发给RuleChain
        TsKvListProtoVO kvProto = postTelemetryMsg.getKvList();

        JsonObject dataJson = getJsonObject(kvProto.getKvs());

        long reciveTime = kvProto.getReviceTime();

        Map<String,String> data = Maps.newHashMap();
        data.put(RECIVE_TIME, reciveTime+"");
        data.put(TENANT_ID, tenantId.toString());
        data.put(DEVICE_ID, deviceId.toString());

        IoTMsgMetaData metaData = new IoTMsgMetaData(data);

        IoTMsg ioTMsg = new IoTMsg(
                UUIDHelper.genUuid(), POST_TELEMETRY_MSG.name(),  gson.toJson(dataJson), metaData);

        DeviceToRuleEngineMsg deviceToRuleEngineMsg = new DeviceToRuleEngineMsg(tenantId, ioTMsg);
        deviceToRuleEngineMsg.setFromDeviceMsgType(POST_TELEMETRY_MSG);

        // 将 DeviceToRuleEngineMsg 投递给TenantActor
        pushToRuleEngine(deviceToRuleEngineMsg);
    }

    /*
        将 DeviceToRuleEngineMsg 投递给TenantActor
     */
    private void pushToRuleEngine(DeviceToRuleEngineMsg deviceToRuleEngineMsg) {
        getOrCreateTenants(deviceToRuleEngineMsg.getTenantId()).tell(deviceToRuleEngineMsg, getSelf());
    }

    /*
        将 KeyValueProtoVO 中封装的设备上报数据，转换为Data的json
     */
    private JsonObject getJsonObject(List<KeyValueProtoVO> kvs) {
        JsonObject json = new JsonObject();
        for(KeyValueProtoVO vo : kvs){
            switch (vo.getType()) {
                case BOOLEAN_V:
                    json.addProperty(vo.getKey(), vo.getBoolValue());
                    break;
                case LONG_V:
                    json.addProperty(vo.getKey(), vo.getLongValue());
                    break;
                case DOUBLE_V:
                    json.addProperty(vo.getKey(), vo.getDoubleValue());
                    break;
                case STRING_V:
                    json.addProperty(vo.getKey(), vo.getStringValue());
                    break;
                default:
            }
        }
        return json;
    }

    private void onComponentEventMsg(ComponentEventMsg msg) {
        ActorRef tenantActor = getOrCreateTenants(msg.getTenantId());
        if(tenantActor != null){
            tenantActor.tell(msg, ActorRef.noSender());
        }
    }


    private void onServiceToRuleEngineMsg(ServiceToRuleEngineMsg msg) {
        ActorRef tenantActor = getOrCreateTenants(msg.getTenantId());
        tenantActor.tell(msg, self());
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

    private ActorRef getOrCreateTenants(TenantId tenantId) {
        ActorRef tenantActor = tenantActos.get(tenantId);
        if(tenantActor != null){
            return tenantActor;
        }

        tenantActor = getContext().actorOf(
                Props.create(
                        new RuleEngineTenantActor.ActorCreator(actorSystemContext, tenantId)).withDispatcher(ActorConfigs.TENANT_DISPATCHER_NAME),
                tenantId.toString()
        );

        tenantActos.put(tenantId, tenantActor);

        return tenantActor;
    }

    public static class ActorCreator extends ContextBaseCreator<RuleEngineAppActor> {
        public ActorCreator(ActorSystemContext actorSystemContext){
            super(actorSystemContext);
        }

        @Override
        public RuleEngineAppActor create() throws Exception {
            return new RuleEngineAppActor(actorSystemContext);
        }
    }

}
