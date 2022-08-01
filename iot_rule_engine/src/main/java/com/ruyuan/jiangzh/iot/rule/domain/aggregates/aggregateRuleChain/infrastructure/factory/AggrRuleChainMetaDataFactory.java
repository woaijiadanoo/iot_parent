package com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.factory;

import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.entity.RuleChainMetaDataEntity;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRelationRepository;
import com.ruyuan.jiangzh.iot.rule.domain.aggregates.aggregateRuleChain.infrastructure.repository.RuleNodeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AggrRuleChainMetaDataFactory {

    @Resource
    private RuleNodeRepository ruleNodeRepository;

    @Resource
    private RuleNodeRelationRepository ruleNodeRelationRepository;

    /*
        获取一个空的metaData
     */
    public RuleChainMetaDataEntity emptyMetaDataEntity(){
        RuleChainMetaDataEntity dataEntity = new RuleChainMetaDataEntity(ruleNodeRepository,ruleNodeRelationRepository);

        return dataEntity;
    }


}
