package com.wooteco.sokdak.support.datasource;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Profile("main")
public class RoutingDataSource extends AbstractRoutingDataSource {

    private RoutingCircular<String> routingCircular;

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        routingCircular = new RoutingCircular<>(
                targetDataSources.keySet().stream()
                        .map(Object::toString)
                        .filter(key -> key.contains("slave"))
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
            return routingCircular.getOne();
        }
        return "master";
    }
}
