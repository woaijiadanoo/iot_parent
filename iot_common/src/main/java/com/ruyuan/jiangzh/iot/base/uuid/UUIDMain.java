package com.ruyuan.jiangzh.iot.base.uuid;

public class UUIDMain {

    public static void main(String[] args) {
        String dataId00 = "1ecd28f9a97f910a05fdbd50d7d93eb";
        System.out.println("dataId00 = " + UUIDHelper.fromStringId(dataId00));
        
        String dataId01 = "1ecd90c5ad5f8d09f6a7fff0967ec80";
        System.out.println("dataId01 = " + UUIDHelper.fromStringId(dataId01));

    }

}
