package com.hef.myreadwritesepshardingspherejdbcv2.conf;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.rule.ReadwriteSplittingDataSourceRule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @Date 2022/1/16
 * @Author lifei
 */
@Configuration
public class DataSourceConf {

    private static final String readWriteSplittingName = "demo_read_write_split_ds";
    private static final String writeDataSourceName = "demo_write_ds";

    private static final String readDs1 = "demo_read_ds_1";
    private static final String readDs2 = "demo_read_ds_2";
    private static final String[] readDataSourceNames = {readDs1, readDs2};

    private static final String loadBalancerName = "demo_weight_lb";

    private static final String shardingShpereAlgorithmType = "WEIGHT";

    private static final String PRO_SQL_SHOW = "sql-show";


    /**
     * 创建读写分离数据库
     * @return
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource() throws SQLException {
        ReadwriteSplittingDataSourceRuleConfiguration dataSourceRuleConfiguration =
                new ReadwriteSplittingDataSourceRuleConfiguration(readWriteSplittingName, writeDataSourceName,
                        writeDataSourceName, getReadDataSourceNames(), loadBalancerName);
        // 权重选择
        Properties props = new Properties();
        props.put(readDs1, "2");
        props.put(readDs2, "1");
        ShardingSphereAlgorithmConfiguration shardingSphereAlgorithmConfiguration =
                new ShardingSphereAlgorithmConfiguration(shardingShpereAlgorithmType, props);
        // 配置路由规则
        Map<String, ShardingSphereAlgorithmConfiguration> shardingSphereAlgorithmConfigurationMap = new HashMap<>();
        shardingSphereAlgorithmConfigurationMap.put(loadBalancerName, shardingSphereAlgorithmConfiguration);
        RuleConfiguration readwriteSplittingRuleConfiguration = new ReadwriteSplittingRuleConfiguration(Collections.singleton(dataSourceRuleConfiguration), shardingSphereAlgorithmConfigurationMap);
        Properties properties = new Properties();
        properties.put(PRO_SQL_SHOW, String.valueOf(true));
        // 创建
        return ShardingSphereDataSourceFactory.createDataSource(createDataSource(), Collections.singleton(readwriteSplittingRuleConfiguration), properties);
    }

    /**
     * 创建读数据库的名称
     * @return
     */
    private List<String> getReadDataSourceNames() {
        return new ArrayList<>(Arrays.asList(readDataSourceNames));
    }

    /**
     * 创建读写数据库实例
     * @return
     */
    private Map<String, DataSource> createDataSource() {
        Map<String, DataSource> result = new HashMap<>(3, 1);
        result.put(writeDataSourceName, dataSourceWrite());
        result.put(readDs1, dataSourceRead01());
        result.put(readDs2, dataSourceRead02());
        return result;
    }

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


}
