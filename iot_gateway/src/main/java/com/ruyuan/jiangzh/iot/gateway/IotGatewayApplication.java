package com.ruyuan.jiangzh.iot.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@ComponentScan(basePackages = {"com.ruyuan.jiangzh.iot"})
@SpringBootApplication
public class IotGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotGatewayApplication.class, args);
    }

}
