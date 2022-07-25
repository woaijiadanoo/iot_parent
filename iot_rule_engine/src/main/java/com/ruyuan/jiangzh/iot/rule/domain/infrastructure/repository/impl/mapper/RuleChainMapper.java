package com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.impl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.po.RuleChainPO;
import org.apache.ibatis.annotations.Param;

public interface RuleChainMapper extends BaseMapper<RuleChainPO> {

    RuleChainPO findRuleChainById(@Param("tenantId") String tenantId,@Param("ruleChainId") String ruleChainId);

}
