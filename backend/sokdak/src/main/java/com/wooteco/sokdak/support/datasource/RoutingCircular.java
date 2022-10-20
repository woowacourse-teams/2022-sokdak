package com.wooteco.sokdak.support.datasource;

import java.util.List;

public class RoutingCircular<T> {
    private List<T> dataSources;
    private Integer counter = 0;

    public RoutingCircular(List<T> dataSources) {
        this.dataSources = dataSources;
    }

    public T getOne() {
        int circularSize = dataSources.size();
        if (counter + 1 > circularSize) {
            counter = 0;
        }
        return dataSources.get(counter++ % circularSize);
    }
}
