package com.github.beguy.ifuture_server.mbean;

public interface KeyCounterPerUnitTime<KeyType, CounterType extends Number> extends KeyCounter<KeyType, CounterType> {
    long getUnitTime();

    void setUnitTime(long millis);

    CounterType getValuePerUnitTime(KeyType key);

    CounterType getValuePerUnitTimeByKeyNumber(long key);
}
