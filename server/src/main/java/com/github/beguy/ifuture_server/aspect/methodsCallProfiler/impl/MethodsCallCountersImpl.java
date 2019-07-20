package com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl;

import com.github.beguy.ifuture_server.aspect.methodsCallProfiler.MethodsCallCounters;
import com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl.keyCounter.impl.LongKeyCountersPerUnitTimeImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class MethodsCallCountersImpl extends LongKeyCountersPerUnitTimeImpl<String> implements MethodsCallCounters {
}
