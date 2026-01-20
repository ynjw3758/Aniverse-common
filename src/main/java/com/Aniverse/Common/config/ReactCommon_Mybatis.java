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
@MapperScan(basePackages = "com.Aniverse.Common.mapper.ReactCommon", sqlSessionFactoryRef = "ReactCommonSqlSessionFactory")
public class ReactCommon_Mybatis {
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.reactcommon.hikari")
	public HikariConfig hearthikariConfig() {
		return new HikariConfig();
	}

	@Bean(name ="ReactCommonDataSource")
	public DataSource dataSource() {
		
		return new HikariDataSource(hearthikariConfig());
	}
	

	@Bean(name = "ReactCommonSqlSessionFactory")
	public SqlSessionFactory ReactCommonSqlSessionFactory(@Qualifier("ReactCommonDataSource") DataSource DataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(DataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/ReactCommon.xml"));
		return sqlSessionFactoryBean.getObject();
	}

    @Bean(name = "ReactCommonSessionTemplate")
    public SqlSessionTemplate ReactCommonSqlSessionTemplate(@Qualifier("ReactCommonSqlSessionFactory") SqlSessionFactory firstSqlSessionFactory) {
        return new SqlSessionTemplate(firstSqlSessionFactory);
    }

}
