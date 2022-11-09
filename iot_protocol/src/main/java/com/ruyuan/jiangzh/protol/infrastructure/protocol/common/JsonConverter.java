package com.ruyuan.jiangzh.protol.infrastructure.protocol.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.PostTelemetryMsg;

public class JsonConverter {

    private static int maxStringValueLength;

    /*
            给后面的内容做预留

            {"key":"testKey","value":"testValue"}
            PostTelemetryMsg
                kvproto
                    timestamp
                    kv -> map

            PostTelemetryMsg
                kvproto
                    timestamp : 1293812398123
                    kv
                        key - > testKey
                        type -> long
                        value -> 40

            {"key":"testKey","value": 40}
         */
    public static PostTelemetryMsg convertToTelemetryMsg(JsonElement jsonObject) throws JsonSyntaxException {
        PostTelemetryMsg postTelemetryMsg  = new PostTelemetryMsg();

        return postTelemetryMsg;
    }



    public static void setMaxStringValueLength(int maxLength) {
        maxStringValueLength = maxLength;
    }


}
