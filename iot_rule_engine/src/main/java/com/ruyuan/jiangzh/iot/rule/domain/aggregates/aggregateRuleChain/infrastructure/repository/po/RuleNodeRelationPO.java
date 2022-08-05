package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.po;

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
@TableName("rule_node_relation")
public class RuleNodeRelationPO extends Model<RuleNodeRelationPO> {

    private static final long serialVersionUID = 1L;

    private String fromId;

    private String fromType;

    private String toId;

    private String toType;

    private String relationTypeGroup;

    private String relationType;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getRelationTypeGroup() {
        return relationTypeGroup;
    }

    public void setRelationTypeGroup(String relationTypeGroup) {
        this.relationTypeGroup = relationTypeGroup;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "RuleNodeRelation{" +
        ", fromId=" + fromId +
        ", fromType=" + fromType +
        ", toId=" + toId +
        ", toType=" + toType +
        ", relationTypeGroup=" + relationTypeGroup +
        ", relationType=" + relationType +
        "}";
    }
}
