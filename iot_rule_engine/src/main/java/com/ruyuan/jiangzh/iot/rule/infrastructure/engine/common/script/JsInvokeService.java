package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;

public interface JsInvokeService {

    // 编译缓存
    ListenableFuture<UUID> eval(JsScriptType scriptType, String scriptBody, String... argNames);

    // 执行缓存脚本
    ListenableFuture<Object> invokeFunction(UUID scriptId, Object... args);

    // 释放缓存
    ListenableFuture<Void> release(UUID scriptId);

}
