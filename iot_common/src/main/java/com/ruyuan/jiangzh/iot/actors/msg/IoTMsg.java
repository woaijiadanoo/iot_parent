package com.ruyuan.jiangzh.iot.actors.msg;

import com.google.common.collect.Maps;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleChainId;
import com.ruyuan.jiangzh.iot.base.uuid.rule.RuleNodeId;

import java.io.Serializable;
import java.util.UUID;

public class IoTMsg implements Serializable {

    private final UUID id;
    private final String type;
    private final EntityId originator;

    /*
        元数据
        ry_device_01 元数据
            tenantId
            deviceId
            deviceName
            productId

        ry_device_01 上报的数据
        {
            "tempature" :  45
        }
     */
    private final String data;

    // ry_device_01 元数据
    private final IoTMsgMetaData metaData;

    private final RuleChainId ruleChainId;
    private final RuleNodeId ruleNodeId;

    public IoTMsg(UUID id, String type, String data){
        this(id,type, null ,data, new IoTMsgMetaData(Maps.newHashMap()), null, null);
    }

    public IoTMsg(UUID id, String type, String data, IoTMsgMetaData metaData){
        this(id,type, null ,data, metaData, null, null);
    }

    public IoTMsg(UUID id, String type, EntityId originator, String data,
                  RuleChainId ruleChainId, RuleNodeId ruleNodeId){
        this(id,type,originator,data, new IoTMsgMetaData(Maps.newHashMap()), ruleChainId, ruleNodeId);
    }

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
