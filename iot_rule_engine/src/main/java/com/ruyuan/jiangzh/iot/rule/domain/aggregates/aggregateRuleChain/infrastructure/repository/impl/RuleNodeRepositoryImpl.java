package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.impl;

import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleNodeEntity;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRepository;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.impl.mapper.RuleNodeMapper;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.po.RuleNodePO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class RuleNodeRepositoryImpl implements RuleNodeRepository {

    @Resource
    private RuleNodeMapper ruleNodeMapper;

    @Override
    public RuleNodeEntity findById(RuleNodeId ruleNodeId) {
        RuleNodePO ruleNodePO = ruleNodeMapper.selectById(transToDataId(ruleNodeId.getUuid()));
        return RuleNodeEntity.poToEntity(ruleNodePO);
    }

    @Override
    public RuleNodeEntity saveNode(RuleNodeEntity ruleNode) {
        boolean insert = ruleNode.getId() == null;
        if(insert){
            // 新增
            ruleNode.setId(new RuleNodeId(UUIDHelper.genUuid()));
            RuleNodePO ruleNodePO = ruleNode.entityToPo();
            ruleNodeMapper.insert(ruleNodePO);
        }else{
            // 修改
            RuleNodePO ruleNodePO = ruleNode.entityToPo();
            ruleNodeMapper.updateById(ruleNodePO);
        }
        return ruleNode;
    }

    @Override
    public void removeById(UUID uuid) {
        ruleNodeMapper.deleteById(transToDataId(uuid));
    }

    /*
        UUID 转 dataId
     */
    private String transToDataId(UUID uuid){
        return UUIDHelper.fromTimeUUID(uuid);
    }

}
