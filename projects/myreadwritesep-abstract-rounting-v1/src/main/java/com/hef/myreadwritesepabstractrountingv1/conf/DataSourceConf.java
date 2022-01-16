package com.hef.myreadwritesepabstractrountingv1.conf;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date 2022/1/16
 * @Author lifei
 */
@Configuration
public class DataSourceConf {

    @Bean(name = "dataSourceWrite")
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    public DataSource dataSourceWrite() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceRead01")
    @ConfigurationProperties(prefix = "spring.datasource.druid.read01")
    public DataSource dataSourceRead01() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceRead02")
    @ConfigurationProperties(prefix = "spring.datasource.druid.read02")
    public DataSource dataSourceRead02() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 动态切换的数据源
     * @param dataSourceWrite
     * @param dataSourceRead01
     * @param dataSourceRead02
     * @return
     */
    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier("dataSourceWrite") DataSource dataSourceWrite,
                                               @Qualifier("dataSourceRead01") DataSource dataSourceRead01,
                                               @Qualifier("dataSourceRead02") DataSource dataSourceRead02) {
        Map<Object, Object> targetDataSourceMap = new HashMap<>();
        targetDataSourceMap.put(DataSourceKey.READ01_KEY, dataSourceRead01);
        targetDataSourceMap.put(DataSourceKey.READ02_KEY, dataSourceRead02);
        DynamicDataSource dynamicDataSource = new DynamicDataSource(dataSourceWrite, targetDataSourceMap);
        return dynamicDataSource;
    }
}
