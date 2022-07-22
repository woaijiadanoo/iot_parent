package com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;


@TableName(value = "rule_chain")
public class RuleChainPO extends Model<RuleChainPO> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String uuid;

    private String tenantId;

    private String userId;

    private String ruleChainName;

    private String firstRuleNodeId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "RuleChain{" +
        ", uuid=" + uuid +
        ", tenantId=" + tenantId +
        ", userId=" + userId +
        ", ruleChainName=" + ruleChainName +
        ", firstRuleNodeId=" + firstRuleNodeId +
        "}";
    }
}
