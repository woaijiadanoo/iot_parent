package com.ruyuan.jiangzh.iot.actors.msg;

public enum MsgType {

    GET_ATTRIBUTE_REQUEST(true);

    private final boolean requireRulesProcessing;

    // 默认的情况下不需要处理
    MsgType(){
        this(false);
    }

    MsgType(boolean requireRulesProcessing){
        this.requireRulesProcessing = requireRulesProcessing;
    }

    public boolean requireRulesProcessing() {
        return requireRulesProcessing;
    }
}
