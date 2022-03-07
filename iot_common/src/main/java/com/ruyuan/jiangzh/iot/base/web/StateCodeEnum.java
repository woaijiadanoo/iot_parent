package com.ruyuan.jiangzh.iot.base.web;

/**
 *  模拟对应的state获取过程
 */
public enum StateCodeEnum {

    DEFAULT( 0, "default","默认state")
    , RESOURCE_NOT_EXISTS(RespCodeEnum.RESOURCE_NOT_EXISTS.getCode(), "resource_not_exists","资源不存在")
    , SYSTEM_ERROR(RespCodeEnum.SYSTEM_ERROR.getCode(), "system_error", "系统内部存错")
    ;

    private int code;
    private String state;
    private String stateMsg;

    StateCodeEnum(int code,String state,String stateMsg){
        this.code = code;
        this.state = state;
        this.stateMsg = stateMsg;
    }

    /**
     *  根据返回code，找到对应的state信息
     * @param code
     * @return
     */
    public static String getState(int code){
        for (StateCodeEnum e : values()){
            if(e.getCode() == code){
                return e.getState();
            }
        }

        return DEFAULT.getState();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }
}
