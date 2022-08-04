package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo;

import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;

import java.io.Serializable;

public class RuleNodeRelationVO  implements Serializable {

    private RuleChainId ruleChainId;

    private Integer fromIndex;
    private Integer toIndex;

    private String relationType;

    public RuleChainId getRuleChainId() {
        return ruleChainId;
    }

    public void setRuleChainId(RuleChainId ruleChainId) {
        this.ruleChainId = ruleChainId;
    }

    public Integer getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(Integer fromIndex) {
        this.fromIndex = fromIndex;
    }

    public Integer getToIndex() {
        return toIndex;
    }

    public void setToIndex(Integer toIndex) {
        this.toIndex = toIndex;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    @Override
    public String toString() {
        return "RuleNodeRelationVO{" +
                "ruleChainId=" + ruleChainId +
                ", fromIndex='" + fromIndex + '\'' +
                ", toIndex='" + toIndex + '\'' +
                ", relationType='" + relationType + '\'' +
                '}';
    }
}
