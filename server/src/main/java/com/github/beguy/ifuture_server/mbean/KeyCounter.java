package com.github.beguy.ifuture_server.mbean;

import java.util.Set;

public interface KeyCounter<KeyType, CounterType extends Number> {
    void increment(KeyType key);

    void incrementByKeyNumber(long keyNumer);

    CounterType getValue(KeyType key);

    CounterType getValueByKeyNumber(long key);

    void reset(KeyType key);

    void resetByKeyNumber(long key);

    KeyType getKey(long keyNumber);

    Set<KeyType> keySet();
}
