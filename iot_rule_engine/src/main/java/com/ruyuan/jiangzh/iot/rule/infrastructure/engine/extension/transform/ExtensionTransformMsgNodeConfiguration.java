package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.extension.transform;

import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.NodeConfiguration;

public class ExtensionTransformMsgNodeConfiguration implements NodeConfiguration<ExtensionTransformMsgNodeConfiguration> {

    private String jsScript;

    @Override
    public ExtensionTransformMsgNodeConfiguration defaultConfiguration() {
        ExtensionTransformMsgNodeConfiguration configuration = new ExtensionTransformMsgNodeConfiguration();
        configuration.setJsScript("return {msg: msg, metadata: metadata, msgType: msgType};");
        return configuration;
    }

    public String getJsScript() {
        return jsScript;
    }

    public void setJsScript(String jsScript) {
        this.jsScript = jsScript;
    }
}
