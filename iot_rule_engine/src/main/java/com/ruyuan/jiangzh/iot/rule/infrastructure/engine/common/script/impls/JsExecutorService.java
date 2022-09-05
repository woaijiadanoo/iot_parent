package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.impls;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.ListeningExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class JsExecutorService implements ListeningExecutor, Executor {

    private ListeningExecutorService service;

    @Value("${js.executor.threadpools}")
    private int jsExecutorThreadPoolSize;


    @Override
    public <T> ListenableFuture<T> executeAsync(Callable<T> task) {
        return service.submit(task);
    }

    @Override
    public void execute(Runnable command) {
        service.execute(command);
    }

    public ListeningExecutorService executor(){
        return service;
    }


    @PostConstruct
    public void init(){
        this.service = MoreExecutors.listeningDecorator(Executors.newWorkStealingPool(getJsExecutorThreadPoolSize()));
    }

    @PreDestroy
    public void destroy(){
        if(this.service != null){
            this.service.shutdown();
        }
    }


    public int getJsExecutorThreadPoolSize() {
        return jsExecutorThreadPoolSize;
    }

    public void setJsExecutorThreadPoolSize(int jsExecutorThreadPoolSize) {
        this.jsExecutorThreadPoolSize = jsExecutorThreadPoolSize;
    }
}
