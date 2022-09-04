package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.extension.filter;

import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.NodeConfiguration;

public class ExtensionJsFilterNodeConfiguration implements NodeConfiguration<ExtensionJsFilterNodeConfiguration> {

    // return msg.temerature > 45;
    private String jsScript;

    @Override
    public ExtensionJsFilterNodeConfiguration defaultConfiguration() {
        ExtensionJsFilterNodeConfiguration configuration = new ExtensionJsFilterNodeConfiguration();
        configuration.setJsScript("return msg.temerature > 45;");
        return configuration;
    }

    public String getJsScript() {
        return jsScript;
    }

    public void setJsScript(String jsScript) {
        this.jsScript = jsScript;
    }
}
