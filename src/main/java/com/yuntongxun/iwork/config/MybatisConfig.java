package com.yuntongxun.iwork.config;

import com.yuntongxun.iwork.dao.mybatis.interceptor.MySqlInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @program: iwork-dao
 * @description: mybatis
 * @author: liugang
 * @create: 2020-05-20 17:15
 **/
@Configuration
public class MybatisConfig {

    private static final String MYSQL_DATASOURCE = "DataSource";

    private static final String MAPPER_RESOURCES = "classpath:/spring/ds/mybatis/mappers/*Mapper.xml";

    @ConfigurationProperties(prefix ="spring.datasource.hikari")
    @Bean(name = "hikariConfig")
    public HikariConfig hikariConfig(){
        return new HikariConfig();
    }

    @Bean(name = MYSQL_DATASOURCE)
    public DataSource dataSource(@Qualifier("hikariConfig") @Autowired HikariConfig config) {
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        return hikariDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(MYSQL_DATASOURCE) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_RESOURCES));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(true);
        configuration.setLazyLoadingEnabled(false);
        configuration.setAggressiveLazyLoading(true);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setPlugins(new Interceptor[]{
                new MySqlInterceptor()
        });
        return sqlSessionFactory.getObject();
    }
}
