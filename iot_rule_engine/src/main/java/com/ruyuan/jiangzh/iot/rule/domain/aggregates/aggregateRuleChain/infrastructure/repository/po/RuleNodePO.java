package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
/**
 * <p>
 * 
 * </p>
 *
 * @author jiangzh
 * @since 2022-08-05
 */
@TableName("rule_node")
public class RuleNodePO extends Model<RuleNodePO> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String uuid;

    private String ruleChainId;

    private String configuration;

    private String nodeType;

    private String nodeName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRuleChainId() {
        return ruleChainId;
    }

    public void setRuleChainId(String ruleChainId) {
        this.ruleChainId = ruleChainId;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
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

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "RuleNode{" +
        ", uuid=" + uuid +
        ", ruleChainId=" + ruleChainId +
        ", configuration=" + configuration +
        ", nodeType=" + nodeType +
        ", nodeName=" + nodeName +
        "}";
    }
}
