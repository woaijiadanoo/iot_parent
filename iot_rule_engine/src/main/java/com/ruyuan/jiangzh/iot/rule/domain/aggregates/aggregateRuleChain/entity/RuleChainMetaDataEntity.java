package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity;

import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
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


    // node对应的属性
    private static final String RULE_NODE_ID_KEY = "nodeId";
    private static final String RULE_NODE_NAME_KEY = "nodeName";
    private static final String RULE_NODE_TYPE_KEY = "nodeType";
    private static final String RULE_NODE_CONFIGURATION_KEY = "configuration";

    // connection 对应的属性
    private static final String RELATION_FROM_KEY = "fromIndex";
    private static final String RELATION_TO_KEY = "toIndex";
    private static final String RELATION_TYPE_KEY = "type";

    private ObjectMapper objectMapper = new ObjectMapper();


    public RuleChainMetaDataEntity(RuleNodeRepository ruleNodeRepository,RuleNodeRelationRepository ruleNodeRelationRepository){
        this.ruleNodeRepository = ruleNodeRepository;
        this.ruleNodeRelationRepository = ruleNodeRelationRepository;
    }

    /*
        添加规则节点
     */
    public void addNode(JsonNode ruleNodeJson){
        RuleNodeEntity ruleNodeEntity = new RuleNodeEntity();
        ruleNodeEntity.setRuleChainId(this.getId());
        // 防止新增没有NodeId
        if (jvNotEmpty(ruleNodeJson, RULE_NODE_ID_KEY)) {
            ruleNodeEntity.setId(new RuleNodeId(IoTStringUtils.toUUID(ruleNodeJson.get(RULE_NODE_ID_KEY).asText())));
        }
        ruleNodeEntity.setNodeName(ruleNodeJson.get(RULE_NODE_NAME_KEY).asText());
        ruleNodeEntity.setNodeType(ruleNodeJson.get(RULE_NODE_TYPE_KEY).asText());
        ruleNodeEntity.setConfiguration(ruleNodeJson.get(RULE_NODE_CONFIGURATION_KEY));

        nodes.add(ruleNodeEntity);
    }

    /*
        添加规则节点链接
     */
    public void addConnection(JsonNode connectionJson){
        RuleNodeRelationVO relationVO = new RuleNodeRelationVO();
        relationVO.setRuleChainId(this.getId());

        relationVO.setFromIndex(connectionJson.get(RELATION_FROM_KEY).asInt());
        relationVO.setToIndex(connectionJson.get(RELATION_TO_KEY).asInt());
        relationVO.setRelationType(connectionJson.get(RELATION_TYPE_KEY).asText());

        connections.add(relationVO);
    }

    public ArrayNode nodesToArrayNode(){
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for(RuleNodeEntity node : nodes){
            ObjectNode objectNode = objectMapper.createObjectNode();

            objectNode.put(RULE_NODE_ID_KEY, node.getId().toString());
            objectNode.put(RULE_NODE_NAME_KEY, node.getNodeName());
            objectNode.put(RULE_NODE_TYPE_KEY, node.getNodeType());

            objectNode.set(RULE_NODE_CONFIGURATION_KEY, node.getConfiguration());

            arrayNode.add(objectNode);
        }
        return arrayNode;
    }

    public ArrayNode connectionsToArrayNode(){
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for(RuleNodeRelationVO vo : connections){
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put(RELATION_FROM_KEY,vo.getFromIndex());
            objectNode.put(RELATION_TO_KEY,vo.getToIndex());
            objectNode.put(RELATION_TYPE_KEY,vo.getRelationType());

            arrayNode.add(objectNode);
        }
        return arrayNode;
    }

    /*
        service ================================================>
     */

    /**
     * RuleNodeId : firstRuleNodeId -> 用来判断RuleChainEntity里的firstRuleNodeId与新保存的是否一致
     * @return
     */
    public RuleNodeId saveRuleChainMetaData(){

        return null;
    }

    public RuleChainMetaDataEntity loadRuleChainMetaData(RuleChainId ruleChainId, RuleNodeId firstRuleNodeId){

        return null;
    }

    /*
        getter & setter ==========================================>
     */

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



    private boolean jvNotEmpty(JsonNode inputJson,String key){
        if(!inputJson.isEmpty() && !inputJson.get(key).isNull()){
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "RuleChainMetaDataEntity{" +
                "ruleNodeRepository=" + ruleNodeRepository +
                ", ruleNodeRelationRepository=" + ruleNodeRelationRepository +
                ", firstNodeIndex=" + firstNodeIndex +
                ", nodes=" + nodes +
                ", connections=" + connections +
                '}';
    }
}
