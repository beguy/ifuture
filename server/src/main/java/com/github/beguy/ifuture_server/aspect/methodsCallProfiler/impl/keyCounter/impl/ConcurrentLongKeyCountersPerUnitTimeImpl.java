package com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl.keyCounter.impl;

import com.github.beguy.ifuture_server.aspect.methodsCallProfiler.impl.keyCounter.KeyCountersPerUnitTime;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

@Component
@Lazy
public class ConcurrentLongKeyCountersPerUnitTimeImpl<KeyType> implements KeyCountersPerUnitTime<KeyType, Long> {
    private Map<KeyType, Counter> counters = new HashMap<>();
    private long commonUnitTimeMillis = 1000; // Milliseconds

    @Override
    public long getUnitTime() {
        return commonUnitTimeMillis;
    }

    @Override
    public void setUnitTime(long millis) {
        commonUnitTimeMillis = millis;
    }

    @Override
    public Long getValuePerUnitTime(KeyType key) {
        return counters.get(key).getValuePerUnitTime();
    }

    @Override
    public Long getValuePerUnitTimeByKeyNumber(long key) {
        return getCounterByNumber(key).getValuePerUnitTime();
    }

    @Override
    public void increment(KeyType key) {
        if (counters.containsKey(key)) {
            counters.get(key).increment();
        } else {
            Counter counter = new Counter();
            counter.increment();
            counters.put(key, counter);
        }
    }

    @Override
    public void incrementByKeyNumber(int keyNumber) {
        getCounterByNumber(keyNumber).increment();
    }

    @Override
    public Set<KeyType> keySet() {
        return counters.keySet();
    }

    @Override
    public Long getValue(KeyType key) {
        return counters.get(key).getCurrentValue();
    }

    @Override
    public Long getValueByKeyNumber(int keyNumber) {
        return getCounterByNumber(keyNumber).getCurrentValue();
    }

    @Override
    public void reset(KeyType key) {
        counters.get(key).reset();
    }

    @Override
    public void resetByKeyNumber(int keyNumber) {
        getCounterByNumber(keyNumber).reset();
    }

    @Override
    @SuppressWarnings("unchecked")
    public KeyType getKey(int keyNumber) {
        if (counters.keySet().size() > keyNumber) {
            return (KeyType) counters.keySet().toArray()[keyNumber];
        } else throw new IllegalArgumentException("Not found method with index " + keyNumber);
    }

    @SuppressWarnings("unchecked")
    private Counter getCounterByNumber(long keyNumber) {
        if (counters.values().size() > keyNumber) {
            return (Counter) counters.values().toArray()[(int) keyNumber];
        } else throw new IllegalArgumentException("Not found method with index " + keyNumber);
    }

    private class Counter {
        private LongAdder currentCounter = new LongAdder();
        private long valueOnStart;
        private long startTime;
        private long valueOnEnd;
        private long unitTimeMillis; // Milliseconds

        Counter() {
            unitTimeMillis = commonUnitTimeMillis;
            startTime = System.currentTimeMillis();
        }

        long getCurrentValue() {
            return currentCounter.longValue();
        }

        long getValuePerUnitTime() {
            return valueOnStart - valueOnEnd;
        }

        void increment() {
            currentCounter.increment();
            long currentTime = System.currentTimeMillis();
            if (startTime + unitTimeMillis < currentTime) {
                startTime = currentTime;
                valueOnEnd = valueOnStart;
                valueOnStart = currentCounter.longValue();
            }
        }

        void reset() {
            currentCounter.reset();
            valueOnStart = 0L;
            startTime = 0L;
            valueOnEnd = 0L;
            unitTimeMillis = 0L;
        }
    }
}
