package com.ruyuan.jiangzh.iot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@MapperScan(basePackages = {"com.ruyuan.jiangzh.iot.rule.domain.infrastructure.repository.impl.mapper"})
@ImportResource(locations = {"classpath:applicationContext-dubbo.xml"})
@SpringBootApplication
public class IotRuleEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotRuleEngineApplication.class, args);
    }

}
