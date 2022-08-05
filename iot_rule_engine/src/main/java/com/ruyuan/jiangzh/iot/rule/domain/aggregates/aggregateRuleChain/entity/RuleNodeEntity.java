package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.po.RuleNodePO;

import java.io.Serializable;

public class RuleNodeEntity   extends CreateTimeIdBase<RuleNodeId> implements Serializable {

    private static final String JSON_READ_FAILTURE_MSG_ID = "default.json_read_failture";

    private RuleChainId ruleChainId;
    private String nodeType;
    private String nodeName;

    private JsonNode configuration;

    private static ObjectMapper objectMapper = new ObjectMapper();
    public RuleNodePO entityToPo(){
        RuleNodePO po = new RuleNodePO();
        // 默认一定有ID， 如果是新增没有ID，则先设置ID再调用
        po.setUuid(UUIDHelper.fromTimeUUID(this.getId().getUuid()));
        po.setRuleChainId(UUIDHelper.fromTimeUUID(this.getRuleChainId().getUuid()));
        po.setNodeName(this.getNodeName());
        po.setNodeType(this.getNodeType());
        po.setConfiguration(this.getConfiguration().toString());

        return po;
    }

    public static RuleNodeEntity poToEntity(RuleNodePO po){
        RuleNodeEntity entity = new RuleNodeEntity();

        entity.setId(new RuleNodeId(UUIDHelper.fromStringId(po.getUuid())));
        entity.setRuleChainId(new RuleChainId(UUIDHelper.fromStringId(po.getRuleChainId())));
        entity.setNodeName(po.getNodeName());
        entity.setNodeType(po.getNodeType());
        try {
            entity.setConfiguration(objectMapper.readTree(po.getConfiguration()));
        } catch (JsonProcessingException e) {
            e.getStackTrace();
            throw new AppException(500 , JSON_READ_FAILTURE_MSG_ID);
        }

        return entity;
    }


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
