package com.ruyuan.jiangzh.protol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations = {"classpath:applicationContext-dubbo.xml"})
@SpringBootApplication
public class IotProtocolApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotProtocolApplication.class, args);
    }

}
