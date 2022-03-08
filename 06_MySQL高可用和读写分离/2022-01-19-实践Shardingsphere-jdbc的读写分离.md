# 实践Shardingsphere-jdbc读写分离

[toc]

## 一、shardingsphere-jdbc概述

通过shardingsphere-jdbc能够很容易实现读写分离。

## 二、示例

先搭建一个mysql集群(一主两从)，半同步复制：[mysql 半同步复制，三个节点](https://github.com/hefrankeleyn/JAVARebuild/blob/main/Week_07_MySQL%E9%AB%98%E5%8F%AF%E7%94%A8%E5%92%8C%E8%AF%BB%E5%86%99%E5%88%86%E7%A6%BB/2022-01-15-mysql%E5%8D%8A%E5%90%8C%E6%AD%A5%E5%A4%8D%E5%88%B6%E6%90%AD%E5%BB%BA_%E4%B8%80%E4%B8%BB%E4%B8%A4%E4%BB%8E.md)。

完整的代码：[myreadwritesep-shardingsphere-jdbc-v2](https://github.com/hefrankeleyn/JAVARebuild/tree/main/projects/myreadwritesep-shardingsphere-jdbc-v2)

### 2.1 pom文件

```xml
	<properties>
		<java.version>1.8</java.version>
		<mybatis-spring-boot-starter.version>2.2.1</mybatis-spring-boot-starter.version>
		<shardingsphere-jdbc-core.version>5.0.0</shardingsphere-jdbc-core.version>
		<druid-spring-boot-starter.version>1.1.18</druid-spring-boot-starter.version>
		<commons-lang3.version>3.9</commons-lang3.version>
		<commons-collections4.version>4.2</commons-collections4.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-log4j2</artifactId>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>${mybatis-spring-boot-starter.version}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.shardingsphere</groupId>
			<artifactId>shardingsphere-jdbc-core</artifactId>
			<version>${shardingsphere-jdbc-core.version}</version>
		</dependency>
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid-spring-boot-starter</artifactId>
			<version>${druid-spring-boot-starter.version}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>${commons-lang3.version}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-collections4</artifactId>
			<version>${commons-collections4.version}</version>
		</dependency>

		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
```

### 2.2 配置读写分离数据源

```java
package com.hef.myreadwritesepshardingspherejdbcv2.conf;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
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

    private static final String WEIGHT_ALGORITHM_TYPE = "WEIGHT";

    private static final String PRO_SQL_SHOW = "sql-show";


    /**
     * 创建读写分离数据库
     * @return
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource() throws SQLException {
        // 读写分离数据库的路由配置
        ReadwriteSplittingDataSourceRuleConfiguration dataSourceRuleConfiguration =
                new ReadwriteSplittingDataSourceRuleConfiguration(readWriteSplittingName, writeDataSourceName,
                        writeDataSourceName, getReadDataSourceNames(), loadBalancerName);
        // 权重选择
        Properties props = new Properties();
        props.put(readDs1, "2");
        props.put(readDs2, "1");
        ShardingSphereAlgorithmConfiguration shardingSphereAlgorithmConfiguration =
                new ShardingSphereAlgorithmConfiguration(WEIGHT_ALGORITHM_TYPE, props);
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

```

### 2.3 配置权重算法，使用JAVA SPI

[JAVA SPI服务发现](https://github.com/hefrankeleyn/JAVARebuild/blob/main/Week_07_MySQL%E9%AB%98%E5%8F%AF%E7%94%A8%E5%92%8C%E8%AF%BB%E5%86%99%E5%88%86%E7%A6%BB/2022-01-19-JAVA%20SPI%E6%9C%8D%E5%8A%A1%E5%8F%91%E7%8E%B0.md)。

```java
package com.hef.myreadwritesepshardingspherejdbcv2.conf;

import org.apache.shardingsphere.readwritesplitting.spi.ReplicaLoadBalanceAlgorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Date 2022/1/18
 * @Author lifei
 */
public class WeightReplicaLoadBalanceAlgorithm implements ReplicaLoadBalanceAlgorithm {

    private static final double ACCURACY_THRESHOLD = 0.0001;

    private static final ConcurrentHashMap<String, double[]> WEIGHT_MAP = new ConcurrentHashMap<>();

    private Properties props = new Properties();

    @Override
    public String getType() {
        return "WEIGHT";
    }

    @Override
    public String getDataSource(final String name, final String writeDataSourceName, final List<String> readDataSourceNames) {
        double[] weight = WEIGHT_MAP.containsKey(name) ? WEIGHT_MAP.get(name) : initWeight(readDataSourceNames);
        WEIGHT_MAP.putIfAbsent(name, weight);
        return getDataSourceName(readDataSourceNames, weight);
    }

    private String getDataSourceName(final List<String> readDataSourceNames, final double[] weight) {
        double randomWeight = ThreadLocalRandom.current().nextDouble(0, 1);
        int index = Arrays.binarySearch(weight, randomWeight);
        if (index < 0) {
            index = -index - 1;
            return index < weight.length && randomWeight < weight[index] ? readDataSourceNames.get(index) : readDataSourceNames.get(readDataSourceNames.size() - 1);
        } else {
            return readDataSourceNames.get(index);
        }
    }

    private double[] initWeight(final List<String> readDataSourceNames) {
        double[] weights = getWeights(readDataSourceNames);
        if (weights.length != 0 && Math.abs(weights[weights.length - 1] - 1.0D) >= ACCURACY_THRESHOLD) {
            throw new IllegalStateException("The cumulative weight is calculated incorrectly, and the sum of the probabilities is not equal to 1.");
        }
        return weights;
    }

    private double[] getWeights(final List<String> readDataSourceNames) {
        double[] exactWeights = new double[readDataSourceNames.size()];
        int index = 0;
        double sum = 0D;
        for (String readDataSourceName : readDataSourceNames) {
            double weight = getWeightValue(readDataSourceName);
            exactWeights[index++] = weight;
            sum += weight;
        }
        for (int i = 0; i < index; i++) {
            if (exactWeights[i] <= 0) {
                continue;
            }
            exactWeights[i] = exactWeights[i] / sum;
        }
        return calcWeight(exactWeights);
    }

    private double[] calcWeight(final double[] exactWeights) {
        double[] weights = new double[exactWeights.length];
        double randomRange = 0D;
        for (int i = 0; i < weights.length; i++) {
            weights[i] = randomRange + exactWeights[i];
            randomRange += exactWeights[i];
        }
        return weights;
    }

    private double getWeightValue(final String readDataSourceName) {
        Object weightObject = props.get(readDataSourceName);
        if (weightObject == null) {
            throw new IllegalStateException("Read database access weight is not configured：" + readDataSourceName);
        }
        double weight;
        try {
            weight = Double.parseDouble(weightObject.toString());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Read database weight configuration error, configuration parameters:" + weightObject.toString());
        }
        if (Double.isInfinite(weight)) {
            weight = 10000.0D;
        }
        if (Double.isNaN(weight)) {
            weight = 1.0D;
        }
        return weight;
    }

    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void setProps(Properties props) {
        this.props = props;
    }
}

```

创建文件：`src/main/resources/META-INF/services/org.apache.shardingsphere.readwritesplitting.spi.ReplicaLoadBalanceAlgorithm`，里面内容如下：

```
com.hef.myreadwritesepshardingspherejdbcv2.conf.WeightReplicaLoadBalanceAlgorithm
```

### 2.4 配置mybatis

```java
package com.hef.myreadwritesepshardingspherejdbcv2.conf;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Date 2022/1/16
 * @Author lifei
 */
@MapperScan(basePackages = {"com.hef.myreadwritesepshardingspherejdbcv2.dao"},
        sqlSessionFactoryRef = "sqlSessionFactory", sqlSessionTemplateRef = "sqlSessionTemplate")
@Configuration
public class RepositoryConf {

    private static final String[] LOCAL_MAPPERS = {"classpath:mybatis/**/*.xml"};

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        List<Resource> resourceList = new ArrayList<>();
        for (String localMapper : LOCAL_MAPPERS) {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(localMapper);
            resourceList.addAll(Arrays.asList(resources));
        }
        sqlSessionFactoryBean.setMapperLocations(resourceList.toArray(new Resource[0]));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

```

### 2.5 测试读写分离

```java
    @Test
    public void findModelInfoByModelTypeTest() {
        ModelInfo modelInfo = modelInfoService.findModelInfoByModelType("dynamic-type");
        System.out.println(modelInfo);
    }
```

### 2.6 发现问题

mybatis的xml文件中，SQL语句不能写库名。如果写上会报错，提示schema找不到。