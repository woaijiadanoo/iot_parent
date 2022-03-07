package com.ruyuan.jiangzh.iot.base.web;

import com.ruyuan.jiangzh.iot.common.IoTStringUtils;

/**
 *  公共的web返回对象
 * @param <M>
 */
public class RespDTO<M>{

    // 状态码，遵循HTTP Code要求，200为正常返回
    private int code;
    // 单次请求唯一标识
    private String requestId;
    // 返回的信息 - 国际化
    private String message;
    // 状态值
    private String state;
    // 待返回的数据
    private M data;

    /**
     *  成功情况下的返回值 - 不携带返回值
     * @param <M>
     * @return
     */
    public static<M> RespDTO success(){
        RespDTO dto = new RespDTO();
        dto.setCode(RespCodeEnum.SUCCESS.getCode());
        dto.setRequestId(IoTStringUtils.requestId());
        dto.setMessage("");
        dto.setState("");
        dto.setData(null);

        return dto;
    }

    /**
     *  成功情况下的返回值 - 携带返回值
     * @param <M>
     * @return
     */
    public static<M> RespDTO success(M data){
        RespDTO dto = new RespDTO();
        dto.setCode(RespCodeEnum.SUCCESS.getCode());
        dto.setRequestId(IoTStringUtils.requestId());
        dto.setMessage("");
        dto.setState("");
        dto.setData(data);

        return dto;
    }

    /**
     *  失败情况下的返回值 - 不携带state
     * @param <M>
     * @return
     */
    public static<M> RespDTO failture(int code,String message){
        RespDTO dto = new RespDTO();
        dto.setCode(code);
        dto.setRequestId(IoTStringUtils.requestId());
        dto.setMessage(message);
        dto.setState("");
        dto.setData(null);

        return dto;
    }

    /**
     *  失败情况下的返回值 - 不携带state
     * @param <M>
     * @return
     */
    public static<M> RespDTO systemFailture(Exception ex){
        RespDTO dto = new RespDTO();
        dto.setCode(RespCodeEnum.SYSTEM_ERROR.getCode());
        dto.setRequestId(IoTStringUtils.requestId());
        dto.setMessage(ex.getMessage());
        dto.setState("");
        dto.setData(null);

        return dto;
    }

    /**
     *  失败情况下的返回值 - 携带state
     * @param <M>
     * @return
     */
    public static<M> RespDTO failture(int code,String message,String state){
        RespDTO dto = new RespDTO();
        dto.setCode(code);
        dto.setRequestId(IoTStringUtils.requestId());
        dto.setMessage(message);
        dto.setState(state);
        dto.setData(null);

        return dto;
    }

    private void setCode(int code) {
        this.code = code;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private void setState(String state) {
        this.state = state;
    }

    private void setData(M data) {
        this.data = data;
    }


    private void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }

    public M getData() {
        return data;
    }

    public String getRequestId() {
        return requestId;
    }

}
