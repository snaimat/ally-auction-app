package com.ally.demo.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Qualifier("appJdbcTemplate")
    JdbcTemplate appJdbcTemplate(@Autowired DataSource appDataSource){
        return new JdbcTemplate(appDataSource);
    };

    @Bean
    @Qualifier("appNamedParameterJdbcTemplate")
    NamedParameterJdbcTemplate appNamedParameterJdbcTemplate(@Autowired DataSource appDataSource){
        return new NamedParameterJdbcTemplate(appDataSource);
    }

}
