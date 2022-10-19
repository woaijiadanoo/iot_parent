package com.ruyuan.jiangzh.protol.infrastructure.protocol.common;

/**
 * @author jiangzheng
 * @version 1.0
 * @description: 定义回调接口
 */
public interface ProtocolServiceCallback<T> {

    // 成功的返回
    void onSuccess(T msg);

    // 异常的返回
    void onError(Throwable e);

}
