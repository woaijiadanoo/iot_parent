package com.ruyuan.jiangzh.iot.actors.msg.rule;

import java.io.Serializable;

public class TransportToRuleEngineActorMsg implements Serializable {

    // TAG类型的数据
    private PostTelemetryMsg postTelemetryMsg;
    // 属性类型的数据

    // 状态类型的数据

    public TransportToRuleEngineActorMsg(){}

    public TransportToRuleEngineActorMsg(NewBuilder builder){
        this.postTelemetryMsg = builder.postTelemetryMsg;
    }

    public static class NewBuilder{
        private PostTelemetryMsg postTelemetryMsg;

        public NewBuilder setPostTelemetryMsg(PostTelemetryMsg postTelemetryMsg) {
            this.postTelemetryMsg = postTelemetryMsg;
            return this;
        }

        public TransportToRuleEngineActorMsg build(){
            return new TransportToRuleEngineActorMsg(this);
        }

    }



    public PostTelemetryMsg getPostTelemetryMsg() {
        return postTelemetryMsg;
    }

    public void setPostTelemetryMsg(PostTelemetryMsg postTelemetryMsg) {
        this.postTelemetryMsg = postTelemetryMsg;
    }

    @Override
    public String toString() {
        return "TransportToRuleEngineActorMsg{" +
                "postTelemetryMsg=" + postTelemetryMsg +
                '}';
    }
}
