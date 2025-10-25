package com.citytransport.util;

public class PerformanceTracker {
    private int operations;

    public PerformanceTracker() {
        this.operations = 0;
    }

    public void incrementOperations(int count) {
        this.operations += count;
    }

    public int getOperations() {
        return operations;
    }

    public void reset() {
        this.operations = 0;
    }
}