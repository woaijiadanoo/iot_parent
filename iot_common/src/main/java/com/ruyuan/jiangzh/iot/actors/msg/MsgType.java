package com.ruyuan.jiangzh.iot.actors.msg;

public enum MsgType {

    // 服务调用规则引擎
    SERVICE_TO_RULE_ENGINE_MSG,

    // ruleChain向ruleNode发送消息
    RULE_CHAIN_TO_RULE_NODE_MSG,

    // ruleNode向ruleChain发送消息
    RULE_NODE_TO_RULE_CHAIN_TELL_NEXT_MSG,

    // 通知节点变更事件的消息
    COMPONENT_EVENT_MSG,

    // 设备上线通知消息
    PROTOCOL_ONLINE_MSG
    ;

}
