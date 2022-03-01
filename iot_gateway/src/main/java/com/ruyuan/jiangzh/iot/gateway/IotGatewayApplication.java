package com.ruyuan.jiangzh.iot.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations = {"classpath:applicationContext-dubbo.xml"})
@SpringBootApplication
public class IotGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotGatewayApplication.class, args);
    }

}
