package com.parfait.study.starter.namedroutingdatasource.datasource;

import com.parfait.study.starter.namedroutingdatasource.selector.NamedRoutingDataSourceManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;

public class NamedRoutingDataSource extends AbstractRoutingDataSource {

    private final String defaultDataSource;
    private final Map<String, NamedDataSource> dataSourceMap;
    private final Map<String, String> masterSlaveMap;

    public NamedRoutingDataSource(String defaultDataSource, List<NamedDataSource> dataSources) {

        this.defaultDataSource = defaultDataSource;
        this.dataSourceMap = createDataSourceMap(dataSources);
        this.masterSlaveMap = dataSourceMap.values()
                                           .stream()
                                           .filter(NamedDataSource::isSlave)
                                           .collect(toMap(NamedDataSource::getSlaveOf, NamedDataSource::getName));
        super.setTargetDataSources(dataSourceMap.entrySet().stream().collect(toMap(Entry::getKey, Entry::getValue)));
        super.setDefaultTargetDataSource(dataSourceMap.get(defaultDataSource));
    }

    private Map<String, NamedDataSource> createDataSourceMap(List<NamedDataSource> dataSources) {
        return dataSources.stream().collect(toMap(NamedDataSource::getName, Function.identity()));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceName = NamedRoutingDataSourceManager.getCurrentDataSourceName();

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

    public List<NamedDataSource> masters() {
        return dataSourceMap.values().stream().filter(dataSource -> !dataSource.isSlave()).collect(Collectors.toList());
    }
}
