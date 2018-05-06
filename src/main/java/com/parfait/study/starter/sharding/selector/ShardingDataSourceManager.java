package com.parfait.study.starter.sharding.selector;

import org.springframework.core.NamedThreadLocal;

public class ShardingDataSourceManager {

    private static final ThreadLocal<String> currentDataSourceName = new NamedThreadLocal<>("name of DataSource selected in this thread");

    private ShardingDataSourceManager() {
        throw new UnsupportedOperationException("this class can't be instance");
    }

    public static String getCurrentDataSourceName() {
        return currentDataSourceName.get();
    }

    public static void setCurrentDataSourceName(String name) {
        currentDataSourceName.set(name);
    }

    public static void removeCurrentDataSourceName() {
        currentDataSourceName.remove();
    }
}
