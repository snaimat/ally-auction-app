package com.ally.demo.api.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:sql_queries.properties")
@ConfigurationProperties(prefix = "sql.query")
@Data
public class SqlQueryConfigProperties {

    private String SQL_SELECT_AUCTION_ITEM;
    private String SQL_SELECT_AUCTION_ITEMS;
    private String SQL_SELECT_ITEMS;
    private String SQL_INSERT_ITEMS;
    private String SQL_UPDATE_AUCTION_ITEM;
    private String SQL_INSERT_BIDS;
    private String SQL_SELECT_HIGHEST_BIDDER;
}
