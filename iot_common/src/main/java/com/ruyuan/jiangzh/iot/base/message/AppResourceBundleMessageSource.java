package com.ruyuan.jiangzh.iot.base.message;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 *  国际化的实现对象
 */
public class AppResourceBundleMessageSource extends ResourceBundleMessageSource {

    /**
     *  国际化语言信息的配置文件路径,  默认是classpath下
     *  例子： messages/message
     */
    private String filePath;

    /**
     *  国家简写
     */
    private String country;

    /**
     *  语言
     */
    private String language;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.setBasename(filePath);
        this.filePath = filePath;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
