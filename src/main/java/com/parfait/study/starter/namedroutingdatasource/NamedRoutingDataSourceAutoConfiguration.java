package com.parfait.study.starter.namedroutingdatasource;

import com.parfait.study.starter.namedroutingdatasource.factory.NamedRoutingDataSourceFactory;
import com.parfait.study.starter.namedroutingdatasource.factory.SimpleNamedRoutingDataSourceFactory;
import com.parfait.study.starter.namedroutingdatasource.properties.NamedRoutingDataSourceGlobalProperties;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(HikariConfig.class)
@ConditionalOnProperty(name = "named-routing-data-source.enabled", havingValue = "true")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(NamedRoutingDataSourceGlobalProperties.class)
public class NamedRoutingDataSourceAutoConfiguration {

	@Autowired
	private NamedRoutingDataSourceGlobalProperties properties;

	@Bean
	@Primary
	public DataSource dataSource() {
		return namedRoutingDataSourceFactory().create(properties);
	}

	private NamedRoutingDataSourceFactory namedRoutingDataSourceFactory() {
		return new SimpleNamedRoutingDataSourceFactory();
	}
}
