package com.ruyuan.jiangzh.iot.common;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: theadlocal帮助工具
 */
public class ThreadLocalUtils {

    private static ThreadLocal<String> threadLocal = new ThreadLocal();

    // 初始化threadlocal
    public static void init(String requestId){
        threadLocal.set(requestId);
    }

    // 获取threadlocal
    public static String getRequestId(){
        return threadLocal.get();
    }

    // 关闭threadlocal
    public static void close(){
        threadLocal.remove();
    }

}