package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.impls;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ListenableFuture;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsgMetaData;
import com.ruyuan.jiangzh.iot.base.uuid.EntityId;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsInvokeService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsScriptType;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.RuleNodeScriptFactory;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.RuleScriptEngine;

import javax.script.ScriptException;
import java.util.Map;
import java.util.UUID;

public class RuleNodeJsScriptEngine implements RuleScriptEngine {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final JsInvokeService jsInvokeService;

    private final UUID scriptId;

    private final EntityId entityId;

    public RuleNodeJsScriptEngine(
            JsInvokeService jsInvokeService, EntityId entityId, String script, String... argNames) {
        this.jsInvokeService = jsInvokeService;
        this.entityId = entityId;
        ListenableFuture<UUID> scriptIdFurture = this.jsInvokeService.eval(JsScriptType.RULE_NODE_SCRIPT, script, argNames);
        try {
            this.scriptId = scriptIdFurture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
        function test(){
            return true;
        }
     */
    @Override
    public boolean executeFilter(IoTMsg msg) throws ScriptException {
        JsonNode result = executeScript(msg);
        // 如果不是true or false的类型，就是类型不匹配
        if(!result.isBoolean()){
            throw new ScriptException("msg type not match filter");
        }

        return result.asBoolean();
    }

    @Override
    public IoTMsg executeUpdate(IoTMsg ioTMsg) throws ScriptException {
        JsonNode result = executeScript(ioTMsg);
        if(!result.isObject()){
            throw new ScriptException("msg type not match update");
        }
        return transformMsg(result, ioTMsg);
    }

    // 按照预定义的Script脚本，构建一个新的IoTMsg
    /*
        msg
        meta
        msgType
     */
    private IoTMsg transformMsg(JsonNode msgData, IoTMsg msg) {
        try {
            String data = null;
            Map<String,String> metadata = null;
            String msgType = null;

            // 获取data数据
            if(msgData.has(RuleNodeScriptFactory.MSG)){
                JsonNode msgPayload = msgData.get(RuleNodeScriptFactory.MSG);
                data = objectMapper.writeValueAsString(msgPayload);
            }

            // 获取metaData
            if(msgData.has(RuleNodeScriptFactory.METADATA)){
                JsonNode msgMetaData = msgData.get(RuleNodeScriptFactory.METADATA);
                metadata  = objectMapper.convertValue(msgMetaData, new TypeReference<Map<String, String>>() {});
            }

            if(msgData.has(RuleNodeScriptFactory.MSG_TYPE)){
                msgType = msgData.get(RuleNodeScriptFactory.MSG_TYPE).asText();
            }

            String newData = data != null ? data : msg.getData();
            IoTMsgMetaData newMetadata = metadata != null ? new IoTMsgMetaData(metadata) : msg.getMetaData().copy();
            String newMsgType = msgType != null ? msgType : msg.getType();

            // 构建一个新的IoTMsg
            return new IoTMsg(
                    msg.getId(), newMsgType, msg.getOriginator(),
                    newData, newMetadata, msg.getRuleChainId(), msg.getRuleNodeId());
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Fail to invoke transformMsg", e);
        }
    }


    private JsonNode executeScript(IoTMsg msg) throws ScriptException {
        try {
            // 组织参数 -> msg, metadata, msgType
            String[] args = prepareArgs(msg);
            // 执行脚本
            ListenableFuture<Object> invokeResultFurture = jsInvokeService.invokeFunction(this.scriptId, args[0], args[1], args[2]);
            // 获取脚本结果
            String invokeResult = invokeResultFurture.get() + "";
            return objectMapper.readTree(invokeResult);
        } catch (Exception e){
            throw new ScriptException(e);
        }
    }

    // 函数入参的准备，msg， metaData， msgType
    private String[] prepareArgs(IoTMsg msg) {
        try {
            String[] args = new String[3];
            // msg
            if(IoTStringUtils.isNotBlank(msg.getData())){
                args[0] = msg.getData();
            }else{
                args[0] = "";
            }
            // metadata
            args[1] = objectMapper.writeValueAsString(msg.getMetaData().getData());
            // msgType
            args[2] = msg.getType();
            return args;
        } catch (Exception e) {
            throw new IllegalArgumentException("msg args is wrong !");
        }
    }


    @Override
    public void destroy() {
        if(this.scriptId != null){
            jsInvokeService.release(this.scriptId);
        }
    }
}
