package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages;

import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

import java.io.Serializable;

public class DeviceToRuleEngineMsg implements IoTActorMessage, Serializable {

    private final TenantId tenantId;

    private FromDeviceMsgType fromDeviceMsgType;

    private final IoTMsg msg;

    public DeviceToRuleEngineMsg(TenantId tenantId, IoTMsg msg) {
        this.tenantId = tenantId;
        this.msg = msg;
    }


    @Override
    public MsgType getMsgType() {
        return MsgType.DEVICE_TO_RULE_ENGINE_MSG;
    }

    public FromDeviceMsgType getFromDeviceMsgType() {
        return fromDeviceMsgType;
    }

    public void setFromDeviceMsgType(FromDeviceMsgType fromDeviceMsgType) {
        this.fromDeviceMsgType = fromDeviceMsgType;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public IoTMsg getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "DeviceToRuleEngineMsg{" +
                "tenantId=" + tenantId +
                ", msg=" + msg +
                '}';
    }
}
