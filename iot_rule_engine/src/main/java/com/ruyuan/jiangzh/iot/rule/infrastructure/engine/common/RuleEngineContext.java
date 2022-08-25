package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common;

import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsgMetaData;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNode;

public interface RuleEngineContext {

    RuleNodeId getSelfId();

    String getNodeId();

    void tellNext(IoTMsg msg, String relationType);

    void tellSelf(IoTMsg msg, long delayMs);

    void tellFailure(IoTMsg msg, Throwable throwable);

    IoTMsg transformMsg(
            IoTMsg origMsg, String type, EntityId originator
            , IoTMsgMetaData metaData, String data);

    void updateSelf(RuleNodeEntity node);

}
