package com.github.beguy.ifuture_server.aspect.methodsCallProfiler;

import com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl.keyCounter.KeyCountersPerUnitTime;

public interface MethodsCallCounters extends KeyCountersPerUnitTime<String, Long> {
}
