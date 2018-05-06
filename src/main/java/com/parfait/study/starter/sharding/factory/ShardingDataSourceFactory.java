package com.parfait.study.starter.sharding.factory;

import com.parfait.study.starter.sharding.properties.ShardingProperties;

import javax.sql.DataSource;

public interface ShardingDataSourceFactory {

    DataSource create(ShardingProperties properties);
}
