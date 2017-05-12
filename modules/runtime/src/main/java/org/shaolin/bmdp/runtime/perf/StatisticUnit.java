package org.shaolin.bmdp.runtime.perf;

public enum StatisticUnit {
    Second(1000), Milliseconds(1);
    
    private int value;
    private StatisticUnit(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
