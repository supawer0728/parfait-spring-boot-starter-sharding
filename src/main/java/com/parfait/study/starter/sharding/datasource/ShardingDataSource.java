package com.parfait.study.starter.sharding.datasource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Delegate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShardingDataSource implements DataSource {

    private final String name;
    private final String slaveOf;
    @Delegate(types = DataSource.class)
    private final DataSource delegate;

    public static ShardingDataSource slaveOf(String slaveOf, String slaveSuffix, DataSource delegate) {
        return new ShardingDataSource(slaveOf + slaveSuffix, slaveOf, delegate);
    }

    public static ShardingDataSource asMaster(String name, DataSource delegate) {
        return new ShardingDataSource(name, null, delegate);
    }

    public boolean isSlave() {
        return !StringUtils.isEmpty(slaveOf);
    }
}
