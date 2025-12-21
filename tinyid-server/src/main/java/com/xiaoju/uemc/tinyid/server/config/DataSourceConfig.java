package com.xiaoju.uemc.tinyid.server.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author du_imba
 */
@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private Environment environment;
    private static final String SEP = ",";

    @Bean
    public DataSource getDynamicDataSource() {
        DynamicDataSource routingDataSource = new DynamicDataSource();
        List<String> dataSourceKeys = new ArrayList<>();
        
        String names = environment.getProperty("datasource.tinyid.names");
        String dataSourceType = environment.getProperty("datasource.tinyid.type");

        Map<Object, Object> targetDataSources = new HashMap<>(4);
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDataSourceKeys(dataSourceKeys);
        
        // 多个数据源
        if (names == null || names.isEmpty()) {
            logger.error("datasource.tinyid.names is empty");
            throw new IllegalStateException("datasource.tinyid.names is empty");
        }
        for (String name : names.split(SEP)) {
            DataSource dataSource = buildDataSource(dataSourceType, name);
            targetDataSources.put(name, dataSource);
            dataSourceKeys.add(name);
        }
        return routingDataSource;
    }

    private DataSource buildDataSource(String dataSourceType, String name) {
        String prefix = "datasource.tinyid." + name + ".";
        
        String driverClassName = environment.getProperty(prefix + "driver-class-name");
        String url = environment.getProperty(prefix + "url");
        String username = environment.getProperty(prefix + "username");
        String password = environment.getProperty(prefix + "password");

        try {
            Class<? extends DataSource> type = HikariDataSource.class;
            if (dataSourceType != null && !dataSourceType.trim().isEmpty()) {
                type = (Class<? extends DataSource>) Class.forName(dataSourceType);
            }

            DataSource dataSource = DataSourceBuilder.create()
                    .driverClassName(driverClassName)
                    .url(url)
                    .username(username)
                    .password(password)
                    .type(type)
                    .build();

            // 配置 HikariCP 连接池属性
            if (dataSource instanceof HikariDataSource hikariDataSource) {
                String maxActive = environment.getProperty(prefix + "maxActive");
                if (maxActive != null) {
                    hikariDataSource.setMaximumPoolSize(Integer.parseInt(maxActive));
                }
                String testOnBorrow = environment.getProperty(prefix + "testOnBorrow");
                if ("true".equalsIgnoreCase(testOnBorrow)) {
                    hikariDataSource.setConnectionTestQuery("SELECT 1");
                }
            }

            return dataSource;
        } catch (ClassNotFoundException e) {
            logger.error("buildDataSource error", e);
            throw new IllegalStateException(e);
        }
    }
}
