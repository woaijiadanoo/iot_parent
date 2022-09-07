package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.extension.transform;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.ruyuan.jiangzh.iot.actors.msg.IoTMsg;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNode;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNodeConfiguration;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineNodeUtils;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.RuleEngineRelationTypes;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.RuleEngineContext;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.RuleScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class ExtensionTransformMsgNode implements RuleEngineNode {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ExtensionTransformMsgNodeConfiguration configuration;
    private RuleScriptEngine jsEngine;

    @Override
    public void init(RuleEngineContext ctx, RuleEngineNodeConfiguration configuration) {
        this.configuration = RuleEngineNodeUtils.convert(configuration, ExtensionTransformMsgNodeConfiguration.class);
        this.jsEngine = ctx.createJsScriptEngine(this.configuration.getJsScript());
    }

    @Override
    public void onMsg(RuleEngineContext ctx, IoTMsg msg) {
        logger.info("js transform data : [{}]", msg.getData());
        withCallback(transform(ctx, msg),
                m -> {
                    if (m != null) {
                        ctx.tellNext(m, RuleEngineRelationTypes.SUCCESS);
                    } else {
                        ctx.tellNext(msg, RuleEngineRelationTypes.FAILURE);
                    }
                },
                t -> ctx.tellFailure(msg, t), ctx.getJsExecutor()
        );
    }

    private ListenableFuture<IoTMsg> transform(RuleEngineContext ctx, IoTMsg msg) {
        return ctx.getJsExecutor().executeAsync(() -> jsEngine.executeUpdate(msg));
    }

    public static <T> void withCallback(
            ListenableFuture<T> future, Consumer<T> onSuccess, Consumer<Throwable> onFailure, Executor executor) {
        FutureCallback<T> callback = new FutureCallback<T>() {
            @Override
            public void onSuccess(@Nullable T result) {
                try {
                    onSuccess.accept(result);
                } catch (Throwable th) {
                    onFailure(th);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                onFailure.accept(throwable);
            }
        };

        if (executor != null) {
            // 执行具体的脚本内容
            Futures.addCallback(future, callback, executor);
        }
    }

    @Override
    public void destory() {
        if (jsEngine != null) {
            jsEngine.destroy();
        }
    }
}
