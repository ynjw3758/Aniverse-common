package com.Aniverse.Common.services.auth.social;

import java.net.URI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.Aniverse.Common.dto.request.login.CreateNaver;
import com.Aniverse.Common.dto.request.login.NaverCode;
import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.exception.RestemplateExceptionHandler;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;
import com.Aniverse.Common.services.auth.TokenProvider;
import com.Aniverse.Common.services.generation.Generation_Services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

@Service
public class AuthNaver {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${naver.client-id}")
	public String client_id;
	
	@Value("${naver.redirect-url}")
	public String redirect_url;
	
	@Value("${naver.client-secret}")
	public String seckret_key;
	
	private RestTemplate restemplate;
	private User_Mapper mapper;
	private TokenProvider token_info;
	private Generation_Services Generation;
	private TokenProvider token;
	
	public AuthNaver( RestTemplate restemplate, User_Mapper mapper, TokenProvider token_info ,Generation_Services Generation,
			          TokenProvider token) {
		
		this.restemplate = restemplate;
		this.mapper = mapper;
		this.token_info = token_info;
		this.Generation = Generation;
		this.token = token;
	}
	@Transactional
	public Map<String, Object> Create_Naver(CreateNaver info){
		Map<String, Object> result = new HashMap<>();
		Map<String ,Object> token_infos = new HashMap<String ,Object>();
		Map<String ,Object> infos = new HashMap<String ,Object>();
		Map<String ,Object> data = new HashMap<String ,Object>();
		
		infos.put("id", info.getId());
		infos.put("username", info.getUsername());
		infos.put("email", info.getEmail());
		infos.put("nickname", info.getNickname());
		infos.put("profile", info.getProfile());
		infos.put("birthday", info.getBirthday());
		infos.put("gender", info.getGender());
		
	    String uuid="";
	    uuid = Generation.uuid();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String createdDate = sdf.format(new Date()); // "2025-01-27"

	    infos.put("createdate", createdDate);
	    infos.put("uuid", uuid);
	    infos.put("social_type", "naver");
	    logger.info("정보 :" + infos);
	    mapper.naver_insert(infos);
	    mapper.dtlink(infos.get("email").toString());
		token_infos = token.CreateToken(infos.get("id").toString());
		logger.info("토큰 정보 : "+ token_infos);
		data.put("refresh_token", token_infos.get("refresh_token").toString());
		data.put("access_token", token_infos.get("access_token").toString());
		data.put("id", infos.get("id"));
		data.put("exp" , token_infos.get("exp"));
		logger.info("data :" + data);
		result.put("code", 200);
		result.put("msg", "네이버 계정 생성");
		result.put("data", data);
		
		return result;
	}
	
	
	public Map<String, Object> Naver_Login(NaverCode infos , HttpServletRequest tokens){

		Map<String ,Object> result = new HashMap<String, Object>();
		String apiurl="https://nid.naver.com/";
		String path = "oauth2.0/token";
		URI url = null;
		url = UriComponentsBuilder.fromUriString(apiurl).path(path).encode().build().toUri();
		HttpHeaders header = new HttpHeaders();
		header.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		logger.info("클라이언트 아이디 :" + client_id);
		MultiValueMap<String ,String>param = new LinkedMultiValueMap<String, String>();
		param.add("grant_type", "authorization_code");
		param.add("client_id", client_id);
		param.add("client_secret", seckret_key);
		param.add("redirect_uri", redirect_url);
		param.add("code", infos.getCode());
		param.add("state", infos.getState());
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, header);
		ResponseEntity<String> respondatas = restemplate.exchange(url, HttpMethod.POST, request, String.class);
		Map<String ,Object> json = new HashMap<String, Object>();
		Map<String ,Object> User_info = new HashMap<String, Object>();
		logger.info("아니 뭐야  :" + respondatas.getBody());
		if(respondatas.getStatusCodeValue() ==200) {
			
			try {
				json = new ObjectMapper().readValue(respondatas.getBody().toString(), Map.class);
				logger.info("네이버 사용자 정보 조회: " + json);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String access_token = json.get("access_token").toString();
			User_info = GetUser_info(access_token ,tokens);
			logger.info("결과 :" + User_info);
			result =User_info; 
		}
		else {
			logger.error("요청 값이 이상한 경우");
			result.put("code", 401);
			result.put("msg", "login_fail");
			result.put("data", "null");
		}
		//String access_token = 
		
		return result;
		
	}
	
