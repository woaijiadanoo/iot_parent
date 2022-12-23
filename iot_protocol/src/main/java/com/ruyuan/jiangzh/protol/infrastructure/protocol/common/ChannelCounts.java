package com.ruyuan.jiangzh.protol.infrastructure.protocol.common;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ChannelCounts {

    private static AtomicInteger channelConnections = new AtomicInteger();

    public ChannelCounts(){
        // 间隔两秒打印一次当前的连接数
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("channelConnections = " + channelConnections.get());
        }, 0, 2, TimeUnit.SECONDS);
    }

    // 每新增一条连接增加一次
    public static void newChannel(){
        channelConnections.incrementAndGet();
    }

}
