package com.Aniverse.Common.services.auth;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.dto.request.login.LoginRequest;
import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LoginService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private PasswordEncoder passwordEncoder; 
	private User_Mapper userinfo;
	private TokenProvider token;
	
	public LoginService(PasswordEncoder passwordEncoder, User_Mapper userinfo,
			            TokenProvider token ) {
		this.passwordEncoder = passwordEncoder;
		this.token = token;
		this.userinfo = userinfo;
	}
	
	public Map<String, Object> Login(LoginRequest infos ,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> UserInfo = new HashMap<>();
		logger.info("로그인 정보 :" + infos.getId());
		String accessToken = request.getHeader("Authorization");
		if(accessToken == null || accessToken.isEmpty()) {
			logger.info("최초 로그인 ");
			UserInfo =userinfo.getUserLoginInfo(infos.getId());
			logger.info("정보 :" +UserInfo );
			if(UserInfo == null) {
				logger.error("아이디가 존재하지 않음");
				
				throw new CustomException(ErrorCode.INVALID_ID_OR_PASSWORD,null) ;
			}else {
				boolean pass_check= false;
				pass_check=passwordEncoder.matches(infos.getPassword(), 
						UserInfo.get("password").toString());
				if(pass_check == true) {
					Map<String, Object> TokenInfos = new HashMap<>();
					TokenInfos = token.CreateToken(infos.getId());
					logger.info("토큰 생성 정보 :" + TokenInfos);
					
					Map<String, Object> data = new HashMap<>();
					data.put("id", infos.getId());
					data.put("NickName", UserInfo.get("nickname").toString());
					data.put("access_token", TokenInfos.get("access_token").toString());
					data.put("refresh_token", TokenInfos.get("refresh_token").toString());
					data.put("exp", TokenInfos.get("exp").toString());
					
					result.put("code", 200);
					result.put("msg", "로그인 성공 ");
					result.put("data", data);
					
					
					
				}else {
					logger.error("비밀번호 틀림");
					Map<String, Object> body = new HashMap<>();
					body.put("id", UserInfo.get("id").toString());
					throw new CustomException(ErrorCode.INVALID_ID_OR_PASSWORD ,body);
					
				}
				
				
			}
			
		}else {
			 logger.info("기존 로그인 정보 있음");
		}
		
		return result;
	}

}
