package com.ruyuan.jiangzh.iot.base.message;

import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.servicelocator.ServiceLocator;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageHelper {

    private static Map<String,String> messageSourceMap = new HashMap<>();
    /**
     *  国际化信息的对象
     */
    private static AppResourceBundleMessageSource arms = null;

    private MessageHelper(){}

    public static AppResourceBundleMessageSource getARMS(){
        if(arms == null){
            arms = (AppResourceBundleMessageSource)ServiceLocator.getInstance().getCtx().getBean("messageSource");
        }
        return arms;
    }

    /**
     *  无参数的获取国际化信息
     * @param msgId
     * @return
     */
    public static String getMessage(String msgId){
        String msg = "";

        // TODO 【init】 把对应语言信息的配置文件读取，然后写入到messageSourceMap
        if(messageSourceMap != null && messageSourceMap.size() > 0){
            // 就先去messageSourceMap查一下有没有
        }

        // 如果msgId没有对应的消息
        try {
            AppResourceBundleMessageSource arms = getARMS();
            msg = ServiceLocator.getInstance().getCtx().getMessage(msgId, null, new Locale(arms.getLanguage(), arms.getCountry()));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        // 如果实在没有消息，就直接返回msgId
       if(IoTStringUtils.isBlank(msg)){
           msg = msgId;
       }

        return msg;
    }

    /**
     *  有参数的获取国际化信息
     * @param msgId
     * @return
     */
    public static String getMessage(String msgId,Object[] params){
        String msg = "";

        // TODO 【init】 把对应语言信息的配置文件读取，然后写入到messageSourceMap
        if(messageSourceMap != null && messageSourceMap.size() > 0){
            // 就先去messageSourceMap查一下有没有
        }

        // 如果msgId没有对应的消息
        try {
            AppResourceBundleMessageSource arms = getARMS();
            msg = ServiceLocator
                    .getInstance()
                    .getCtx()
                    .getMessage(msgId, params, new Locale(arms.getLanguage(), arms.getCountry()));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        // 如果实在没有消息，就直接返回msgId
        if(IoTStringUtils.isBlank(msg)){
            msg = msgId;
        }

        return msg;
    }

}
