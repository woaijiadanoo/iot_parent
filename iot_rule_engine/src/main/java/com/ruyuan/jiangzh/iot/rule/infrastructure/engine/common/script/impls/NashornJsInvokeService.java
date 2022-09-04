package com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.impls;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.ruyuan.jiangzh.iot.base.uuid.UUIDHelper;
import com.ruyuan.jiangzh.iot.common.IoTStringUtils;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsInvokeService;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.JsScriptType;
import com.ruyuan.jiangzh.iot.rule.infrastructure.engine.common.script.RuleNodeScriptFactory;
import com.sun.org.apache.xpath.internal.functions.FuncTrue;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.script.Invocable;
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

    // 记录ScriptId与functionName的映射关系
    private Map<UUID, String> scriptIdToNameMap = new ConcurrentHashMap<>();

    // 记录ScriptId对应的失败次数，如果超过一定限制， 则直接进入黑名单，不再执行
    private Map<UUID, AtomicInteger> blackListedFunctions = new ConcurrentHashMap<>();


    // JS引擎实现
    private NashornSandbox sandbox;
    // JS引擎实现
    private ScriptEngine engine;

    private ExecutorService executorService;

    // 以下几项可以修改到配置文件里
    private int threadPoolSize = 4;
    private long maxCpuTime = 100;
    private boolean useSandbox = true;
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
        String functionName = scriptIdToNameMap.get(scriptId);
        if (IoTStringUtils.isBlank(functionName)){
            return Futures.immediateFailedFuture(new RuntimeException("No compiled script found ! scriptId : "+scriptId));
        }
        // 判断有没有被使用
        if (!isBlackListed(scriptId)){
            // 如果没有被使用， 则进行处理
            return doInvokeFunction(scriptId, functionName, args);
        } else {
            return Futures.immediateFailedFuture(new RuntimeException("Script is blacklisted errors : " + getMaxErrors()));
        }
    }

    private boolean isBlackListed(UUID scriptId) {
        if(blackListedFunctions.containsKey(scriptId)){
            AtomicInteger errorCount = blackListedFunctions.get(scriptId);
            // 判断失败次数
            return errorCount.get() >= getMaxErrors();
        } else {
            return false;
        }
    }


    private ListenableFuture<Object> doInvokeFunction(UUID scriptId, String functionName, Object... args) {
        try {
            Object result;
            if(useSandbox){
                // 执行已经编译好的脚本
                result = sandbox.getSandboxedInvocable().invokeFunction(functionName, args);
            } else {
                result = ((Invocable)engine).invokeFunction(functionName, args);
            }
            return Futures.immediateFuture(result);
        } catch (Exception e) {
            onScriptExecutionError(scriptId);
            return Futures.immediateFailedFuture(e);
        }
    }

    private void onScriptExecutionError(UUID scriptId) {
        // 记录当前ScriptId对应的function的失败次数
        blackListedFunctions.computeIfAbsent(scriptId, key -> (new AtomicInteger(0))).incrementAndGet();
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
