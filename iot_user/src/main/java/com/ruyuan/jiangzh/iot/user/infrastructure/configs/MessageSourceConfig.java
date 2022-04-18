package com.ruyuan.jiangzh.iot.user.infrastructure.configs;

import com.ruyuan.jiangzh.iot.base.message.AppResourceBundleMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSourceConfig {

    /**
     *  region
     *  优化点：【国家和语言通过外部传入】
     *      1、docker的本地变量获取country和language
     *      2、配置文件【不太推荐】
     * @return
     */
    @Bean
    public MessageSource messageSource(){
        AppResourceBundleMessageSource messageSource = new AppResourceBundleMessageSource();

        //初始化几个属性
        messageSource.setFilePath("messages/message");
        messageSource.setCountry("UK");
        messageSource.setLanguage("en");

        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

}
