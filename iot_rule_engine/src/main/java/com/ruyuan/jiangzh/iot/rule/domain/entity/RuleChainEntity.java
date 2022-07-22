package com.ruyuan.jiangzh.iot.rule.domain.entity;

import com.ruyuan.jiangzh.iot.base.exception.AppException;
import com.ruyuan.jiangzh.iot.base.uuid.CreateTimeIdBase;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.TenantId;
import com.ruyuan.jiangzh.iot.base.uuid.tenant.UserId;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.po.RuleChainPO;

import java.io.Serializable;

public class RuleChainEntity  extends CreateTimeIdBase<RuleChainId> implements Serializable {

    private static final String ID_EMPTY_MSG_ID = "rule.rule_id_is_empty";

    private TenantId tenantId;
    private UserId userId;

    private String ruleChainName;

    private RuleNodeId firstRuleNodeId;

    public RuleChainEntity(){}

    public RuleChainEntity(RuleChainId ruleChainId){
        super(ruleChainId);
    }

    public RuleChainEntity(RuleChainPO po){
        super(new RuleChainId(UUIDHelper.fromStringId(po.getUuid())));
        this.tenantId = new TenantId(UUIDHelper.fromStringId(po.getTenantId()));
        this.userId = new UserId(UUIDHelper.fromStringId(po.getUserId()));
        this.ruleChainName = po.getRuleChainName();
        if(IoTStringUtils.isNotBlank(po.getFirstRuleNodeId())){
            this.firstRuleNodeId = new RuleNodeId(UUIDHelper.fromStringId(po.getFirstRuleNodeId()));
        }
    }

    public static RuleChainPO entityToPo(RuleChainEntity entity){
        RuleChainPO po = new RuleChainPO();
        if(entity.getId() == null){
            throw new AppException(500, ID_EMPTY_MSG_ID);
        }
        po.setUuid(UUIDHelper.fromTimeUUID(entity.getId().getUuid()));
        po.setTenantId(UUIDHelper.fromTimeUUID(entity.getTenantId().getUuid()));
        po.setUserId(UUIDHelper.fromTimeUUID(entity.getUserId().getUuid()));
        po.setRuleChainName(entity.ruleChainName);

        if(entity.getFirstRuleNodeId() != null){
            po.setFirstRuleNodeId(UUIDHelper.fromTimeUUID(entity.getFirstRuleNodeId().getUuid()));
        }

        return po;
    }


    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getRuleChainName() {
        return ruleChainName;
    }

    public void setRuleChainName(String ruleChainName) {
        this.ruleChainName = ruleChainName;
    }

    public RuleNodeId getFirstRuleNodeId() {
        return firstRuleNodeId;
    }

    public void setFirstRuleNodeId(RuleNodeId firstRuleNodeId) {
        this.firstRuleNodeId = firstRuleNodeId;
    }

    @Override
    public String toString() {
        return "RuleChainEntity{" +
                "tenantId=" + tenantId +
                ", userId=" + userId +
                ", ruleChainName='" + ruleChainName + '\'' +
                ", firstRuleNodeId=" + firstRuleNodeId +
                '}';
    }
}
