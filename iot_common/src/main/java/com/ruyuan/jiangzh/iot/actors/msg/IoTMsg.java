package com.ruyuan.jiangzh.iot.actors.msg;

import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;

import java.io.Serializable;
import java.util.UUID;

public class IoTMsg implements Serializable {

    private final UUID id;
    private final String type;
    private final EntityId originator;

    private final String data;

    private final IoTMsgMetaData metaData;

    private final RuleChainId ruleChainId;
    private final RuleNodeId ruleNodeId;

    public IoTMsg(UUID id, String type, EntityId originator, String data, IoTMsgMetaData metaData,
                  RuleChainId ruleChainId, RuleNodeId ruleNodeId) {
        this.id = id;
        this.type = type;
        this.originator = originator;
        this.data = data;
        this.metaData = metaData;
        this.ruleChainId = ruleChainId;
        this.ruleNodeId = ruleNodeId;
    }

    /*
        拷贝对象
     */
    public IoTMsg copy(UUID newId,RuleChainId ruleChainId,RuleNodeId ruleNodeId){
        return new IoTMsg(newId, getType(), getOriginator(), getData(),metaData.copy(), ruleChainId, ruleNodeId);
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public EntityId getOriginator() {
        return originator;
    }

    public String getData() {
        return data;
    }

    public RuleChainId getRuleChainId() {
        return ruleChainId;
    }

    public RuleNodeId getRuleNodeId() {
        return ruleNodeId;
    }

    public IoTMsgMetaData getMetaData() {
        return metaData;
    }
}
