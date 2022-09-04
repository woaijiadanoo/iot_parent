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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.management.RuntimeMBeanException;
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

    // 记录ScriptId对应的functionName
    private Map<UUID, String> scriptIdToNameMap = new ConcurrentHashMap<>();

    // 记录每个ScriptId的失败次数，如果超过阈值， 则不会执行，直接返回错误信息
    private Map<UUID, AtomicInteger> blackListedFunctions = new ConcurrentHashMap<>();

    // 两种scriptEngine之一
    private NashornSandbox sandbox;

    // 两种scriptEngine之一
    private ScriptEngine engine;

    // 线程池种类
    private ExecutorService executorService;

    // 使用哪种scriptEngine， sandbox 或者原生
    @Value("${js.js-invoker.use-sandbox}")
    private boolean useSandbox;

    // 线程池数量
    @Value("${js.js-invoker.threadpools}")
    private int threadPoolSize;

    // 最大CPU时间
    @Value("${js.js-invoker.max-cpu-time}")
    private long maxCpuTime;

    // 最大失败次数
    @Value("${js.js-invoker.max-errors}")
    private int maxErrors;

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
        // 1、通过ScriptId来获取functionName，以备后续使用
        String functionName = scriptIdToNameMap.get(scriptId);
        // 1.1、如果functionName不存在，抛出异常
        if(IoTStringUtils.isBlank(functionName)){
            return Futures.immediateFailedFuture(new RuntimeException("No compiled script found scriptId : "+scriptId));
        }
        // 1.2、如果functionName存在
        // 1.2.1、判断function的失败次数是不是超过阈值
        if(!isBlackList(scriptId)){
            // 1.2.1.2、如果没有超过， 则执行function
            return doInvokerFunction(scriptId, functionName, args);
        } else {
            // 1.2.1.1、如果超过，则直接返回错误信息
            return Futures.immediateFailedFuture(new RuntimeException("scriptId : "+scriptId + " is blacklist, max error count : "+getMaxErrors() ));
        }
    }

    /*
        判断scriptId对应的script有没有超过最大失败次数
     */
    private boolean isBlackList(UUID scriptId) {
        if(blackListedFunctions.containsKey(scriptId)){
            AtomicInteger errors = blackListedFunctions.get(scriptId);
            return errors.get() >= getMaxErrors();
        } else {
            return false;
        }
    }

    /*
        执行real的script脚本
     */
    private ListenableFuture<Object> doInvokerFunction(UUID scriptId, String functionName, Object[] args) {

        try {
            // 执行function
            Object result;
            if(useSandbox){
                result = sandbox.getSandboxedInvocable().invokeFunction(functionName, args);
            } else {
                result = ((Invocable)engine).invokeFunction(functionName, args);
            }
            // 如果执行成功，直接返回
            return Futures.immediateFuture(result);
        } catch (Exception e) {
            // 如果执行不成功， 则返回错误信息，并增加黑名单中的次数
            onScriptExecuteError(scriptId);
            return Futures.immediateFailedFuture(e);
        }
    }

    private void onScriptExecuteError(UUID scriptId) {
        blackListedFunctions.computeIfAbsent(scriptId, key -> new AtomicInteger(0)).incrementAndGet();
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
