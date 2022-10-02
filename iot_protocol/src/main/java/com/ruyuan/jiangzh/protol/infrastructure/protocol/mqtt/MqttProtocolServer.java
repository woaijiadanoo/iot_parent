package com.ruyuan.jiangzh.protol.infrastructure.protocol.mqtt;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author jiangzheng
 * @version 1.0
 * @description:
 */
@Service
public class MqttProtocolServer {

    @Autowired
    private MqttProtocolContext context;

    @Value("${protocol.mqtt.bind_address}")
    private String host;
    @Value("${protocol.mqtt.bind_port}")
    private Integer port;

    @Value("${protocol.mqtt.boss_group_thread_count}")
    private Integer bossGroupThreadCount;

    @Value("${protocol.mqtt.worker_group_thread_count}")
    private Integer workerGroupThreadCount;

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @PostConstruct
    public void init() throws Exception{
        System.out.println("===============> mqtt server start");
        bossGroup = new NioEventLoopGroup(bossGroupThreadCount);
        workerGroup = new NioEventLoopGroup(workerGroupThreadCount);

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MqttProtoolServiceInitializer(context));

        serverChannel = b.bind(host, port).sync().channel();

        System.out.println("===============> mqtt server start finish");
    }

    @PreDestroy
    public void shutdown() throws Exception{
        try {
            serverChannel.close().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

}
