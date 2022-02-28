package com.ruyuan.jiangzh.iot;

import com.ruyuan.jiangzh.iot.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class IotUserApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(IotUserApplication.class, args);

        UserService userService = (UserService)context.getBean("userService");

//        userService.insert();

//        userService.query();

//        userService.update();

//        userService.selectCondition();

//        userService.delete();

//        userService.selectByPage();

    }

}
