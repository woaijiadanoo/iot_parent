package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity;

import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.EntityType;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.RelationTypeGroup;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRelationRepository;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRepository;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo.EntityRelationVO;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo.RuleNodeRelationVO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    public RuleChainMetaDataEntity(RuleChainId ruleChainId){
        super(ruleChainId);
        this.setId(ruleChainId);
        this.ruleNodeRepository = null;
        this.ruleNodeRelationRepository = null;
    }

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
            if(IoTStringUtils.isNotBlank(ruleNodeJson.get(RULE_NODE_ID_KEY).asText())){
                ruleNodeEntity.setId(new RuleNodeId(IoTStringUtils.toUUID(ruleNodeJson.get(RULE_NODE_ID_KEY).asText())));
            }
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
        // 1、维护firstRuleNodeId
        RuleNodeId firstRuleNodeId = null;
        if(nodes.get(firstNodeIndex).getId() != null){
            firstRuleNodeId = nodes.get(firstNodeIndex).getId();
        }
        // 2、维护ruleNode的表
        List<RuleNodeEntity> nodes = this.getNodes();
        List<RuleNodeEntity> toAddOrUpdate = Lists.newArrayList();
        List<RuleNodeEntity> toDelete = Lists.newArrayList();

        Map<RuleNodeId, Integer> ruleNodeIndexMap = Maps.newHashMap();
        if(nodes != null){
            for(RuleNodeEntity node : nodes){
                if(node.getId() != null){
                    ruleNodeIndexMap.put(node.getId(), nodes.indexOf(node));
                }else{
                    toAddOrUpdate.add(node);
                }
            }
        }

        List<RuleNodeEntity> existingRuleNodes = getRuleChainNodes(this.getId());
        for(RuleNodeEntity existingRuleNode : existingRuleNodes){
            // 首先删除所有跟ruleNodeId有关的relation信息
            deleteEntityRelations(existingRuleNode.getId());

            Integer nodeindex = ruleNodeIndexMap.get(existingRuleNode.getId());
            if(nodeindex != null){
                // nodeIndex如果存在，说明表中有，同时传入的内容也有，则进行修改
                toAddOrUpdate.add(nodes.get(nodeindex));
            }else{
                // nodeIndex如果不存在，说明表中有，同时传入的内容没有，则进行删除
                toDelete.add(existingRuleNode);
            }
        }

        // 2.1 维护待新增或者待修改的【tips: 注意要维护firstRuleNode】
        for(RuleNodeEntity node : toAddOrUpdate){
            node.setRuleChainId(this.getId());
            // 无论是新增还是修改，都可以用这个方法
            RuleNodeEntity saveNode = ruleNodeRepository.saveNode(node);
            // ruleChain 到 ruleNode之间的连接关系
            createRelation(
                    new EntityRelationVO(
                            this.getId(), saveNode.getId(), EntityRelationVO.CONTAINS_TYPE, RelationTypeGroup.RULE_CHAIN));

            int index = nodes.indexOf(saveNode);
            if(index == firstNodeIndex){
                firstRuleNodeId = saveNode.getId();
            }

            // 将新增的node维护到Map里
            nodes.set(index, saveNode);
            ruleNodeIndexMap.put(saveNode.getId(), index);
        }

        // 2.2 维护待删除的
        for(RuleNodeEntity node : toDelete){
            deleteRuleNode(node.getId());
        }

        // 3、维护ruleNodeRelation的表
        if(this.getConnections() != null){
            for(RuleNodeRelationVO nodeConnection : this.getConnections()){
                // 假设我们的前端给的是对的。
                RuleNodeId from = nodes.get(nodeConnection.getFromIndex()).getId();
                RuleNodeId to = nodes.get(nodeConnection.getToIndex()).getId();
                String type = nodeConnection.getRelationType();
                RelationTypeGroup typeGroup = RelationTypeGroup.RULE_NODE;
                // 设置节点和节点之间的连接关系
                createRelation(new EntityRelationVO(from, to, type, typeGroup));

            }
        }

        return firstRuleNodeId;
    }

    /*
        获取某个ruleChain下的所有已存在的RuleNode列表
     */
    private List<RuleNodeEntity> getRuleChainNodes(RuleChainId ruleChainId){
        List<EntityRelationVO> relations = getRuleChainToNodeRelations(ruleChainId);
        List<RuleNodeEntity> ruleNodes = Lists.newArrayList();
        for(EntityRelationVO relation : relations){
            RuleNodeEntity ruleNode = ruleNodeRepository.findById((RuleNodeId) relation.getTo());
            if(ruleNode != null){
                ruleNodes.add(ruleNode);
            }else{
                // 如果不存在，记得要删除对应的relation关系
                ruleNodeRelationRepository.deleteRelation(relation);
            }
        }

        return ruleNodes;
    }

    private void deleteEntityRelations(EntityId entityId){
        ruleNodeRelationRepository.deleteEntityRelations(entityId);
    }

    private void createRelation(EntityRelationVO relationVO){
        ruleNodeRelationRepository.saveRelation(relationVO);
    }

    private void deleteRuleNode(EntityId entityId){
        deleteEntityRelations(entityId);
        ruleNodeRepository.removeById(entityId.getUuid());
    }

    private List<EntityRelationVO> getRuleChainToNodeRelations(RuleChainId ruleChainId){
        List<EntityRelationVO> relationVOList = ruleNodeRelationRepository.findByFrom(ruleChainId, RelationTypeGroup.RULE_CHAIN);
        return relationVOList;
    }

    public RuleChainMetaDataEntity loadRuleChainMetaData(RuleChainId ruleChainId, RuleNodeId firstRuleNodeId){
        // 组装nodes属性
        List<RuleNodeEntity> ruleNodes = getRuleChainNodes(ruleChainId);
        Map<RuleNodeId, Integer> ruleNodeIndexMap = Maps.newHashMap();
        for(RuleNodeEntity node : ruleNodes){
            ruleNodeIndexMap.put(node.getId(), ruleNodes.indexOf(node));
        }

        this.setNodes(ruleNodes);
        if(firstRuleNodeId != null){
            this.setFirstNodeIndex(ruleNodeIndexMap.get(firstRuleNodeId));
        }

        // 组装connections属性
        for(RuleNodeEntity node : ruleNodes){
            int fromIndex = ruleNodeIndexMap.get(node.getId());
            List<EntityRelationVO> nodeRelations = getRuleNodeRelations(node.getId());
            for(EntityRelationVO relation : nodeRelations){
                String type = relation.getType();
                if(relation.getTo().getEntityType() == EntityType.RULE_NODE){
                    RuleNodeId toNodeId = new RuleNodeId(relation.getTo().getUuid());
                    Integer toIndex = ruleNodeIndexMap.get(toNodeId);
                    // 将EntityRelationVO 转换为 RuleNodeRelationVO
                    this.addConnection(ruleChainId, fromIndex, toIndex, type);
                }
            }
        }

        return this;
    }

    public void addConnection(RuleChainId ruleChainId, int fromIndex, int toIndex, String relationType){
        RuleNodeRelationVO vo = new RuleNodeRelationVO();
        vo.setRuleChainId(ruleChainId);
        vo.setFromIndex(fromIndex);
        vo.setToIndex(toIndex);
        vo.setRelationType(relationType);

        connections.add(vo);
    }

    /*
        以ruleNodeId为from节点的所有relation数据
     */
    public List<EntityRelationVO> getRuleNodeRelations(RuleNodeId ruleNodeId){
        List<EntityRelationVO> relations =
                ruleNodeRelationRepository.findByFrom(ruleNodeId, RelationTypeGroup.RULE_NODE);
        List<EntityRelationVO> validRelations = Lists.newArrayList();
        for (EntityRelationVO relation : relations) {
            boolean valid = true;
            EntityType toType = relation.getTo().getEntityType();
            if(toType == EntityType.RULE_NODE){
                RuleNodeEntity entity = null;
                if(relation.getTo().getEntityType() == EntityType.RULE_NODE){
                    entity = ruleNodeRepository.findById((RuleNodeId) relation.getTo());
                }
                if(entity == null){
                    ruleNodeRelationRepository.deleteRelation(relation);
                    valid = false;
                }
            }
            if(valid){
                validRelations.add(relation);
            }
        }

        return validRelations;
    }

    public RuleNodeEntity findRuleNodeById(RuleNodeId ruleNodeId) {
        return ruleNodeRepository.findById(ruleNodeId);
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
