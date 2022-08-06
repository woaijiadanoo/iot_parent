package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.RelationTypeGroup;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRelationRepository;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.impl.mapper.RuleNodeRelationMapper;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.po.RuleNodeRelationPO;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo.EntityRelationVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RuleNodeRelationRepositoryImpl implements RuleNodeRelationRepository {

    @Resource
    private RuleNodeRelationMapper relationMapper;

    @Override
    public void saveRelation(EntityRelationVO relation) {
        RuleNodeRelationPO relationPO = queryRuleNodeRelation(relation);
        if(relationPO == null){
            RuleNodeRelationPO inputPO = relation.voToPo();
            relationMapper.insert(inputPO);
        }
    }

    @Override
    public List<EntityRelationVO> findByFrom(EntityId fromId, RelationTypeGroup typeGroup) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("from_id", UUIDHelper.fromTimeUUID(fromId.getUuid()));
        queryWrapper.eq("relation_type_group", typeGroup.toString());

        List<RuleNodeRelationPO> fromPOs = relationMapper.selectList(queryWrapper);
        // 数据类型转换
        if(fromPOs != null && fromPOs.size() > 0){
            List<EntityRelationVO> result =
                    fromPOs
                            .stream()
                            .map(po -> EntityRelationVO.poToVo(po))
                            .collect(Collectors.toList());

            return result;
        }

        return Lists.newArrayList();
    }

    @Override
    public void deleteRelation(EntityRelationVO relation) {
        if(queryRuleNodeRelation(relation) != null){
            QueryWrapper queryWrapper = getRuleNodeRelationQueryWrapper(relation);
            relationMapper.delete(queryWrapper);
        }
    }

    @Override
    public void deleteEntityRelations(EntityId entityId) {
        // TODO 简易实现
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("from_id", UUIDHelper.fromTimeUUID(entityId.getUuid()));

        relationMapper.delete(queryWrapper);
    }


    private RuleNodeRelationPO queryRuleNodeRelation(EntityRelationVO relation){
        List<RuleNodeRelationPO> result =
                relationMapper.selectList(getRuleNodeRelationQueryWrapper(new EntityRelationVO()));

        return (result != null && result.size() > 0) ? result.get(0) : null;
    }

    private QueryWrapper getRuleNodeRelationQueryWrapper(EntityRelationVO relation){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("from_id", UUIDHelper.fromTimeUUID(relation.getFrom().getUuid()));
        queryWrapper.eq("to_id", UUIDHelper.fromTimeUUID(relation.getTo().getUuid()));
        queryWrapper.eq("relation_type", relation.getType());
        queryWrapper.eq("relation_type_group", relation.getTypeGroup().toString());

        return queryWrapper;
    }

}
