package com.Aniverse.Common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer{
	
	/*
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:3000")  // 프론트 주소
				.allowedMethods("*")
				.allowedHeaders("*")
				.exposedHeaders("Authorization")
				.allowCredentials(true);  // 쿠키 사용할 경우
	}
	*/
	
}
