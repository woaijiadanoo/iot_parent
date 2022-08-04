package com.ruyuan.jiangzh.iot.rule.interfaces.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleChainMetaDataEntity;

public class RuleChainMetaDataDTO {

    private String ruleChainId;

    private int firstNodeIndex;

    private ArrayNode nodes;

    private ArrayNode connections;


    public static void dtoToEntity(RuleChainMetaDataDTO dto, RuleChainMetaDataEntity entity){
        // 考虑到新增没有Id的情况
        if(IoTStringUtils.isNotBlank(dto.getRuleChainId())){
            entity.setId(new RuleChainId(IoTStringUtils.toUUID(dto.getRuleChainId())));
        }

        // 节点的添加
        if(!dto.getNodes().isNull() && !dto.getNodes().isEmpty()){
            for(JsonNode ruleNodeJson : dto.getNodes()){
                entity.addNode(ruleNodeJson);
            }
        }

        // 连接信息的添加
        if(!dto.getConnections().isNull() && !dto.getConnections().isEmpty()){
            for(JsonNode connectionJson : dto.getConnections()){
                entity.addConnection(connectionJson);
            }
        }

        // 必须有，没有就没必要保存了
        entity.setFirstNodeIndex(dto.getFirstNodeIndex());
    }

    public static RuleChainMetaDataDTO entity2Dto(RuleChainMetaDataEntity entity){
        RuleChainMetaDataDTO dto = new RuleChainMetaDataDTO();

        dto.setRuleChainId(entity.getId().toString());
        dto.setFirstNodeIndex(entity.getFirstNodeIndex());
        dto.setNodes(entity.nodesToArrayNode());
        dto.setConnections(entity.connectionsToArrayNode());

        return dto;
    }


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
