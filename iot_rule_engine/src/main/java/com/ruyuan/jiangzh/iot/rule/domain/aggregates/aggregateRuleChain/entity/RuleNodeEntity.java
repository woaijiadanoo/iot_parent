package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;

import java.io.Serializable;

public class RuleNodeEntity   extends CreateTimeIdBase<RuleNodeId> implements Serializable {

    private RuleChainId ruleChainId;
    private String nodeType;
    private String nodeName;

    private JsonNode configuration;

    public RuleChainId getRuleChainId() {
        return ruleChainId;
    }

    public void setRuleChainId(RuleChainId ruleChainId) {
        this.ruleChainId = ruleChainId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public JsonNode getConfiguration() {
        return configuration;
    }

    public void setConfiguration(JsonNode configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return "RuleNodeEntity{" +
                "ruleChainId=" + ruleChainId +
                ", nodeType='" + nodeType + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", configuration=" + configuration +
                '}';
    }
}
