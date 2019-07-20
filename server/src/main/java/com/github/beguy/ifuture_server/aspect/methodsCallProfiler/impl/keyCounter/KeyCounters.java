package com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl.keyCounter;

import java.util.Set;

public interface KeyCounters<KeyType, CounterType extends Number> {
    void increment(KeyType key);

    void incrementByKeyNumber(long keyNumber);

    CounterType getValue(KeyType key);

    CounterType getValueByKeyNumber(long keyNumber);

    void reset(KeyType key);

    void resetByKeyNumber(long keyNumber);

    KeyType getKey(long keyNumber);

    Set<KeyType> keySet();
}
