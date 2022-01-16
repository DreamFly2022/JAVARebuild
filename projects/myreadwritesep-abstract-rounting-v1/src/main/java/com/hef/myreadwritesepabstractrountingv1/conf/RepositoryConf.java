package com.hef.myreadwritesepabstractrountingv1.conf;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Date 2022/1/16
 * @Author lifei
 */
@MapperScan(basePackages = {"com.hef.myreadwritesepabstractrountingv1.dao"},
        sqlSessionFactoryRef = "sqlSessionFactory", sqlSessionTemplateRef = "sqlSessionTemplate")
@Configuration
public class RepositoryConf {

    private static final String[] LOCAL_MAPPERS = {"classpath:mybatis/**/*.xml"};

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
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
