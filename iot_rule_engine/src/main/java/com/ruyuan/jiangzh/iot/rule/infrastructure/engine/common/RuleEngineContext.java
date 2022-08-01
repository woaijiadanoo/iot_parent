package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common;

import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;

public interface RuleEngineContext {

    RuleNodeId getSelfId();

    String getNodeId();

    void tellNext(RuleEngineMsg msg, String relationType);

    void tellSelf(RuleEngineMsg msg, long delayMs);

    void tellFailure(RuleEngineMsg msg, Throwable throwable);

    RuleEngineMsg transformMsg(
            RuleEngineMsg origMsg, String type, EntityId originator
            , RuleEngineMsgMetaData metaData, String data);

}
