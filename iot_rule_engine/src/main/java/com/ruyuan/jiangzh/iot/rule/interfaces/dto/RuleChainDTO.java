package com.ruyuan.jiangzh.iot.rule.interfaces.dto;

import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.domain.entity.RuleChainEntity;

public class RuleChainDTO {

    private String ruleChainId;
    private String tenantId;
    private String userId;
    private String ruleChainName;
    private String firstRuleNodeId;

    public static RuleChainEntity dtoToEntity(RuleChainDTO dto, TenantId tenantId, UserId userId){
        RuleChainEntity ruleChainEntity = null;
        if(IoTStringUtils.isNotBlank(dto.getRuleChainId())){
            ruleChainEntity = new RuleChainEntity(new RuleChainId(IoTStringUtils.toUUID(dto.getRuleChainId())));
        }else{
            ruleChainEntity = new RuleChainEntity();
        }

        if(IoTStringUtils.isNotBlank(dto.getFirstRuleNodeId())){
            ruleChainEntity.setFirstRuleNodeId(new RuleNodeId(IoTStringUtils.toUUID(dto.getFirstRuleNodeId())));
        }

        ruleChainEntity.setRuleChainName(dto.ruleChainName);

        ruleChainEntity.setTenantId(tenantId);
        ruleChainEntity.setUserId(userId);

        return ruleChainEntity;
    }

    public static RuleChainDTO entityToDto(RuleChainEntity entity){
        RuleChainDTO dto = new RuleChainDTO();

        dto.setRuleChainId(entity.getId().toString());
        dto.setTenantId(entity.getTenantId().toString());
        dto.setUserId(entity.getUserId().toString());
        dto.setRuleChainName(entity.getRuleChainName());
        if(entity.getFirstRuleNodeId() != null){
            dto.setFirstRuleNodeId(entity.getFirstRuleNodeId().toString());
        }

        return dto;
    }


    public String getRuleChainId() {
        return ruleChainId;
    }

    public void setRuleChainId(String ruleChainId) {
        this.ruleChainId = ruleChainId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRuleChainName() {
        return ruleChainName;
    }

    public void setRuleChainName(String ruleChainName) {
        this.ruleChainName = ruleChainName;
    }

    public String getFirstRuleNodeId() {
        return firstRuleNodeId;
    }

    public void setFirstRuleNodeId(String firstRuleNodeId) {
        this.firstRuleNodeId = firstRuleNodeId;
    }

    @Override
    public String toString() {
        return "RuleChainDTO{" +
                "ruleChainId='" + ruleChainId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", userId='" + userId + '\'' +
                ", ruleChainName='" + ruleChainName + '\'' +
                ", firstRuleNodeId='" + firstRuleNodeId + '\'' +
                '}';
    }
}
