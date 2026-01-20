package com.Aniverse.Common.services.auth.social;

import java.net.URI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.Aniverse.Common.dto.request.login.CreateKakao;
import com.Aniverse.Common.dto.request.login.KakaoCode;
import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.exception.RestemplateExceptionHandler;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;
import com.Aniverse.Common.services.auth.TokenProvider;
import com.Aniverse.Common.services.generation.Generation_Services;
import com.Aniverse.Common.services.redis.Redis_Services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthKakao {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restemplate;
	private User_Mapper mapper;
	private TokenProvider token;
	private Redis_Services Redis;
	private Generation_Services Generation;
	
	@Value("${kakao.client.id}")
	public String client_id;
	
	@Value("${kakao.redirect.url}")
	public String redirect_url;
	
	@Value("${kakao.seckret.key}")
	public String seckret_key;
	
	public AuthKakao(RestTemplate restemplate ,User_Mapper mapper ,
			TokenProvider token, Redis_Services Redis ,Generation_Services Generation) {
		this.Redis = Redis;
		this.mapper = mapper;
		this.token = token;
		this.restemplate =restemplate; 
		this.Generation = Generation;
		
	}
	    @Transactional
		public Map<String, Object> create_kakao(CreateKakao kakao_info){
	
			Map<String, Object> result_data = new HashMap<String, Object>();
			Map<String ,Object> token_infos = new HashMap<String ,Object>();
			Map<String ,Object> infos = new HashMap<String ,Object>();
			Map<String ,Object> data = new HashMap<String ,Object>();
			
			infos.put("id", kakao_info.getId());
			infos.put("email", kakao_info.getEmail());
			infos.put("nickname", kakao_info.getNickname());
			infos.put("profile", kakao_info.getProfile());
			infos.put("thumbnail", kakao_info.getThumnail());
			infos.put("gender", kakao_info.getGender());
			logger.info("카카오 정보 :" + infos);
			
		    String uuid="";
		    uuid = Generation.uuid();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    String createdDate = sdf.format(new Date()); // "2025-01-27"
	
		    infos.put("createdate", createdDate);
		    infos.put("uuid", uuid);
		    infos.put("social_type", "kakao");
		    logger.info("카카오 새 계정 정보 :" + infos.get("id").toString());
		    mapper.kakao_insert(infos);
		    mapper.dtlink(infos.get("email").toString());
			token_infos = token.CreateToken(infos.get("id").toString());
			logger.info("토큰 정보 : "+ token_infos);
			data.put("refresh_token", token_infos.get("refresh_token").toString());
			data.put("access_token", token_infos.get("access_token").toString());
			data.put("id", infos.get("id"));
			data.put("exp" , token_infos.get("exp"));
			logger.info("data :" + data);
		    result_data.put("code", 200);
		    result_data.put("msg", "카카오 계정 생성");
		    result_data.put("data", data);
		    
			return result_data;
			
		}
	
	
	
	
	//카카오 로그아웃
    public Map<String, Object> kakao_logout(HttpServletRequest tokens , String code){
    	Map<String ,Object>response = new HashMap<String ,Object>();
    	logger.info("카카오 로그아웃");
    	
    	return response;
    	
    }
	
	//카카오 로그인
	public Map<String, Object>kakao_login(KakaoCode code){
		logger.info("카카오 로그인 :" + code+ " , " + "client_id : " + client_id) ;
		Map<String ,Object> result_data = new HashMap<String, Object>();
		Map<String ,Object> user_infos = new HashMap<String, Object>();
		Map<String ,Object> response_data = new HashMap<String, Object>();
		Map<String, Object> kakao_info = new HashMap<String, Object>();
		logger.info("시크릿 키 : " + seckret_key);
		
		String apiurl="https://kauth.kakao.com/";
		String path = "oauth/token";
		URI url = null;
		url = UriComponentsBuilder.fromUriString(apiurl).path(path).encode().build().toUri();
		HttpHeaders header = new HttpHeaders();
		header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		restemplate.setErrorHandler(new RestemplateExceptionHandler());// Http통신 예외 처리
		
		MultiValueMap<String ,String>param = new LinkedMultiValueMap<String, String>();
		param.add("grant_type", "authorization_code");
		param.add("client_id", client_id);
		param.add("redirect_uri", redirect_url);
		param.add("code", code.getCode());
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, header);
		ResponseEntity<String> respondatas = restemplate.exchange(url, HttpMethod.POST, request, String.class);
		logger.info("respondata : " + respondatas);
		
		if(respondatas.getStatusCodeValue() == 200) {
		Map<String ,Object> json = new HashMap<String, Object>();
		try {
			json = new ObjectMapper().readValue(respondatas.getBody().toString(), Map.class);
			logger.info("카카오 사용자 정보 조회: " + json);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kakao_info = json;
		user_infos = user_info(json.get("access_token").toString());
		logger.info("사용자 정보 조회 :" + user_infos);

		boolean user_check = false;
		Map<String, Object> insert_info = new HashMap<String ,Object>();
		Map<String ,Object> token_infos = new HashMap<String ,Object>();
		user_check =kakao_user_check(user_infos.get("id").toString()); 
		logger.info("사용자 정보 체크 :" +user_check );
		if(user_check == true) {
			logger.info("사용자 데이터 존재");
			//todu:저장된 사용자 데이터 가져오기
			
			Map<String , Object> user_data = new HashMap<String,Object>();
			user_data = mapper.Kakao_user_info(user_infos.get("id").toString());
			logger.info("정보 조회 결과 :" + user_data);
			
			if(user_data  == null) {
				logger.error("사용자 id가 존재하지 않습니다 ");
				//response_data.put("uuid", "null");
				response_data.put("id", "null");
				response_data.put("email", "null");
				response_data.put("nickname", "null");
				response_data.put("gender", "null");
				response_data.put("profile_img", "null");
				response_data.put("thumbnail_img", "null");
				logger.info("데이터 : " + response_data);
				
				result_data.put("data", response_data);
				result_data.put("msg", "id is not found");
				result_data.put("code", 400);
				throw new CustomException(ErrorCode.USER_NOT_INFO, result_data);
			}
				token_infos = token.CreateToken(user_infos.get("id").toString());
				logger.info("토큰 정보 : "+ token_infos);
				
					response_data.put("exp", token_infos.get("exp"));
					response_data.put("refresh_token", token_infos.get("refresh_token").toString());
					response_data.put("access_token", token_infos.get("access_token").toString());
	                response_data.put("username", user_data.get("username").toString());
	                response_data.put("id", user_infos.get("id").toString());
	                response_data.put("email", user_infos.get("email").toString());
	                response_data.put("nickname", user_data.get("nickname").toString());
	                response_data.put("profile_img", user_data.get("profile_img").toString());
	                
	                result_data.put("msg", "login success");
	                result_data.put("data", response_data);
	                result_data.put("code", 200);
	                result_data.put("Customcode", "00");
					return result_data;
		}
		else {
			logger.info("신규 사용자 ");
			//todu : 신규 데이터 저장
		    Boolean email_check =false;
		    email_check = mapper.Connect_Email(user_infos.get("email").toString());
		    logger.info("이메일 조회 :" +email_check);
		    if(email_check == false) {
		       Map<String, Object> properties = (Map<String, Object>) user_infos.get("properties");
		       logger.info("proper : " + properties);
		    
			    String uuid="";
			    uuid = Generation.uuid();
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String Create_date = sdf.format(date);
			    insert_info.put("uuid", uuid);
			    insert_info.put("id", user_infos.get("id"));
			    insert_info.put("email", user_infos.get("email").toString());
			    insert_info.put("nickname", properties.get("nickname").toString());
			    insert_info.put("gender", user_infos.get("gender").toString());
			    insert_info.put("profile_img", properties.get("profile_image").toString());
			    insert_info.put("thumbnail_img", properties.get("thumbnail_image").toString());
			    insert_info.put("createdate",Create_date);
			    insert_info.put("social_type","kakao");
			    
				mapper.kakao_insert(insert_info);
				token_infos = token.CreateToken(user_infos.get("id").toString());
				logger.info("토큰 정보 : "+ token_infos);
				
				response_data.put("refresh_token", token_infos.get("refresh_token").toString());
				response_data.put("access_token", token_infos.get("access_token").toString());
				response_data.put("id", user_infos.get("id"));
				response_data.put("nickname", properties.get("nickname").toString());
				response_data.put("profile_img", properties.get("profile_image").toString());
				response_data.put("thumbnail_img", properties.get("thumbnail_image").toString());
				response_data.put("exp", token_infos.get("exp"));
				//response_data.put("uuid", uuid);
				
				
				result_data.put("data", response_data);
				result_data.put("msg", "login success");
				result_data.put("code", 201);
				
				return result_data;
		    }
		    else {
		    	logger.info("기존 사용자 정보 조회 카카오 계정과 연동");
		    	Map<String, Object> data = new HashMap<>();
		    	data = mapper.Connecting_Id(user_infos.get("email").toString());
		    	data.put("email", user_infos.get("email").toString());
		    	data.put("gender", user_infos.get("email").toString());
		    	data.put("id", user_infos.get("id").toString());
		    	data.put("nickname", user_infos.get("nickname").toString());
		    	data.put("thumbnail", user_infos.get("thumbnail").toString());
		    	data.put("profile", user_infos.get("profile").toString());
		    	
				throw new CustomException(ErrorCode.KAKAO_NEED_LINK, data);

		    }
			
		}

		}
		else if(respondatas.getStatusCodeValue() == 401) {
			logger.error("인증코드값이 존재하지 않습니다");
			result_data.put("code", 401);
			result_data.put("msg", "code is not foud");
			result_data.put("data","null");
			throw new CustomException(ErrorCode.IS_NOT_CODE, result_data);

			
		}

		return result_data;
	}
	
	
	//카카오 id 체크
	public boolean kakao_user_check(String info) {
		logger.info("카카오 id db조회");
		boolean id_check =false;
		
		id_check =mapper.Kakao_Check_id(info);
		
		return id_check;
	}
	
	//사용자 정보 조회
	public Map<String ,Object> user_info(String token){
		logger.info("엑세스 토큰으로 사용자 정보 조회");

		
		logger.info(" access: " + token);
		
		String apiurl="https://kapi.kakao.com/";
		String path = "v2/user/me";
		URI url = null;
		url = UriComponentsBuilder.fromUriString(apiurl).path(path).encode().build().toUri();
		HttpHeaders header = new HttpHeaders();
		header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		header.add("Authorization", "Bearer " + token);
		logger.info("헤더 : " + header);
		logger.info("url : " + url);
		restemplate.setErrorHandler(new RestemplateExceptionHandler());// Http통신 예외 처리
		
		MultiValueMap<String ,String>param = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(header);
		logger.info("request : " + request);
		ResponseEntity<String> respondatas = restemplate.exchange(url, HttpMethod.GET, request, String.class);
		logger.info("respondata : " + respondatas);
		JSONObject json = new JSONObject();
		Map<String ,Object> properties = new HashMap<String ,Object>();
		if(respondatas.getStatusCodeValue()==200) {
			Map<String, Object> user_info = new HashMap<String ,Object>();
		logger.info("사용자 정보 조회 성공");
		try {
			user_info = new ObjectMapper().readValue(respondatas.getBody().toString(), Map.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		logger.info("사용자 정보 조회 결과값 : " + user_info.get("id").toString());
		JSONObject maptojson = new JSONObject(user_info);
		logger.info("test : " + maptojson.get("kakao_account"));
		JSONObject account = new JSONObject(maptojson.get("kakao_account").toString());
        Object propertie = user_info.get("properties");
        logger.info("properties : " + propertie);
        Map<String ,Object> propertie_data = (Map<String, Object>) propertie;
		String email="";
		email = account.get("email").toString();
		logger.info("properties : " + propertie_data + " , " + "email : "+ email);

		properties.put("properties", propertie_data);
		properties.put("email", email);
		properties.put("gender", account.get("gender").toString());
		properties.put("id", user_info.get("id").toString());
		properties.put("nickname", propertie_data.get("nickname").toString());
		properties.put("profile", propertie_data.get("profile_image").toString());
		properties.put("thumbnail", propertie_data.get("thumbnail_image").toString());
		}
		
		return properties;
	}
	
	//토큰 정보 조회
	public Map<String, Object> token_info(String token){
		logger.info("토큰 정보 조회");
		Map<String, Object> token_info = new HashMap<String, Object>();
		String apiurl="https://kapi.kakao.com/";
		String path = "v1/user/access_token_info";
		URI url = null;
		url = UriComponentsBuilder.fromUriString(apiurl).path(path).encode().build().toUri();
		HttpHeaders header = new HttpHeaders();
		header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		header.add("Authorization", "Bearer " + token);
		logger.info("헤더 : " + header);
		restemplate.setErrorHandler(new RestemplateExceptionHandler());// Http통신 예외 처리
		
		MultiValueMap<String ,String>param = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(header);
		ResponseEntity<String> respondatas = restemplate.exchange(url, HttpMethod.GET, request, String.class);
		logger.info("respondata : " + respondatas);
		if(respondatas.getStatusCodeValue()==200) {
			logger.info("토큰 정보 조회 성공");
			try {
				token_info = new ObjectMapper().readValue(respondatas.getBody().toString(), Map.class);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("토큰 만료 시간 확인 :" + token_info);
		}
		else if(respondatas.getStatusCodeValue() == 401) {
			
			logger.info("엑세스 토큰값이 맞지 않습니다");
			
		}
		
		return token_info;
	}

}
