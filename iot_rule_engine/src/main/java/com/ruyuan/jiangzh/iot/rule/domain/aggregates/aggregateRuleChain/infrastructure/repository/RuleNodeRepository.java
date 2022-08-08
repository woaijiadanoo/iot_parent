package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;

import java.util.UUID;

public interface RuleNodeRepository {

    RuleNodeEntity findById(RuleNodeId ruleNodeId);

    /*
        新增和修改都是这个save
     */
    RuleNodeEntity saveNode(RuleNodeEntity ruleNode);

    void removeById(UUID uuid);

}
