package com.parfait.study.starter.namedroutingdatasource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "named-routing-data-source")
public class NamedRoutingDataSourceGlobalProperties {

    private Boolean enabled;
    private String slaveSuffix = "-slave";
    private String defaultDataSource = "default";
    private List<NamedRoutingDataSourceTargetProperties> dataSources;
}
