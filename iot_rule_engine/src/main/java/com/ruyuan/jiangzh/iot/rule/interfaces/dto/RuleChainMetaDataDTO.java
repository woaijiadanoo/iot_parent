package com.ruyuan.jiangzh.iot.rule.interfaces.dto;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class RuleChainMetaDataDTO {

    private String ruleChainId;

    private int firstNodeIndex;

    private ArrayNode nodes;

    private ArrayNode connections;



    public String getRuleChainId() {
        return ruleChainId;
    }

    public void setRuleChainId(String ruleChainId) {
        this.ruleChainId = ruleChainId;
    }

    public int getFirstNodeIndex() {
        return firstNodeIndex;
    }

    public void setFirstNodeIndex(int firstNodeIndex) {
        this.firstNodeIndex = firstNodeIndex;
    }

    public ArrayNode getNodes() {
        return nodes;
    }

    public void setNodes(ArrayNode nodes) {
        this.nodes = nodes;
    }

    public ArrayNode getConnections() {
        return connections;
    }

    public void setConnections(ArrayNode connections) {
        this.connections = connections;
    }

    @Override
    public String toString() {
        return "RuleChainMetaDataDTO{" +
                "ruleChainId='" + ruleChainId + '\'' +
                ", firstNodeIndex=" + firstNodeIndex +
                ", nodes=" + nodes +
                ", connections=" + connections +
                '}';
    }
}