	public Map<String ,Object> GetUser_info(String token, HttpServletRequest headers){
		
		Map<String ,Object> data = new HashMap<String, Object>();
		Map<String ,Object> response_data = new HashMap<String, Object>();
		String apiurl="https://openapi.naver.com/";
		String path = "v1/nid/me";
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
		Map<String ,Object> user_infos =(Map<String, Object>) user_info.get("response"); 
		logger.info("정보 :" +user_infos);
		boolean user_check = false;
		user_check = mapper.Naver_Check_id(user_infos.get("id").toString());
		if(user_check == true) {
			logger.info("기존 사용자");
			Map<String , Object> user_data = new HashMap<String,Object>();
			user_data = mapper.Naver_user_info(user_infos.get("id").toString());
			logger.info("정보 조회 결과 :" + user_data);
			if(user_data  == null) {
				logger.error("사용자 id가 존재하지 않습니다 ");
				response_data.put("uuid", "null");
				response_data.put("id", "null");
				response_data.put("email", "null");
				response_data.put("nickname", "null");
				response_data.put("gender", "null");
				response_data.put("profile_img", "null");
				response_data.put("thumbnail_img", "null");
				logger.info("데이터 : " + response_data);
				
				data.put("data", response_data);
				data.put("msg", "id is not found");
				data.put("code", 400);
				throw new CustomException(ErrorCode.USER_NOT_INFO, data);
			}
			//TODO:토큰 인증
			Map<String ,Object> token_data = new HashMap<String, Object>();	                
	                response_data.put("uuid", user_data.get("connectid").toString());
	                response_data.put("username", user_data.get("username").toString());
	                response_data.put("id", user_data.get("id").toString());
	                response_data.put("email", user_infos.get("email").toString());
	                response_data.put("nickname", user_data.get("nickname").toString());
	                response_data.put("profile_img", user_data.get("profile_img").toString());
	                //response_data.put("exp", token_data.get("exp"));
	                logger.info("데이터 : " + response_data);
	                
	                data.put("msg", "login success");
	                data.put("data", response_data);
					data.put("code", 200);
					data.put("Customcode", "00");
					return data;

		}
		else {
			logger.info("신규 사용자");
		    Boolean email_check =false;
		    email_check = mapper.Connect_Email(user_infos.get("email").toString());
		    logger.info("이메일 조회 :" +email_check);
		    
		    if(email_check == false) {
		    Map<String, Object> insert_info = new HashMap<String ,Object>();
		    Map<String ,Object> token_infos = new HashMap<String ,Object>();
            logger.info("네이버 계정으로 아이디 생성");
		    logger.info("네이버 정보 : " + user_infos);
		    
		    String uuid="";
		    uuid = Generation.uuid();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String Create_date = sdf.format(date);
			
		    insert_info.put("uuid", uuid);
		    insert_info.put("id", user_infos.get("id"));
		    insert_info.put("email", user_infos.get("email").toString());
		    insert_info.put("nickname", user_infos.get("nickname").toString());
		    insert_info.put("name", user_infos.get("name").toString());
		    insert_info.put("gender", user_infos.get("gender").toString());
		    insert_info.put("profile_img", user_infos.get("profile_image").toString());
		    insert_info.put("createdate",Create_date);
		    insert_info.put("mobile",user_infos.get("mobile").toString());
		    insert_info.put("social_type","naver");
		    
			mapper.naver_insert(insert_info);
			
			token_infos = token_info.CreateToken(user_infos.get("id").toString());
			logger.info("토큰 정보 : "+ token_infos);
			
			response_data.put("refresh_token", token_infos.get("refresh_token").toString());
			response_data.put("access_token", token_infos.get("access_token").toString());
			response_data.put("id", user_infos.get("id"));
			response_data.put("nickname", properties.get("nickname").toString());
			response_data.put("profile_img", user_infos.get("profile_image").toString());
			response_data.put("exp", token_infos.get("exp"));
			response_data.put("uuid", uuid);
			
			
			data.put("data", response_data);
			data.put("msg", "login success");
			data.put("code", 201);
			
			return data;
		    }
		    else {
		    	logger.info("네이버 등록된 이메일과 같은 메일 존재");
		    	Map<String, Object> before_infos = new HashMap<String ,Object>();
                logger.info("보낼 데이터 :" +user_infos );
		    	before_infos = mapper.Connecting_Id(user_infos.get("email").toString());
		    	before_infos.put("username", user_infos.get("name").toString());
		    	before_infos.put("email", user_infos.get("email").toString());
		    	before_infos.put("nickname", user_infos.get("nickname").toString());
		    	before_infos.put("gender", user_infos.get("gender").toString());
		    	before_infos.put("profile", user_infos.get("profile_image").toString());
		    	before_infos.put("phone", user_infos.get("mobile").toString());
		    	before_infos.put("birthday", user_infos.get("birthyear").toString()+user_infos.get("birthday").toString());
		    	before_infos.put("newid", user_infos.get("id").toString());
				
				throw new CustomException(ErrorCode.NAVER_NEED_LINK, before_infos);
		    }
			
		}
		
		
		}
		else {
			logger.error("엑세스 토큰값 이상");
			data.put("code", 401);
			data.put("errorcode", "00");
			data.put("msg", "네이버 코드 및 stat값 이상");
			data.put("data", "null");
			
		}
		
				
	 return data;	
	}
	
	public boolean user_check(String info) {
		logger.info("카카오 id db조회");
		boolean id_check =false;
		
		//id_check =mapper.Check_id(info);
		
		return id_check;
	}



}
