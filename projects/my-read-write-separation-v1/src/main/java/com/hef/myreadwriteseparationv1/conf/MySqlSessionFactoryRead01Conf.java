package com.hef.myreadwriteseparationv1.conf;

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
 * @Date 2022/1/15
 * @Author lifei
 */
@Configuration
@MapperScan(basePackages = {"com.hef.myreadwriteseparationv1.dao.dao02"},
        sqlSessionFactoryRef = "sqlSessionFactoryRead01",
        sqlSessionTemplateRef = "sqlSessionTemplateRead01")
public class MySqlSessionFactoryRead01Conf {

    private static final String[] LOCAL_MAPPERS = {"classpath:mybatis/mapper02/**/*.xml"};

    @Bean(name = "sqlSessionFactoryRead01")
    public SqlSessionFactory sqlSessionFactoryRead01(@Qualifier("dataSourceRead01") DataSource dataSource) throws Exception {
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

    @Bean(name = "sqlSessionTemplateRead01")
    public SqlSessionTemplate sqlSessionTemplateRead01(@Qualifier("sqlSessionFactoryRead01") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

}
