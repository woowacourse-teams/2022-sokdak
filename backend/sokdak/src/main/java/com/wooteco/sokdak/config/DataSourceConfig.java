package com.wooteco.sokdak.config;

import com.wooteco.sokdak.support.datasource.RoutingDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Profile({"dev", "prod"})
@Configuration
public class DataSourceConfig {

    private static final String MASTER_SERVER = "MASTER";
    private static final String SLAVE1_SERVER = "SLAVE1";
    private static final String SLAVE2_SERVER = "SLAVE2";

    @Bean
    @Qualifier(MASTER_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(SLAVE1_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(SLAVE2_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.slave2")
    public DataSource slave2DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(@Qualifier(MASTER_SERVER) DataSource masterDataSource,
                                        @Qualifier(SLAVE1_SERVER) DataSource slave1DataSource,
                                        @Qualifier(SLAVE2_SERVER) DataSource slave2DataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("master", masterDataSource);
        dataSources.put("slave1", slave1DataSource);
        dataSources.put("slave2", slave2DataSource);

        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource determinedDataSource = routingDataSource(masterDataSource(), slave1DataSource(), slave2DataSource());
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }
}
