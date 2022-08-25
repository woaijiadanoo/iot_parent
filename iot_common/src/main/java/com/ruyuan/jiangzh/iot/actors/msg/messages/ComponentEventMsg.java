package com.ruyuan.jiangzh.iot.actors.msg.messages;

import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.actors.msg.ToAllNodesMsg;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;

public class ComponentEventMsg implements IoTActorMessage, ToAllNodesMsg {

    private final TenantId tenantId;

    private final RuleChainId ruleChainId;

    private final ComponentEventEnum event;

    public ComponentEventMsg(TenantId tenantId, RuleChainId ruleChainId, ComponentEventEnum event) {
        this.tenantId = tenantId;
        this.ruleChainId = ruleChainId;
        this.event = event;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.COMPONENT_EVENT_MSG;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public RuleChainId getRuleChainId() {
        return ruleChainId;
    }

    public ComponentEventEnum getEvent() {
        return event;
    }
}
