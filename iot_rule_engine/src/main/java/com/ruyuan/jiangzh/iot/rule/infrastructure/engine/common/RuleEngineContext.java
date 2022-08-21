package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common;

import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNode;

public interface RuleEngineContext {

    RuleNodeId getSelfId();



    String getNodeId();

    void tellNext(RuleEngineMsg msg, String relationType);

    void tellSelf(RuleEngineMsg msg, long delayMs);

    void tellFailure(RuleEngineMsg msg, Throwable throwable);

    RuleEngineMsg transformMsg(
            RuleEngineMsg origMsg, String type, EntityId originator
            , RuleEngineMsgMetaData metaData, String data);

    void updateSelf(RuleNodeEntity node);

}
