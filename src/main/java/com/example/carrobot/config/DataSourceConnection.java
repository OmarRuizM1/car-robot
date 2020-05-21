package com.example.carrobot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConnection {

    private final DataSourceConfig dataSourceConfig;

    @Autowired
    public DataSourceConnection(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceConfig.getDriver());
        dataSource.setUrl(dataSourceConfig.getUrl());
        dataSource.setUsername(dataSourceConfig.getUsername());
        dataSource.setPassword(dataSourceConfig.getPassword());
        return dataSource;
    }
}
