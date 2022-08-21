package com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository;

import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.web.PageDTO;
import com.ruyuan.jiangzh.iot.rule.domain.entity.RuleChainEntity;

import java.util.List;

public interface RuleChainRepository {
    // 新增和修改
    RuleChainEntity saveRuleChain(RuleChainEntity entity);

    // 列表查询
    PageDTO<RuleChainEntity> ruleChains(PageDTO<RuleChainEntity> pageDTO);

    // 根据主键查询
    RuleChainEntity findRuleChainById(TenantId tenantId, RuleChainId ruleChainId);

    // 删除
    boolean deleteRuleChainById(TenantId tenantId, RuleChainId ruleChainId);

    List<RuleChainEntity> findRuleChains(TenantId tenantId);
}
