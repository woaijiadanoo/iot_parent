package com.ruyuan.jiangzh.protol.infrastructure.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
public class ProtocolContext {

    protected final ObjectMapper mapper = new ObjectMapper();

    private ExecutorService executor;

    @PostConstruct
    public void init(){
        executor = Executors.newCachedThreadPool();
    }

    @PreDestroy
    public void stop(){
        if(executor != null){
            executor.shutdownNow();
        }
    }


    public ExecutorService getExecutor() {
        return executor;
    }
}
