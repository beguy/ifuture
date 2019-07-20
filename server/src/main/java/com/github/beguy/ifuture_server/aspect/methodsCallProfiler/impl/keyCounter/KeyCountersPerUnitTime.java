package com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl.keyCounter;

public interface KeyCountersPerUnitTime<KeyType, CounterType extends Number> extends KeyCounters<KeyType, CounterType> {
    long getUnitTime();

    void setUnitTime(long millis);

    CounterType getValuePerUnitTime(KeyType key);

    CounterType getValuePerUnitTimeByKeyNumber(long key);
}
