package com.parfait.study.starter.namedroutingdatasource.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "named-routing-data-source")
public class NamedRoutingDataSourceGlobalProperties {

	private Boolean enabled;
	private String slaveSuffix = "-slave";
	private String defaultDataSource = "default";
	private List<NamedRoutingDataSourceTargetProperties> dataSources;
}
