package com.parfait.study.starter.namedroutingdatasource.factory;

import com.parfait.study.starter.namedroutingdatasource.datasource.NamedRoutingDataSource;
import com.parfait.study.starter.namedroutingdatasource.properties.NamedRoutingDataSourceGlobalProperties;

public interface NamedRoutingDataSourceFactory {

    NamedRoutingDataSource create(NamedRoutingDataSourceGlobalProperties properties);
}
