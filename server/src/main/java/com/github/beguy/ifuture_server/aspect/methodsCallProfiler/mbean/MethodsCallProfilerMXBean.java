package com.github.beguy.ifuture_server.aspect.methodsCallProfiler.mbean;

import com.github.beguy.ifuture_server.aspect.Switchable;

import java.util.Set;

public interface MethodsCallProfilerMXBean extends Switchable {
    Set<String> getMethodsNames();

    long getUnitTime();

    void setUnitTime(long millis);

    Long getNumberOfRequestsProcessedPerUnitTime(String methodName);

    Long getNumberOfRequestsProcessedPerUnitTime(long methodNumber);

    Long getNumberOfRequestsFromAllUsers(String methodName);

    Long getNumberOfRequestsFromAllUsers(int methodNumber);

    boolean clearNumberOfRequests(String methodName);

    boolean clearNumberOfRequests(Integer methodNumber);

    String writeToLog(String methodName);

    String writeToLog(int methodName);

    String writeToLogAll();
}
