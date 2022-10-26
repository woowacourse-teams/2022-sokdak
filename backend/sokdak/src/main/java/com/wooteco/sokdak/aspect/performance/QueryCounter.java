package com.wooteco.sokdak.aspect.performance;

public class QueryCounter {

    private int count;

    public void increase() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public boolean isWarn() {
        return count > 10;
    }
}
