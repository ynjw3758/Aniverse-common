package com.Aniverse.Common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {
	
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	//SecurityFilterChain는 추후에 설정할 예정
	
	  @Bean
	  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		  /*
	    http.csrf(csrf -> csrf.disable());
	    http.authorizeHttpRequests(auth -> auth
	        .requestMatchers("/.well-known/**", "/login").permitAll() // JWKS, 로그인 허용
	        .anyRequest().authenticated()
	    );
	    http.httpBasic(Customizer.withDefaults()); // 필요시
	    return http.build();
	    */
		  return http
			      .csrf(csrf -> csrf.disable())
			      .httpBasic(b -> b.disable())            // ★ Basic 끄기(팝업 원인 제거)
			      .formLogin(f -> f.disable())            // 폼 로그인 안 씀
			      .authorizeHttpRequests(auth -> auth
			          .requestMatchers(
			              "/Pets-social/Common/**",       // ★ 로그인 전 공개 API 전부 허용
			              "/.well-known/**"
			          ).permitAll()
			          .anyRequest().authenticated()       // (혹시 나중용 보호 경로가 있으면 여기서)
			      )
			      .build();  
	  }



}
