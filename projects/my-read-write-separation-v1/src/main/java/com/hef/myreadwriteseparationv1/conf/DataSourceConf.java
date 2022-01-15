package com.hef.myreadwriteseparationv1.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Date 2022/1/15
 * @Author lifei
 */
@Configuration
public class DataSourceConf {

    @Resource
    private MyDataConf myDataConf;

    @Bean(name = "dataSourceWrite")
    public DataSource dataSourceWrite() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(myDataConf.getUrlWrite());
        dataSource.setUsername(myDataConf.getUsername());
        dataSource.setPassword(myDataConf.getPassword());
        return dataSource;
    }
    @Bean(name = "dataSourceRead01")
    public DataSource dataSourceRead01() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(myDataConf.getUrlRead01());
        dataSource.setUsername(myDataConf.getUsername());
        dataSource.setPassword(myDataConf.getPassword());
        return dataSource;
    }
    @Bean(name = "dataSourceRead02")
    public DataSource dataSourceRead02() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(myDataConf.getUrlRead02());
        dataSource.setUsername(myDataConf.getUsername());
        dataSource.setPassword(myDataConf.getPassword());
        return dataSource;
    }
}
