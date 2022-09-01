package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script;

import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;

import javax.script.ScriptException;

public interface RuleScriptEngine {

    // 执行Filter类型的脚本Node
    boolean executeFilter(IoTMsg ioTMsg) throws ScriptException;

    // 释放资源
    void destroy();

}
