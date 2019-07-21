package com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl.keyCounter;

import java.util.Set;

public interface KeyCounters<KeyType, CounterType extends Number> {
    void increment(KeyType key);

    void incrementByKeyNumber(int keyNumber);

    CounterType getValue(KeyType key);

    CounterType getValueByKeyNumber(int keyNumber);

    void reset(KeyType key);

    void resetByKeyNumber(int keyNumber);

    KeyType getKey(int keyNumber);

    Set<KeyType> keySet();
}
