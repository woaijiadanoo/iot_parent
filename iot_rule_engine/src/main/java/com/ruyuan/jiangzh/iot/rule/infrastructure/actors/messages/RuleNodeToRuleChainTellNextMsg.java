package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages;

import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineContext;

import java.util.Set;

public class RuleNodeToRuleChainTellNextMsg implements IoTActorMessage {

    private final RuleNodeId originator;
    private final Set<String> relationTypes;
    private final IoTMsg msg;

    public RuleNodeToRuleChainTellNextMsg(RuleNodeId originator, Set<String> relationTypes, IoTMsg msg) {
        this.originator = originator;
        this.relationTypes = relationTypes;
        this.msg = msg;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.RULE_NODE_TO_RULE_CHAIN_TELL_NEXT_MSG;
    }

    public RuleNodeId getOriginator() {
        return originator;
    }

    public Set<String> getRelationTypes() {
        return relationTypes;
    }

    public IoTMsg getMsg() {
        return msg;
    }
}
