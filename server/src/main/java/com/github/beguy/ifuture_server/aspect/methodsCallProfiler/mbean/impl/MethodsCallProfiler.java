package com.github.beguy.ifuture_server.aspect.methodsCallProfiler.mbean.impl;

import com.github.beguy.ifuture_server.aspect.methodsCallProfiler.MethodsCallCounters;
import com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl.keyCounter.KeyCountersPerUnitTime;
import com.github.beguy.ifuture_server.aspect.methodsCallProfiler.mbean.MethodsCallProfilerMXBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * MxBean adapter for {@link KeyCountersPerUnitTime}
 */
@Component
@ManagedResource
public class MethodsCallProfiler implements MethodsCallProfilerMXBean {
    private static final Log log = LogFactory.getLog(MethodsCallProfiler.class);
    private final MethodsCallCounters methodsCallCounters;
    private boolean enabled = false;

    @Autowired
    @Lazy
    public MethodsCallProfiler(MethodsCallCounters methodsCallCounters) {
        this.methodsCallCounters = methodsCallCounters;
    }

    @Override
    @ManagedAttribute(defaultValue = "false")
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    @ManagedAttribute
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    @ManagedAttribute
    public long getUnitTime() {
        if (enabled) { // dont create lazy object
            return methodsCallCounters.getUnitTime();
        } else {
            return -1;
        }
    }

    @Override
    @ManagedAttribute(description = "unit time in milliseconds")
    public void setUnitTime(long millis) {
        methodsCallCounters.setUnitTime(millis);
    }

    @Override
    public Set<String> getMethodsNames() {
        if (enabled) { // dont create lazy object
            return methodsCallCounters.keySet();
        } else {
            return null;
        }
    }

    @Override
    @ManagedOperation
    public Long getNumberOfRequestsProcessedPerUnitTime(String methodName) {
        return methodsCallCounters.getValuePerUnitTime(methodName);
    }

    @Override
    public Long getNumberOfRequestsProcessedPerUnitTime(long methodNumber) {
        return methodsCallCounters.getValuePerUnitTimeByKeyNumber(methodNumber);
    }

    @Override
    @ManagedOperation
    public Long getNumberOfRequestsFromAllUsers(String methodName) {
        return methodsCallCounters.getValue(methodName);
    }

    @Override
    public Long getNumberOfRequestsFromAllUsers(int methodNumber) {
        return methodsCallCounters.getValueByKeyNumber(methodNumber);
    }

    @Override
    @ManagedOperation
    public boolean clearNumberOfRequests(String methodName) {
        methodsCallCounters.reset(methodName);
        return true;
    }

    @Override
    public boolean clearNumberOfRequests(Integer methodNumber) {
        methodsCallCounters.resetByKeyNumber(methodNumber);
        return true;
    }

    @Override
    @ManagedOperation
    public String writeToLog(String methodName) {
        String msg = String.format("Method %s\n have number of requests from all clients = %d" +
                        "\n have number of requests processed on the server = %d per unit time = %d[milliseconds]\n",
                methodName, getNumberOfRequestsFromAllUsers(methodName),
                getNumberOfRequestsProcessedPerUnitTime(methodName), getUnitTime());
        log.debug(msg);
        return msg;
    }

    @Override
    @ManagedOperation
    public String writeToLog(int methodNumber) {
        String msg = String.format("Method %s\n have number of requests from all clients = %d" +
                        "\n have number of requests processed on the server = %d per unit time = %d[milliseconds]\n",
                methodsCallCounters.getKey(methodNumber), getNumberOfRequestsFromAllUsers(methodNumber),
                getNumberOfRequestsProcessedPerUnitTime(methodNumber), getUnitTime());
        log.debug(msg);
        return msg;
    }

    @Override
    @ManagedOperation
    public String writeToLogAll() {
        StringBuilder msg = new StringBuilder();
        for (String methodName : methodsCallCounters.keySet()) {
            msg.append(writeToLog(methodName));
        }
        log.debug(msg);
        return msg.toString();
    }
}
