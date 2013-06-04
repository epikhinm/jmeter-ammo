package me.schiz.load.ammo;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCyclicCounter {
    private final int maxVal;
    private final AtomicInteger ai = new AtomicInteger(0);

    public AtomicCyclicCounter(int maxVal) {
        this.maxVal = maxVal;
    }

    public int cyclicallyIncrementAndGet() {
        int curVal, newVal;
        do {
            curVal = this.ai.get();
            newVal = (curVal + 1) % this.maxVal;
        } while (!this.ai.compareAndSet(curVal, newVal));
        return newVal;
    }

}