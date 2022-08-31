package com.ruyuan.jiangzh.iot;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScriptEngineMain {
    /*

        function sum(){
            var a = 10;
            var b = 5;
            return a + b;
        }

        sum();

       function sum(){var a = 10;var b = 5;return a + b;}
        sum();

     */
    public static void main(String[] args) throws ScriptException {
        String script = "function sum(){var a = 10;var b = 5;return a + b;}\n" +
                "        sum();";

//        scriptEngineManger(script);
//        nashorn(script);
        nashornSandbox(script);
    }

    private static void scriptEngineManger(String script) throws ScriptException {
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        System.out.println("scriptEngineManger ： "+scriptEngine.eval(script));
    }

    private static void nashorn(String script) throws ScriptException{
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();

        ScriptEngine scriptEngine = factory.getScriptEngine(new String[]{"--no-java"});
        System.out.println("nashorn ： "+scriptEngine.eval(script));
    }

    private static void nashornSandbox(String script) throws ScriptException{
        NashornSandbox sandbox = NashornSandboxes.create();
        // 核心配置
        // 核心配置 - 最大CPU占用时间
        sandbox.setMaxCPUTime(100);
        // 核心配置 - LRU缓存大小
        sandbox.setMaxPreparedStatements(30);
        // 核心配置 - 是否允许使用大括号
        sandbox.allowNoBraces(false);
        // 核心配置 - 是否允许使用本地函数
        sandbox.allowLoadFunctions(true);
        // 核心配置 - 执行脚本进程
        ExecutorService executorService = Executors.newWorkStealingPool(50);
        sandbox.setExecutor(executorService);

        System.out.println("nashornSandbox ： "+sandbox.eval(script));

    }

}
