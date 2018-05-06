package com.parfait.study.starter.sharding.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "sharding")
public class ShardingProperties {

    private Boolean enabled;
    private String slaveSuffix = "-slave";
    private String defaultDataSource = "default";
    private List<ShardingDataSourceProperties> dataSources;
}
