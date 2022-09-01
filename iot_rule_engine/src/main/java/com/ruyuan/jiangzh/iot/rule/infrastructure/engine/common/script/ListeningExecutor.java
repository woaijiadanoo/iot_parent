package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public interface ListeningExecutor extends Executor {

    // 异步任务提交处理
    <T> ListenableFuture<T> executeAsync(Callable<T> task);

}
