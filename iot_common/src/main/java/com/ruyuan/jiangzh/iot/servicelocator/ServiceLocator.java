package com.ruyuan.jiangzh.iot.servicelocator;

import org.springframework.context.ApplicationContext;

public class ServiceLocator {

    private ServiceLocator(){}

    // 饥汉模式
    private static ServiceLocator serviceLocator = null;

    /**
     *  Spring的应用上下文
     */
    private static ApplicationContext ctx = null;

    /**
     *  初始化serviceLocator， 需要应用注册ApplicationContext
     * @param ctx
     * @return
     */
    public static ServiceLocator initServiceLocator(ApplicationContext ctx){
        if(serviceLocator == null || serviceLocator.getCtx() == null){
            serviceLocator = new ServiceLocator();
            serviceLocator.setCtx(ctx);
        }

        return serviceLocator;
    }

    private void setCtx(ApplicationContext ctx) {
        ServiceLocator.ctx = ctx;
    }

    public ApplicationContext getCtx(){
        return ctx;
    }

}
