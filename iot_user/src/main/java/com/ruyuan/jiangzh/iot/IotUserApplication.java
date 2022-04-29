package com.ruyuan.jiangzh.iot;

import com.ruyuan.jiangzh.iot.servicelocator.ServiceLocator;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@MapperScan(value = "com.ruyuan.jiangzh.iot.user.domain.infrastructure.repository.impl.mapper")
@ImportResource(locations = {"classpath:applicationContext-dubbo.xml"})
@SpringBootApplication
public class IotUserApplication {

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext context = SpringApplication.run(IotUserApplication.class, args);
            ServiceLocator.initServiceLocator(context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
