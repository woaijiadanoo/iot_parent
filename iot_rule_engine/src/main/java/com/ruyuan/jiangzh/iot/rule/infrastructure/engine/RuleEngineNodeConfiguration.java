package com.ruyuan.jiangzh.iot.rule.infrastructure.engine;


import com.fasterxml.jackson.databind.JsonNode;

public final class RuleEngineNodeConfiguration {
    private final JsonNode data;

    public RuleEngineNodeConfiguration(JsonNode data) {
        this.data = data;
    }

    public JsonNode getData() {
        return data;
    }
}
