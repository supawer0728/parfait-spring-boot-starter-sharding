package com.parfait.study.starter.sharding;

import com.parfait.study.starter.sharding.factory.ShardingDataSourceFactory;
import com.parfait.study.starter.sharding.factory.SimpleShardingDataSourceFactory;
import com.parfait.study.starter.sharding.properties.ShardingProperties;
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
@ConditionalOnProperty(name = "sharding.enabled", havingValue = "true")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(ShardingProperties.class)
public class ShardingAutoConfiguration {

    @Autowired
    private ShardingProperties properties;

    @Bean
    @Primary
    public DataSource dataSource() {
        return shardingDataSourceFactory().create(properties);
    }

    private ShardingDataSourceFactory shardingDataSourceFactory() {
        return new SimpleShardingDataSourceFactory();
    }
}
