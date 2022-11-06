package com.ruyuan.jiangzh.protol.infrastructure.protocol.exceptions;

public class AdaptorException extends Exception{

    public AdaptorException(){
        super();
    }

    public AdaptorException(String cause){
        super(cause);
    }

    public AdaptorException(Exception cause){
        super(cause);
    }

}
