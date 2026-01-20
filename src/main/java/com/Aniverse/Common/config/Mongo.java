package com.Aniverse.Common.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class Mongo {
	
	  // Upload DB (기본)
    @Value("${spring.data.mongodb.uri}")
    private String uploadUri;

    @Value("${spring.data.mongodb.database}")
    private String uploadDb;

    // Noti DB (추가)
    @Value("${spring.data.mongodb.noti.uri}")
    private String notiUri;

    @Value("${spring.data.mongodb.noti.database}")
    private String notiDb;
    
    @Bean(name = "uploadMongoTemplate")
    public MongoTemplate uploadMongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(uploadMongoClient(), uploadDb));
    }

    @Bean(name = "notiMongoTemplate")
    public MongoTemplate notiMongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(notiMongoClient(), notiDb));
    }

    @Bean
    public MongoClient uploadMongoClient() {
        ConnectionString connectionString = new ConnectionString(uploadUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoClient notiMongoClient() {
        ConnectionString connectionString = new ConnectionString(notiUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(settings);
    }

}
