package com.ruyuan.jiangzh.iot.base.uuid;

public class UUIDMain {

    public static void main(String[] args) {
        String dataId00 = "1ecc89ff20cc080989e8b76480d43cf";
        System.out.println("dataId00 = " + UUIDHelper.fromStringId(dataId00));
        
        String dataId01 = "1ecc8a0940f0e60989e8b76480d43cf";
        System.out.println("dataId01 = " + UUIDHelper.fromStringId(dataId01));

    }

}
