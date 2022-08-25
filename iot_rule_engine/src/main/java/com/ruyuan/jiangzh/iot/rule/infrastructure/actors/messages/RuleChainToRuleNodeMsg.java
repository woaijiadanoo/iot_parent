package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.messages;

import com.ruyuan.jiangzh.iot.actors.msg.IoTActorMessage;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.MsgType;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineContext;

public class RuleChainToRuleNodeMsg implements IoTActorMessage {

    private final RuleEngineContext ruleEngineCtx;
    private final IoTMsg msg;
    private final String fromRelationType;

    public RuleChainToRuleNodeMsg(RuleEngineContext ruleEngineCtx, IoTMsg msg, String fromRelationType) {
        this.ruleEngineCtx = ruleEngineCtx;
        this.msg = msg;
        this.fromRelationType = fromRelationType;
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.RULE_CHAIN_TO_RULE_NODE_MSG;
    }

    public RuleEngineContext getRuleEngineCtx() {
        return ruleEngineCtx;
    }

    public IoTMsg getMsg() {
        return msg;
    }

    public String getFromRelationType() {
        return fromRelationType;
    }
}
