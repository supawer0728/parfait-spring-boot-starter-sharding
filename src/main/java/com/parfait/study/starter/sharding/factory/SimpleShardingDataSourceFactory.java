package com.parfait.study.starter.sharding.factory;

import com.parfait.study.starter.sharding.datasource.ShardingDataSource;
import com.parfait.study.starter.sharding.datasource.ShardingRoutingDataSource;
import com.parfait.study.starter.sharding.properties.ShardingDataSourceProperties;
import com.parfait.study.starter.sharding.properties.ShardingProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleShardingDataSourceFactory implements ShardingDataSourceFactory {
    @Override
    public DataSource create(ShardingProperties properties) {

        List<ShardingDataSource> dataSources = createShardingDataSources(properties);

        return new ShardingRoutingDataSource(properties.getDefaultDataSource(), dataSources);
    }

    private List<ShardingDataSource> createShardingDataSources(ShardingProperties properties) {

        return properties.getDataSources().stream().map(dataSourceProperties -> createShardingDataSource(properties, dataSourceProperties)).collect(Collectors.toList());
    }

    private ShardingDataSource createShardingDataSource(ShardingProperties properties, ShardingDataSourceProperties dataSourceProperties) {

        DataSource dataSource = lazyConnectionProxy(hikariDataSource(dataSourceProperties.getHikari()));

        return dataSourceProperties.isSlave()
                ? ShardingDataSource.slaveOf(dataSourceProperties.getSlaveOf(), properties.getSlaveSuffix(), dataSource)
                : ShardingDataSource.asMaster(dataSourceProperties.getName(), dataSource);
    }

    private HikariDataSource hikariDataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }

    private LazyConnectionDataSourceProxy lazyConnectionProxy(DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }
}
