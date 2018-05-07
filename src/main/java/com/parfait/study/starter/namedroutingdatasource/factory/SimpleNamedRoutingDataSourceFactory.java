package com.parfait.study.starter.namedroutingdatasource.factory;

import com.parfait.study.starter.namedroutingdatasource.datasource.NamedDataSource;
import com.parfait.study.starter.namedroutingdatasource.datasource.NamedRoutingDataSource;
import com.parfait.study.starter.namedroutingdatasource.datasource.RoundRobinRoutingDataSource;
import com.parfait.study.starter.namedroutingdatasource.properties.NamedRoutingDataSourceGlobalProperties;
import com.parfait.study.starter.namedroutingdatasource.properties.NamedRoutingDataSourceTargetProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class SimpleNamedRoutingDataSourceFactory implements NamedRoutingDataSourceFactory {

    @Override
    public NamedRoutingDataSource create(NamedRoutingDataSourceGlobalProperties properties) {

        List<NamedDataSource> dataSources = createNamedDataSources(properties);

        return new NamedRoutingDataSource(properties.getDefaultDataSource(), dataSources);
    }

    private List<NamedDataSource> createNamedDataSources(NamedRoutingDataSourceGlobalProperties properties) {

        List<NamedDataSource> namedDataSources = properties.getDataSources()
                                                           .stream()
                                                           .map(dataSourceProperties -> createNamedDataSource(properties, dataSourceProperties))
                                                           .collect(toList());

        List<NamedDataSource> slaveGroups = createRoundRobinSlaveGroups(namedDataSources, properties);
        List<NamedDataSource> masters = namedDataSources.stream().filter(ds -> !ds.isSlave()).collect(toList());

        return Stream.concat(masters.stream(), slaveGroups.stream()).collect(toList());
    }

    private NamedDataSource createNamedDataSource(NamedRoutingDataSourceGlobalProperties properties, NamedRoutingDataSourceTargetProperties dataSourceProperties) {

        DataSource dataSource = lazyConnectionProxy(hikariDataSource(dataSourceProperties.getHikari()));

        return dataSourceProperties.isSlave()
            ? NamedDataSource.slaveOf(dataSourceProperties.getSlaveOf(), properties.getSlaveSuffix(), dataSource)
            : NamedDataSource.asMaster(dataSourceProperties.getName(), dataSource);
    }

    private List<NamedDataSource> createRoundRobinSlaveGroups(List<NamedDataSource> namedDataSources, NamedRoutingDataSourceGlobalProperties properties) {
        List<NamedDataSource> slaves = namedDataSources.stream().filter(NamedDataSource::isSlave).collect(toList());
        Map<String, List<NamedDataSource>> slavesMap = slaves.stream().collect(groupingBy(NamedDataSource::getSlaveOf, Collectors.toList()));
        return slavesMap.entrySet()
                        .stream()
                        .map(entry -> NamedDataSource.slaveOf(entry.getKey(), properties.getSlaveSuffix(), new RoundRobinRoutingDataSource(entry.getValue())))
                        .collect(toList());
    }

    private HikariDataSource hikariDataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }

    private LazyConnectionDataSourceProxy lazyConnectionProxy(DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }
}
