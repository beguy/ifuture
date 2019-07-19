package com.github.beguy.ifuture_server.mbean.impl;

import com.github.beguy.ifuture_server.mbean.KeyCounterPerUnitTime;
import com.github.beguy.ifuture_server.mbean.ServiceLoadProfilerMXBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.Set;

@ManagedResource
@Component
public class ServiceLoadProfiler implements KeyCounterPerUnitTime<String /*Method names*/, Long>, ServiceLoadProfilerMXBean {
    private static final Log log = LogFactory.getLog(ServiceLoadProfiler.class);

    @Lazy
    private KeyCounterPerUnitTime<String, Long> requestCounters;

    public ServiceLoadProfiler() {
    }

    // ServiceLoadProfilerMXBean
    @Autowired
    public ServiceLoadProfiler(@Qualifier("longKeyCounterPerUnitTimeImpl") KeyCounterPerUnitTime<String, Long> requestCounters) {
        this.requestCounters = requestCounters;
    }

    @Override
    @ManagedOperation
    public Long getNumberOfRequestsProcessedPerUnitTime(String methodName) {
        return getValuePerUnitTime(methodName);
    }

    @Override
    public Long getNumberOfRequestsProcessedPerUnitTime(long methodNumber) {
        return getValuePerUnitTimeByKeyNumber(methodNumber);
    }

    @Override
    @ManagedOperation
    public Long getNumberOfRequestsFromAllUsers(String methodName) {
        return getValue(methodName);
    }

    @Override
    public Long getNumberOfRequestsFromAllUsers(long methodNumber) {
        return getValueByKeyNumber(methodNumber);
    }

    @Override
    @ManagedOperation
    public boolean clearNumberOfRequests(String methodName) {
        this.reset(methodName);
        return true;
    }

    @Override
    public boolean clearNumberOfRequests(Long methodNumber) {
        resetByKeyNumber(methodNumber);
        return true;
    }

    @Override
    @ManagedOperation
    public String writeToLog(String methodName) {
        String msg = String.format("Method %s\n have number of requests from all clients = %d" +
                        "\n have number of requests processed on the server = %d per unit time = %d[milliseconds]\n",
                methodName, getNumberOfRequestsFromAllUsers(methodName),
                getNumberOfRequestsProcessedPerUnitTime(methodName), getUnitTime());
        log.info(msg);
        return msg;
    }

    @Override
    public String writeToLog(long methodNumber) {
        String msg = String.format("Method %s\n have number of requests from all clients = %d" +
                        "\n have number of requests processed on the server = %d per unit time = %d[milliseconds]\n",
                getKey(methodNumber), getNumberOfRequestsFromAllUsers(methodNumber),
                getNumberOfRequestsProcessedPerUnitTime(methodNumber), getUnitTime());
        log.info(msg);
        return msg;
    }

    @Override
    @ManagedOperation
    public String writeToLogAll() {
        StringBuilder msg = new StringBuilder();
        for (String methodName : keySet()) {
            msg.append(writeToLog(methodName));
        }
        return msg.toString();
    }

    // wrap KeyCounterPerUnitTime

    @Override
    @ManagedAttribute
    public long getUnitTime() {
        return requestCounters.getUnitTime();
    }

    @Override
    @ManagedAttribute
    public void setUnitTime(long millis) {
        requestCounters.setUnitTime(millis);
    }

    @Override
    public Long getValuePerUnitTime(String key) {
        return requestCounters.getValuePerUnitTime(key);
    }

    @Override
    public Long getValuePerUnitTimeByKeyNumber(long key) {
        return requestCounters.getValuePerUnitTimeByKeyNumber(key);
    }

    @Override
    @ManagedAttribute
    public Set<String> getMethodsNames() {
        return requestCounters.keySet();
    }

    @Override
    public void increment(String key) {
        requestCounters.increment(key);
    }

    @Override
    public void incrementByKeyNumber(long keyNumer) {
        requestCounters.incrementByKeyNumber(keyNumer);
    }

    @Override
    public Set<String> keySet() {
        return requestCounters.keySet();
    }

    @Override
    public Long getValue(String key) {
        return requestCounters.getValue(key);
    }

    @Override
    public Long getValueByKeyNumber(long key) {
        return requestCounters.getValueByKeyNumber(key);
    }

    @Override
    public void reset(String key) {
        requestCounters.reset(key);
    }

    @Override
    public void resetByKeyNumber(long key) {
        requestCounters.resetByKeyNumber(key);
    }

    @Override
    public String getKey(long keyNumber) {
        return requestCounters.getKey(keyNumber);
    }
}
