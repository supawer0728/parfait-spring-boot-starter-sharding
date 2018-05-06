package com.parfait.study.starter.sharding.datasource;

import com.parfait.study.starter.sharding.selector.ShardingDataSourceManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class ShardingRoutingDataSource extends AbstractRoutingDataSource {

    private final String defaultDataSource;
    private final Map<String, ShardingDataSource> dataSourceMap;
    private final Map<String, String> masterSlaveMap;

    public ShardingRoutingDataSource(String defaultDataSource, List<ShardingDataSource> dataSources) {

        this.defaultDataSource = defaultDataSource;
        this.dataSourceMap = createDataSourceMap(dataSources);
        this.masterSlaveMap = dataSourceMap.values().stream().filter(ShardingDataSource::isSlave).collect(toMap(ShardingDataSource::getSlaveOf, ShardingDataSource::getName));
        super.setTargetDataSources(dataSourceMap.entrySet().stream().collect(toMap(entry -> (Object) entry.getKey(), entry -> (Object) entry.getValue())));
        super.setDefaultTargetDataSource(dataSourceMap.get(defaultDataSource));
    }

    private Map<String, ShardingDataSource> createDataSourceMap(List<ShardingDataSource> dataSources) {
        return dataSources.stream().collect(toMap(ShardingDataSource::getName, Function.identity()));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = ShardingDataSourceManager.getCurrentDataSourceName();

        if (dataSourceName == null) {
            dataSourceName = defaultDataSource;
        }

        boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        if (transactionActive) {
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            if (readOnly) {
                return masterSlaveMap.getOrDefault(dataSourceName, dataSourceName);
            }
        }

        return dataSourceName;
    }
}
