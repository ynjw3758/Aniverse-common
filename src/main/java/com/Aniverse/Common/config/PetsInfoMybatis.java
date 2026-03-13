package com.Aniverse.Common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(basePackages ="com.Aniverse.Common.mapper.Pets", sqlSessionFactoryRef = "petsSqlSessionFactory")
public class PetsInfoMybatis {
	

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.pets.hikari")
    public HikariConfig petsHikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = "petsDataSource")
    public DataSource petsDataSource() {
        return new HikariDataSource(petsHikariConfig());
    }

    @Bean(name = "petsSqlSessionFactory")
    public SqlSessionFactory petsSqlSessionFactory(
            @Qualifier("petsDataSource") DataSource dataSource,
            ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        // ⚠️ mapper xml 분리 추천
        sqlSessionFactoryBean.setMapperLocations(
                applicationContext.getResources("classpath:/mappers/pets/*.xml")
        );

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "petsSessionTemplate")
    public SqlSessionTemplate petsSqlSessionTemplate(
            @Qualifier("petsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {

        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
