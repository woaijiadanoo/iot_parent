package com.ruyuan.jiangzh.iot.rule.infrastructure.actors.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruyuan.jiangzh.iot.actors.ActorSystemContext;

public abstract class AbstractMsgProcessor {

    protected final ActorSystemContext systemContext;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected AbstractMsgProcessor(ActorSystemContext systemContext){
        this.systemContext = systemContext;
    }

}
