package com.ruyuan.jiangzh.iot.rule.infrastructure.engine;

import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineContext;

public interface RuleEngineNode {

    // 初始化
    void init(RuleEngineContext ctx, RuleEngineNodeConfiguration configuration);

    // 实例处理消息的逻辑
    void onMsg(RuleEngineContext ctx, IoTMsg msg);

    // 销毁时回收资源
    void destory();

}
