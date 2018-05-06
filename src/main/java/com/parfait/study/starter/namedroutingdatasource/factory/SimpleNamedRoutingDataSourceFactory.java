package com.parfait.study.starter.namedroutingdatasource.factory;

import com.parfait.study.starter.namedroutingdatasource.datasource.NamedDataSource;
import com.parfait.study.starter.namedroutingdatasource.datasource.NamedRoutingDataSource;
import com.parfait.study.starter.namedroutingdatasource.properties.NamedRoutingDataSourceGlobalProperties;
import com.parfait.study.starter.namedroutingdatasource.properties.NamedRoutingDataSourceTargetProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleNamedRoutingDataSourceFactory implements NamedRoutingDataSourceFactory {

    @Override
    public NamedRoutingDataSource create(NamedRoutingDataSourceGlobalProperties properties) {

        List<NamedDataSource> dataSources = createNamedDataSources(properties);

        return new NamedRoutingDataSource(properties.getDefaultDataSource(), dataSources);
    }

    private List<NamedDataSource> createNamedDataSources(NamedRoutingDataSourceGlobalProperties properties) {

        return properties.getDataSources()
                         .stream()
                         .map(dataSourceProperties -> createNamedDataSource(properties, dataSourceProperties))
                         .collect(Collectors.toList());

    }

    private NamedDataSource createNamedDataSource(NamedRoutingDataSourceGlobalProperties properties, NamedRoutingDataSourceTargetProperties dataSourceProperties) {

        DataSource dataSource = lazyConnectionProxy(hikariDataSource(dataSourceProperties.getHikari()));

        return dataSourceProperties.isSlave()
            ? NamedDataSource.slaveOf(dataSourceProperties.getSlaveOf(), properties.getSlaveSuffix(), dataSource)
            : NamedDataSource.asMaster(dataSourceProperties.getName(), dataSource);
    }

    private HikariDataSource hikariDataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }

    private LazyConnectionDataSourceProxy lazyConnectionProxy(DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }
}
