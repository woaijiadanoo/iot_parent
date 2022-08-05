package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo;

import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.RelationTypeGroup;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.po.RuleNodeRelationPO;

public class EntityRelationVO {

    private EntityId from;
    private EntityId to;
    private String type;
    private RelationTypeGroup typeGroup;

    public EntityRelationVO(){}

    public EntityRelationVO(EntityId from, EntityId to, String type, RelationTypeGroup typeGroup){
        this.from = from;
        this.to = to;
        this.type = type;
        this.typeGroup = typeGroup;
    }

    public RuleNodeRelationPO voToPo(){
        RuleNodeRelationPO po = new RuleNodeRelationPO();
        // from的存储逻辑
        po.setFromId(UUIDHelper.fromTimeUUID(this.getFrom().getUuid()));
        if(this.getFrom() instanceof RuleChainId){
            po.setFromType(RelationTypeGroup.RULE_CHAIN.toString());
        }else if(this.getFrom() instanceof RuleNodeId){
            po.setFromType(RelationTypeGroup.RULE_NODE.toString());
        }

        // to的存储逻辑
        po.setToId(UUIDHelper.fromTimeUUID(this.getTo().getUuid()));
        if(this.getTo() instanceof RuleChainId){
            po.setToType(RelationTypeGroup.RULE_CHAIN.toString());
        }else if(this.getTo() instanceof RuleNodeId){
            po.setToType(RelationTypeGroup.RULE_NODE.toString());
        }

        po.setRelationTypeGroup(this.getTypeGroup().toString());
        po.setRelationType(this.getType());

        return po;
    }

    public static EntityRelationVO poToVo(RuleNodeRelationPO po){
        // 获取fromId
        EntityId fromId = null;
        if(RelationTypeGroup.valueOf(po.getFromType()).equals(RelationTypeGroup.RULE_CHAIN)){
            fromId = new RuleChainId(UUIDHelper.fromStringId(po.getFromId()));
        } else if (RelationTypeGroup.valueOf(po.getFromType()).equals(RelationTypeGroup.RULE_NODE)) {
            fromId = new RuleNodeId(UUIDHelper.fromStringId(po.getFromId()));
        }

        // 获取toId
        EntityId toId = null;
        if(RelationTypeGroup.valueOf(po.getToType()).equals(RelationTypeGroup.RULE_CHAIN)){
            toId = new RuleChainId(UUIDHelper.fromStringId(po.getToId()));
        } else if (RelationTypeGroup.valueOf(po.getToType()).equals(RelationTypeGroup.RULE_NODE)) {
            toId = new RuleNodeId(UUIDHelper.fromStringId(po.getToId()));
        }

        String relationType = po.getRelationType();
        RelationTypeGroup relationTypeGroup = RelationTypeGroup.valueOf(po.getRelationTypeGroup());

        EntityRelationVO vo = new EntityRelationVO(fromId, toId, relationType, relationTypeGroup);
        return vo;
    }

    public EntityId getFrom() {
        return from;
    }

    public void setFrom(EntityId from) {
        this.from = from;
    }

    public EntityId getTo() {
        return to;
    }

    public void setTo(EntityId to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RelationTypeGroup getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(RelationTypeGroup typeGroup) {
        this.typeGroup = typeGroup;
    }

    @Override
    public String toString() {
        return "EntityRelationVO{" +
                "from=" + from +
                ", to=" + to +
                ", type='" + type + '\'' +
                ", typeGroup=" + typeGroup +
                '}';
    }
}
