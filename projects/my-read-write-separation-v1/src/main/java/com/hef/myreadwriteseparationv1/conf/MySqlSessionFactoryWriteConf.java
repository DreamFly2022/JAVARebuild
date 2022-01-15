package com.hef.myreadwriteseparationv1.conf;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Date 2022/1/15
 * @Author lifei
 */
@Configuration
@MapperScan(basePackages = {"com.hef.myreadwriteseparationv1.dao.dao01"},
        sqlSessionFactoryRef = "sqlSessionFactoryWrite",
        sqlSessionTemplateRef = "sqlSessionTemplateWrite")
public class MySqlSessionFactoryWriteConf {

    private static final String[] LOCAL_MAPPERS = {"classpath:mybatis/mapper01/**/*.xml"};

    @Bean(name = "sqlSessionFactoryWrite")
    public SqlSessionFactory sqlSessionFactoryWrite(@Qualifier("dataSourceWrite") DataSource dataSource) throws Exception {
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

    @Bean(name = "sqlSessionTemplateWrite")
    public SqlSessionTemplate sqlSessionTemplateWrite(@Qualifier("sqlSessionFactoryWrite") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

}
