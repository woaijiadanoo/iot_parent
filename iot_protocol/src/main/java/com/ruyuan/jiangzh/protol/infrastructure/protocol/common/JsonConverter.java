package com.ruyuan.jiangzh.protol.infrastructure.protocol.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.ruyuan.jiangzh.protol.infrastructure.protocol.messages.PostTelemetryMsg;

public class JsonConverter {

    /*
        给后面的内容做预留

        {"key":"testKey","value":"testValue"}
     */
    public static PostTelemetryMsg convertToTelemetryMsg(JsonElement jsonObject) throws JsonSyntaxException {
        PostTelemetryMsg postTelemetryMsg  = new PostTelemetryMsg();

        return postTelemetryMsg;
    }

}
