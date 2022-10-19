package com.ruyuan.jiangzh.protol.infrastructure.protocol.common;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class FutureCallbackUtils {

    private static ExecutorService defaultExecutorService = Executors.newSingleThreadExecutor();

    public static <T> void withCallback(
            ListenableFuture<T> future, Consumer<T> onSuccess, Consumer<Throwable> onFailure,
            Executor executor){

        FutureCallback<T> callback = new FutureCallback<T>() {
            @Override
            public void onSuccess(@Nullable T result) {
                try {
                    onSuccess.accept(result);
                }catch (Throwable t){
                    onFailure(t);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                onFailure.accept(t);
            }
        };

        if(executor != null){
            Futures.addCallback(future, callback, executor);
        }else{
            Futures.addCallback(future, callback, defaultExecutorService);
        }

    }

}
