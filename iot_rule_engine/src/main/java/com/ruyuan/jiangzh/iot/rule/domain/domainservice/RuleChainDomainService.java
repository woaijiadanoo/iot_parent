package com.ruyuan.jiangzh.iot.rule.domain.domainservice;

import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleChainMetaDataEntity;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.factory.AggrRuleChainMetaDataFactory;
import com.ruyuan.jiangzh.iot.rule.domain.entity.RuleChainEntity;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.RuleChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuleChainDomainService {

    @Autowired
    private RuleChainRepository ruleChainRepository;

    @Autowired
    private AggrRuleChainMetaDataFactory ruleChainMetaDataFactory;

    public RuleChainMetaDataEntity saveRuleChainMetaData(TenantId tenantId, RuleChainMetaDataEntity metaDataEntity){
        RuleChainId ruleChainId = metaDataEntity.getId();

        // 验证RuleChain是否存在
        RuleChainEntity ruleChainEntity = ruleChainRepository.findRuleChainById(tenantId, ruleChainId);
        if (ruleChainEntity == null) {
            return null;
        }

        // 保存RuleChainMetaData
        RuleNodeId firstRuleNodeId = metaDataEntity.saveRuleChainMetaData();

        // 如果firstRoleNode出现变化，则应该更新RuleChainEntity
        if((ruleChainEntity.getFirstRuleNodeId() != null && !ruleChainEntity.getFirstRuleNodeId().equals(firstRuleNodeId))
                || (ruleChainEntity.getFirstRuleNodeId() == null && firstRuleNodeId != null)){
            ruleChainEntity.setFirstRuleNodeId(firstRuleNodeId);
            ruleChainRepository.saveRuleChain(ruleChainEntity);
        }


        // 获取最新的RuleChainMetaData，并返回
        RuleChainMetaDataEntity result = loadRuleChainMetaData(tenantId, ruleChainId);

        return result;
    }


    public RuleChainMetaDataEntity loadRuleChainMetaData(TenantId tenantId, RuleChainId ruleChainId){
        // 验证RuleChain是否存在
        RuleChainEntity ruleChainEntity = ruleChainRepository.findRuleChainById(tenantId, ruleChainId);
        if (ruleChainEntity == null) {
            return null;
        }

        RuleChainMetaDataEntity entity = ruleChainMetaDataFactory.emptyMetaDataEntity();
        entity.setId(ruleChainId);

        RuleNodeId firstRuleNodeId = null;
        if(ruleChainEntity.getFirstRuleNodeId() != null){
            firstRuleNodeId = ruleChainEntity.getFirstRuleNodeId();
        } else {
            // 如果firstRuleNodeId不存在，表明RuleChain里面既没有节点，又没有关系，只有一个RuleChainId，直接返回即可
            return entity;
        }

        // 获取数据的逻辑
        RuleChainMetaDataEntity metaDataEntity = entity.loadRuleChainMetaData(ruleChainId, firstRuleNodeId);

        return metaDataEntity;
    }

}
