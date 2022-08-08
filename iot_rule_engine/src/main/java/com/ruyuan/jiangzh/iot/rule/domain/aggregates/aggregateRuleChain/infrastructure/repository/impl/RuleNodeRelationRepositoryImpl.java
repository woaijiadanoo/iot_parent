package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.RelationTypeGroup;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRelationRepository;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.impl.mapper.RuleNodeRelationMapper;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.po.RuleNodeRelationPO;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.vo.EntityRelationVO;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
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
    public boolean deleteRelation(EntityRelationVO relation) {
        if(queryRuleNodeRelation(relation) != null){
            QueryWrapper queryWrapper = getRuleNodeRelationQueryWrapper(relation);
            relationMapper.delete(queryWrapper);
        }
        return true;
    }


    private RuleNodeRelationPO queryRuleNodeRelation(EntityRelationVO relation){
        List<RuleNodeRelationPO> result =
                relationMapper.selectList(getRuleNodeRelationQueryWrapper(relation));

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


    /**
     *  1、删除应该先删ToId为传入ID的内容
     *  2、需要做异步处理，不能影响主逻辑
     * @param entityId
     */

    private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    @PreDestroy
    void onDestroy(){
        if(service != null){
            service.shutdown();
        }
    }

    @Override
    public void deleteEntityRelations(EntityId entityId) {
        try {
            // 改成异步做删除
            deleteEntityRelationsAsync(entityId).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(500, "default.premission_denied");
        }
    }

    private ListenableFuture<Void> deleteEntityRelationsAsync(EntityId entityId){
        // 要先删除所有ToId为entityId的内容
        List<ListenableFuture<List<EntityRelationVO>>> toRelationList = Lists.newArrayList();
        for(RelationTypeGroup typeGroup : RelationTypeGroup.values()){
            toRelationList.add(findAllByToId(entityId, typeGroup));
        }

        ListenableFuture<List<List<EntityRelationVO>>> toRelations = Futures.allAsList(toRelationList);
        ListenableFuture<List<Boolean>> toDeletions = Futures.transformAsync(
                toRelations,
                relations -> {
                    // 执行具体的删除逻辑
                    List<ListenableFuture<Boolean>> results = deleteRelationGroupAsync(relations);
                    return Futures.allAsList(results);
                },
                service);

        ListenableFuture<List<List<Boolean>>> deletionsFuture = Futures.allAsList(toDeletions);

        // 删除所有FromId为entityId的内容
        return Futures.transform(
                        Futures.transformAsync(
                            deletionsFuture,
                            (deletions) -> deleteFromRelationsAsync(entityId),
                            service),
                    result -> null,
                    service);
    }


    private ListenableFuture<List<EntityRelationVO>> findAllByToId(EntityId entityId, RelationTypeGroup typeGroup) {
        return service.submit(() -> findByTo(entityId, typeGroup));
    }

    public List<EntityRelationVO> findByTo(EntityId entityId, RelationTypeGroup typeGroup){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("to_id", UUIDHelper.fromTimeUUID(entityId.getUuid()));
        queryWrapper.eq("to_type", entityId.getEntityType().toString());
        queryWrapper.eq("relation_type_group", typeGroup.toString());

        List<RuleNodeRelationPO> toPOs = relationMapper.selectList(queryWrapper);
        if(toPOs != null && toPOs.size() > 0){
            List<EntityRelationVO> result =
                    toPOs.stream().map(po -> EntityRelationVO.poToVo(po)).collect(Collectors.toList());

            return result;
        }
        return Lists.newArrayList();
    }

    private List<ListenableFuture<Boolean>> deleteRelationGroupAsync(List<List<EntityRelationVO>> relations) {
        List<ListenableFuture<Boolean>> results = Lists.newArrayList();
        for(List<EntityRelationVO> relationList : relations){
            relationList.forEach(relation -> results.add(deleteAsync(relation)));
        }
        return results;
    }

    private ListenableFuture<Boolean> deleteAsync(EntityRelationVO relation) {
        return service.submit(() -> deleteRelation(relation));
    }



    private ListenableFuture<Boolean> deleteFromRelationsAsync(EntityId entityId) {
        return service.submit(() -> {
            boolean relationExists = false;
            List<EntityRelationVO> fromEntites = findAllByFromIdAndFromType(entityId);
            if(fromEntites != null && fromEntites.size() > 0){
                // 具体的删除逻辑
                relationExists = deleteAllByFromIdAndFromType(entityId);
            }

            return relationExists;
        });
    }

    private List<EntityRelationVO> findAllByFromIdAndFromType(EntityId entityId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("from_id", UUIDHelper.fromTimeUUID(entityId.getUuid()));
        queryWrapper.eq("from_type", entityId.getEntityType().toString());

        List<RuleNodeRelationPO> fromPOs = relationMapper.selectList(queryWrapper);
        if(fromPOs != null && fromPOs.size() > 0){
            List<EntityRelationVO> result =
                    fromPOs.stream().map(po -> EntityRelationVO.poToVo(po)).collect(Collectors.toList());

            return result;
        }
        return Lists.newArrayList();
    }


    private boolean deleteAllByFromIdAndFromType(EntityId entityId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("from_id", UUIDHelper.fromTimeUUID(entityId.getUuid()));
        queryWrapper.eq("from_type", entityId.getEntityType().toString());

        relationMapper.delete(queryWrapper);
        return true;
    }


}
