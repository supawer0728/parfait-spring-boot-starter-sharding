package com.parfait.study.starter.namedroutingdatasource.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class RoundRobinRoutingDataSource extends AbstractRoutingDataSource {

    private final AtomicInteger index = new AtomicInteger(Integer.MIN_VALUE);
    private final int size;

    public RoundRobinRoutingDataSource(List<? extends DataSource> dataSources) {
        this.size = dataSources.size();
        super.setTargetDataSources(IntStream.range(0, size).boxed().collect(toMap(Function.identity(), dataSources::get)));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return index.getAndIncrement() % size;
    }
}
