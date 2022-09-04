package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.impls;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsInvokeService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsScriptType;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.RuleNodeScriptFactory;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/*
    目标：编译并保存待执行的Script脚本，并且可以进行Script脚本运行
    1、保存大多数的待执行Script脚本
 */
@Component
public class NashornJsInvokeService implements JsInvokeService {

    private Map<UUID, String> scriptIdToNameMap = new ConcurrentHashMap<>();

    private Map<UUID, AtomicInteger> blackListedFunctions = new ConcurrentHashMap<>();

    private boolean useSandbox = true;

    private NashornSandbox sandbox;

    private ScriptEngine engine;

    private ExecutorService executorService;
    private int threadPoolSize = 4;
    private long maxCpuTime = 100;

    private int maxErrors = 3;

    @Override
    public ListenableFuture<UUID> eval(JsScriptType scriptType, String scriptBody, String... argNames) {
        UUID scriptId = UUIDHelper.genUuid();
        String functionName = "internal_func_" + scriptId.toString().replace("-","_");
        // 按类型区分待生成的JsScript脚本
        String trustJsScript = generateJsScript(scriptType, functionName, scriptBody, argNames);
        return doEval(scriptId, functionName, trustJsScript);
    }

    /*
        按类型区分待生成的JsScript脚本
     */
    private String generateJsScript(JsScriptType scriptType, String functionName, String scriptBody, String... argNames) {
        switch (scriptType) {
            case RULE_NODE_SCRIPT:
                return RuleNodeScriptFactory.generateJsScript(functionName, scriptBody, argNames);
            default:
                throw new RuntimeException("no script type match");
        }
    }

    /*
        使用sandbox或者engine来eval真实的Js Script脚本
     */
    private ListenableFuture<UUID> doEval(UUID scriptId, String functionName, String trustJsScript) {
        try {
            if(useSandbox){
              sandbox.eval(trustJsScript);
            } else{
                engine.eval(trustJsScript);
            }
            // 保存functionName
            scriptIdToNameMap.put(scriptId, functionName);
        } catch (Exception e){
            return Futures.immediateFailedFuture(e);
        }
        return Futures.immediateFuture(scriptId);
    }


    @Override
    public ListenableFuture<Void> release(UUID scriptId) {
        String functionName = scriptIdToNameMap.get(scriptId);
        if(IoTStringUtils.isNotBlank(functionName)){
            try {
                scriptIdToNameMap.remove(scriptId);
                blackListedFunctions.remove(scriptId);
                doRelease(scriptId, functionName);
            } catch (Exception e) {
                return Futures.immediateFailedFuture(e);
            }
        }
        return Futures.immediateFuture(null);
    }

    private void doRelease(UUID scriptId, String functionName) throws ScriptException {
        if(useSandbox){
            sandbox.eval(functionName + " = undefined;");
        } else {
            engine.eval(functionName + " = undefined;");
        }
    }

    @Override
    public ListenableFuture<Object> invokeFunction(UUID scriptId, Object... args) {
        return null;
    }



    @PostConstruct
    public void init(){
        if(useSandbox){
            sandbox = NashornSandboxes.create();
            executorService = Executors.newWorkStealingPool(getThreadPoolSize());

            sandbox.setExecutor(executorService);
            sandbox.setMaxCPUTime(getMaxCpuTime());
            sandbox.allowNoBraces(false);
            sandbox.allowLoadFunctions(true);
            sandbox.setMaxPreparedStatements(30);
        }else{
            NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
            engine = factory.getScriptEngine(new String[]{"--no-java"});
        }
    }

    @PreDestroy
    public void stop(){
        if(executorService != null){
            executorService.shutdownNow();
        }
    }


    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public long getMaxCpuTime() {
        return maxCpuTime;
    }

    public void setMaxCpuTime(long maxCpuTime) {
        this.maxCpuTime = maxCpuTime;
    }

    public int getMaxErrors() {
        return maxErrors;
    }

    public void setMaxErrors(int maxErrors) {
        this.maxErrors = maxErrors;
    }
}
