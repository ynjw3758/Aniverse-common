package com.Aniverse.Common.services.auth;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.dto.request.common.RequestUtil;
import com.Aniverse.Common.dto.request.login.SessionRedisDto;
import com.Aniverse.Common.services.redis.Redis_Services;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class TokenProvider {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Redis_Services Redis;
	private RequestUtil util;
	
	public TokenProvider(Redis_Services Redis ,RequestUtil util) {
		this.Redis = Redis;
		this.util = util;
	}
	
	   @Value("${jwt.security.key}")
	    private String secretKey;
	    int life_time  = 1000*60*24;
	    
	    //최초 access_token, refresh_token  생성
	   public Map<String, Object> CreateToken(String id,HttpServletRequest request,
			   String LoginType ,String Client) {
		   Map<String, Object>tokeninfo = new HashMap<>();
		   logger.info("로그인 요청 후 토큰 생성");
		   Date create_time = new Date();
		    long access_time = 1000L * 60 * 15;          // access 15분
		    long refresh_time = 1000L * 60 * 60 * 24 * 14; // refresh 14일

		    Date accessExpireDate = new Date(create_time.getTime() + access_time);
		    Date refreshExpireDate = new Date(create_time.getTime() + refresh_time);

		    byte[] signingKey = Base64.getEncoder().encode(secretKey.getBytes());

		    String access_Token = Jwts.builder()
		            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
		            .claim("userid", id)
		            .setIssuer(id)
		            .setIssuedAt(create_time)
		            .setExpiration(accessExpireDate)
		            .setSubject("access-token")
		            .signWith(SignatureAlgorithm.HS256, signingKey)
		            .compact();

		    String refresh_Token = Jwts.builder()
		            .claim("userid", id)
		            .setIssuer(id)
		            .setIssuedAt(create_time)
		            .setExpiration(refreshExpireDate)
		            .setSubject("refresh-token")
		            .signWith(SignatureAlgorithm.HS256, signingKey)
		            .compact();

		    Claims payload_info = Jwts.parser()
		            .setSigningKey(signingKey)
		            .parseClaimsJws(access_Token)
		            .getBody();


	        String sessionId = UUID.randomUUID().toString();
	        String ip = util.getClientIp(request);
	        String userAgent = request.getHeader("User-Agent");
	        String SessionId="";

	        SessionRedisDto session = new SessionRedisDto();
	        session.setSessionId(sessionId);
	        session.setUserId(id);
	        session.setAccessToken(access_Token);
	        session.setRefreshToken(refresh_Token);
	        session.setAccessExp(accessExpireDate.getTime());
	        session.setRefreshExp(refreshExpireDate.getTime());
	        session.setLoginType(LoginType);     // LOCAL, KAKAO
	        session.setClientType(Client);   // WEB, APP, DESKTOP
	        session.setIp(ip);
	        session.setUserAgent(userAgent);
	        session.setCreatedAt(create_time.getTime());
	        session.setLastAccessAt(create_time.getTime());
	        Redis.saveSession(session);
	        tokeninfo.put("access_token", access_Token);
	        tokeninfo.put("refresh_token", refresh_Token);
	        tokeninfo.put("exp", payload_info.get("exp").toString());
	        tokeninfo.put("sessionId", sessionId);
	        //Redis.InsertToken(id, refresh_Token, refresh_time);
	        return tokeninfo;
		   
	   }

}
