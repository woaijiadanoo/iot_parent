package com.ruyuan.jiangzh.iot;

import com.ruyuan.jiangzh.iot.servicelocator.ServiceLocator;
import com.ruyuan.jiangzh.iot.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations = {"classpath:applicationContext-dubbo.xml"})
@SpringBootApplication
public class IotUserApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(IotUserApplication.class, args);

        // 初始化ServiceLocator
        ServiceLocator serviceLocator = ServiceLocator.initServiceLocator(context);

        UserService userService = (UserService)serviceLocator.getCtx().getBean("userService");

        userService.showXml();

//        userService.insert();

//        userService.query();

//        userService.update();

//        userService.selectCondition();

//        userService.delete();

//        userService.selectByPage();

    }

}
