package com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.vo;

public enum KeyValueType {

    BOOLEAN_V(0),LONG_V(1),DOUBLE_V(2),STRING_V(3);

    private int typeCode;

    KeyValueType(int typeCode){
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
