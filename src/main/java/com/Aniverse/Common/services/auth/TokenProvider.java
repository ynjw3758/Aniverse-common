package com.Aniverse.Common.services.auth;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.services.redis.Redis_Services;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class TokenProvider {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Redis_Services Redis;
	
	public TokenProvider(Redis_Services Redis) {
		this.Redis = Redis;
	}
	
	   @Value("${jwt.security.key}")
	    private String secretKey;
	    int life_time  = 1000*60*24;
	    
	    //최초 access_token, refresh_token  생성
	   public Map<String, Object> CreateToken(String id) {
		   logger.info("로그인 요청 후 토큰 생성");
		   Date create_time =new Date();
		   long finish_time = 1000*60*60;
		   long refresh_time=1000L*60*60*24*30; //리프레쉬 토큰 30일
		   Map<String, Object> tokeninfo = new HashMap<String, Object>();
		   logger.info("타임 :" + refresh_time);
	        String access_Token = "";
	        access_Token = Jwts.builder()
		                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
		                .claim("userid", id)
		                .setIssuer(id) // 토큰발급자(iss)
		                .setIssuedAt(create_time) // 발급시간(iat)
		                .setExpiration(new Date(create_time.getTime()+finish_time))
		                .setSubject("access-token") //  토큰 제목(subject)
		                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes())) // 알고리즘, 시크릿 키
		                .compact();
	        
	        String refresh_Token="";
	        refresh_Token=Jwts.builder()
	     		   .claim("userid", id)
		                .setExpiration(new Date(create_time.getTime()+refresh_time))
		                .setSubject("refresh-token")
		                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes())) // 알고리즘, 시크릿 키
		                .compact();
	        tokeninfo.put("access_token", access_Token);
	        tokeninfo.put("refresh_token", refresh_Token);
	        Map<String , Object> payload_info = new HashMap<String, Object>();
	        
	        payload_info = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(access_Token).getBody();
	        logger.info("페이로드 복호화 결과 : " + payload_info.get("exp").toString());
	
	        Redis.InsertToken(id, refresh_Token, refresh_time);
	
	        tokeninfo.put("exp", payload_info.get("exp").toString());
	        logger.info("토큰 : " + tokeninfo);
	        return tokeninfo;
		   
	   }

}
