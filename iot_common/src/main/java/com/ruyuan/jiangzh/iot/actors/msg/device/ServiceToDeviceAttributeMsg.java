package com.ruyuan.jiangzh.iot.actors.msg.device;

import java.io.Serializable;

public class ServiceToDeviceAttributeMsg implements Serializable {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
