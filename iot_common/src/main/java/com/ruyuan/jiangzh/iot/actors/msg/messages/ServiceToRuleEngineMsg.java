package com.ruyuan.jiangzh.iot.actors.msg.messages;

import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.io.Serializable;

public class ServiceToRuleEngineMsg implements IoTActorMessage, Serializable {
    private final TenantId tenantId;

    private final IoTMsg msg;

    public ServiceToRuleEngineMsg(TenantId tenantId, IoTMsg msg) {
        this.tenantId = tenantId;
        this.msg = msg;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.SERVICE_TO_RULE_ENGINE_MSG;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public IoTMsg getMsg() {
        return msg;
    }
}
