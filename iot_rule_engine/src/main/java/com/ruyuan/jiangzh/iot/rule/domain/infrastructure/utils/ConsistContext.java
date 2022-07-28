package com.ruyuan.jiangzh.iot.rule.domain.infrastructure.utils;

public interface ConsistContext {

    public static final String RULE_CHAIN_QUERY_PARAM_NAME="ruleChainName";
    public static final String TENANT_ID="tenantId";

    // msgId
    public static final String RULE_CHAIN_NAME_EXISTS="rule.rule_chain_name_exists";
    public static final String RULE_CHAIN_ID_EMPTY="rule.rule_chain_id_empty";
    public static final String RESOURCE_NOT_EXISTS="default.resource_not_exists";

}
