package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common;

public class RuleNodeScriptFactory {

    public static final String MSG="msg";
    public static final String METADATA="metadata";
    public static final String MSG_TYPE="msgType";

    public static final String RULE_NODE_FUNCTION_NAME = "ruleNodefunc";

    private static final String JS_WRAPPER_PERFIX_TEMPLATE="function %s(msgStr, metadataStr, msgType){\n" +
            "\tvar msg = JSON.parse(msgStr);\n" +
            "\tvar metadata = JSON.parse(metadataStr);\n" +
            "\treturn JSON.stringify(%s(msg,metadata,msgType));\n" +
            "\t\n" +
            "\tfunction %s(%s,%s,%s){";

    private static final String JS_WRAPPER_SUFFIX_TEMPLATE="\n}\n" +
            "\t\n" +
            "}";

    public static String generateJsScript(String functionName, String scriptBody, String ... argNames){
        String msgArg;
        String metadataArg;
        String msgTypeArg;
        if(argNames != null && argNames.length == 3){
            msgArg = argNames[0];
            metadataArg = argNames[1];
            msgTypeArg = argNames[2];
        }else{
            msgArg = MSG;
            metadataArg = METADATA;
            msgTypeArg = MSG_TYPE;
        }

        String jsWrapperPrefixResult = String.format(JS_WRAPPER_PERFIX_TEMPLATE,
                functionName, RULE_NODE_FUNCTION_NAME, RULE_NODE_FUNCTION_NAME
            , msgArg, metadataArg, msgTypeArg);

        return jsWrapperPrefixResult + scriptBody + JS_WRAPPER_SUFFIX_TEMPLATE;
    }

    public static void main(String[] args) {
        String scriptBody = "retun msg.temperature > 40;";
        String scriptResult = RuleNodeScriptFactory.generateJsScript("test", scriptBody);
        System.out.println("scriptResult = " + scriptResult);
    }

}
