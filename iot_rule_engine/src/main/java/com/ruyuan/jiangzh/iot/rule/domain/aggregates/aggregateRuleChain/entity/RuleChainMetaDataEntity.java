package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity;

import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRelationRepository;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRepository;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo.RuleNodeRelationVO;

import java.io.Serializable;
import java.util.List;

public class RuleChainMetaDataEntity  extends CreateTimeIdBase<RuleChainId> implements Serializable {

    private RuleNodeRepository ruleNodeRepository;
    private RuleNodeRelationRepository ruleNodeRelationRepository;

    private Integer firstNodeIndex;
    private List<RuleNodeEntity> nodes = Lists.newArrayList();
    private List<RuleNodeRelationVO> connections = Lists.newArrayList();

    public RuleChainMetaDataEntity(RuleNodeRepository ruleNodeRepository,RuleNodeRelationRepository ruleNodeRelationRepository){
        this.ruleNodeRepository = ruleNodeRepository;
        this.ruleNodeRelationRepository = ruleNodeRelationRepository;
    }


    public Integer getFirstNodeIndex() {
        return firstNodeIndex;
    }

    public void setFirstNodeIndex(Integer firstNodeIndex) {
        this.firstNodeIndex = firstNodeIndex;
    }

    public List<RuleNodeEntity> getNodes() {
        return nodes;
    }

    public void setNodes(List<RuleNodeEntity> nodes) {
        this.nodes = nodes;
    }

    public List<RuleNodeRelationVO> getConnections() {
        return connections;
    }

    public void setConnections(List<RuleNodeRelationVO> connections) {
        this.connections = connections;
    }
}
