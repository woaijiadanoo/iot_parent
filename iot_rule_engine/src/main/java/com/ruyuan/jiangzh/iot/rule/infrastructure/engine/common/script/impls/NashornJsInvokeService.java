package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.impls;

import com.google.common.util.concurrent.ListenableFuture;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsInvokeService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsScriptType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NashornJsInvokeService implements JsInvokeService {
    @Override
    public ListenableFuture<UUID> eval(JsScriptType scriptType, String scriptBody, String... argNames) {
        return null;
    }

    @Override
    public ListenableFuture<Object> invokeFunction(UUID scriptId, Object... args) {
        return null;
    }

    @Override
    public ListenableFuture<Void> release(UUID scriptId) {
        return null;
    }
}
