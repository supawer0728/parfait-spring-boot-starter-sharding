package com.parfait.study.starter.sharding.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ShardingDataSourceProperties {

    private String name;
    private String slaveOf;
    private HikariConfig hikari;

    public boolean isSlave() {
        return !StringUtils.isEmpty(slaveOf);
    }
}
