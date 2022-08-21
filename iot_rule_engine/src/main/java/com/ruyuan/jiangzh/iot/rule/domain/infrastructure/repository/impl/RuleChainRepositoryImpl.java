package com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.rule.domain.entity.RuleChainEntity;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.RuleChainRepository;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.impl.mapper.RuleChainMapper;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.po.RuleChainPO;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.utils.ConsistContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RuleChainRepositoryImpl implements RuleChainRepository {

    @Resource
    private RuleChainMapper ruleChainMapper;

    @Override
    public RuleChainEntity saveRuleChain(RuleChainEntity entity) {
        boolean newRuleChain = entity.getId() == null ? true : false;
        if(newRuleChain){
            if(sameName(entity.getTenantId(), entity.getRuleChainName())){
                throw new AppException(409, ConsistContext.RULE_CHAIN_NAME_EXISTS);
            }

            entity.setId(new RuleChainId(UUIDHelper.genUuid()));

            RuleChainPO ruleChainPO = RuleChainEntity.entityToPo(entity);
            ruleChainMapper.insert(ruleChainPO);
        }else{
            // TODO 需要按照ruleChainId来判断名字有没有修改
                // 如果名字修改了，要判断名字修改的是否合法

            RuleChainPO ruleChainPO = RuleChainEntity.entityToPo(entity);
            ruleChainMapper.updateById(ruleChainPO);
        }
        return entity;
    }

    private boolean sameName(TenantId tenantId, String ruleChainName){
        QueryWrapper<RuleChainPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", UUIDHelper.fromTimeUUID(tenantId.getUuid()));
        queryWrapper.eq("rule_chain_name", ruleChainName);

        Integer ruleChainNameCounts = ruleChainMapper.selectCount(queryWrapper);

        return ruleChainNameCounts > 0;
    }

    @Override
    public PageDTO<RuleChainEntity> ruleChains(PageDTO<RuleChainEntity> pageDTO) {
        // 组织mybatis-plus的两个核心对象 ipage , queryWrapper
        IPage<RuleChainPO> iPage = new Page<>(pageDTO.getNowPage(),pageDTO.getPageSize());
        QueryWrapper<RuleChainPO> queryWrapper = null;
        if(pageDTO.getConditions().size() > 0){
            queryWrapper = new QueryWrapper<>();
            Set<String> keys = pageDTO.getConditions().keySet();
            for(String key : keys){
                // 拼接查询条件
                spellCondition(queryWrapper, key, pageDTO.getConditions().get(key));
            }
        }

        IPage<RuleChainPO> page = ruleChainMapper.selectPage(iPage, queryWrapper);
        List<RuleChainPO> records = page.getRecords();
        // 将PO转换为领域实体
        List<RuleChainEntity> entities =
                records.stream().map(po -> new RuleChainEntity(po)).collect(Collectors.toList());
        // 封装返回对象
        pageDTO.setResult(page.getTotal(), page.getPages(), entities);

        return pageDTO;
    }

    private void spellCondition(QueryWrapper<RuleChainPO> queryWrapper, String fieldName, Object fieldValue) {
        if(ConsistContext.RULE_CHAIN_QUERY_PARAM_NAME.equalsIgnoreCase(fieldName)){
            queryWrapper.eq("rule_chain_name", fieldValue);
        } else if(ConsistContext.TENANT_ID.equalsIgnoreCase(fieldName)){
            queryWrapper.eq("tenant_id", fieldValue);
        }
    }

    @Override
    public RuleChainEntity findRuleChainById(TenantId tenantId, RuleChainId ruleChainId) {
        String tenantDataId = UUIDHelper.fromTimeUUID(tenantId.getUuid());
        String ruleChainDataId = UUIDHelper.fromTimeUUID(ruleChainId.getUuid());

        RuleChainPO ruleChainPO = ruleChainMapper.findRuleChainById(tenantDataId, ruleChainDataId);

        return new RuleChainEntity(ruleChainPO);
    }

    @Override
    public boolean deleteRuleChainById(TenantId tenantId, RuleChainId ruleChainId) {
        QueryWrapper<RuleChainPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", UUIDHelper.fromTimeUUID(tenantId.getUuid()));
        queryWrapper.eq("uuid", UUIDHelper.fromTimeUUID(ruleChainId.getUuid()));

        int delete = ruleChainMapper.delete(queryWrapper);

        return delete >= 0;
    }

    @Override
    public List<RuleChainEntity> findRuleChains(TenantId tenantId) {
        QueryWrapper<RuleChainPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", UUIDHelper.fromTimeUUID(tenantId.getUuid()));

        List<RuleChainPO> poList = ruleChainMapper.selectList(queryWrapper);

        List<RuleChainEntity> entities = poList.stream().map(po -> new RuleChainEntity(po)).collect(Collectors.toList());

        return entities;
    }
}
