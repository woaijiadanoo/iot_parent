package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.RelationTypeGroup;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo.EntityRelationVO;

import java.util.List;

public interface RuleNodeRelationRepository {

    void saveRelation(EntityRelationVO relation);

    // 按from查所有的关系
    List<EntityRelationVO> findByFrom(EntityId entityId, RelationTypeGroup typeGroup);

    // 按组合条件进行删除
    boolean deleteRelation(EntityRelationVO relation);

    /*
        删除所有跟entityId有关的内容
            ruleChainId   01   ruleNodes  02 03 04
            01
                02 -> success -> 03
                02 -> failture -> 04

           01
                02 -> success -> 03
                03 -> success -> 05
                05 -> failture -> 04
     */
    void deleteEntityRelations(EntityId entityId);

}
