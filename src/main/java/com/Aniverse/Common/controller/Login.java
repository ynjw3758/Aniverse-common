package com.Aniverse.Common.controller;

import org.slf4j.Logger;



import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.Aniverse.Common.dto.request.login.CreateKakao;
import com.Aniverse.Common.dto.request.login.CreateNaver;
import com.Aniverse.Common.dto.request.login.KakaoCode;
import com.Aniverse.Common.dto.request.login.LoginRequest;
import com.Aniverse.Common.dto.request.login.NaverCode;
import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.services.auth.LoginService;
import com.Aniverse.Common.services.auth.social.AuthKakao;
import com.Aniverse.Common.services.auth.social.AuthNaver;

import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.json.JSONObject;


@RestController
@RequestMapping("/Pets-social/Common/login")
public class Login {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private LoginService auth;
	private AuthKakao kakao;
	private AuthNaver naver;
	
	public Login(LoginService auth ,AuthKakao kakao ,AuthNaver naver) {
		this.auth = auth;
		this.kakao = kakao;
		this.naver = naver;
	}
	
	//일반 로그인
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest  login_info,
			HttpServletResponse response, HttpServletRequest quest) {
		Map<String, Object> result_data = new HashMap<String, Object>();
			result_data = auth.Login(login_info ,quest);
			
			HttpHeaders headers = new HttpHeaders();
			JSONObject res_data = new JSONObject(result_data);
			JSONObject ACCESS_TOKEN = new JSONObject(res_data.get("data").toString());
			response.addHeader("Authorization", ACCESS_TOKEN.get("access_token").toString());
			
		    ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", ACCESS_TOKEN.get("refresh_token").toString())
		            .httpOnly(true)
		            .secure(false) // HTTPS 환경에서만 사용할 경우
		            .path("/")
		            .maxAge(/*30 * 24 * 60 * 60*/60 * 5) // 30일
		            .sameSite("Lax") // 또는 "Lax"
		            .build();
	
		    response.addHeader("Set-Cookie", refreshTokenCookie.toString());
			return ResponseEntity.status(HttpStatus.OK).body(result_data);
	}
	
	@GetMapping("/oauth/kakao")
	public ResponseEntity<Map<String, Object>> Kakao_login(@ModelAttribute @Valid KakaoCode code,
			HttpServletResponse response) {
		Map<String, Object> result_data = new HashMap<String, Object>();
		logger.info("code : " + code);
		result_data = kakao.kakao_login(code);
		logger.info("결과값 : " + result_data);

		if (result_data.get("code").equals(201)) {
			logger.info("신규 회원 로그인 성공");
			JSONObject res_data = new JSONObject(result_data);
			logger.info("data : " + res_data.get("data"));
			JSONObject ACCESS_TOKEN = new JSONObject(res_data.get("data").toString());
			logger.info("access : " + ACCESS_TOKEN.get("access_token").toString());

			response.addHeader("Authorization", ACCESS_TOKEN.get("access_token").toString());
		    ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", ACCESS_TOKEN.get("refresh_token").toString())
		            .httpOnly(false)
		            .secure(false) // HTTPS 환경에서만 사용할 경우
		            .path("/")
		            .maxAge(30 * 24 * 60 * 60) // 30일
		            .sameSite("Strict") // 또는 "Lax"
		            .build();

		    response.addHeader("Set-Cookie", refreshTokenCookie.toString());

			return ResponseEntity.status(HttpStatus.CREATED).body(result_data);
		}
		return ResponseEntity.status(HttpStatus.OK).body(result_data);
	}
	
	@PostMapping("/naver/create")
	public ResponseEntity<Map<String, Object>> create_kakao(@Valid @RequestBody CreateNaver naver_info,
			HttpServletResponse response){
		Map<String, Object> result =new HashMap<String ,Object>();
		logger.info("카카오 새게정 만들기");
		result =naver.Create_Naver(naver_info);
		Map<String, Object> TOKEN = (Map<String, Object>) result.get("data");
        logger.info("토큰 결과 :" +TOKEN );
		response.addHeader("Authorization", TOKEN.get("access_token").toString());
		
	    ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", TOKEN.get("refresh_token").toString())
	            .httpOnly(false)
	            .secure(false) // HTTPS 환경에서만 사용할 경우
	            .path("/")
	            .maxAge(30 * 24 * 60 * 60) // 30일
	            .sameSite("Strict") // 또는 "Lax"
	            .build();

	    response.addHeader("Set-Cookie", refreshTokenCookie.toString());
	    TOKEN.remove("access_token");
	    TOKEN.remove("refresh_token");
	    result.put("data", TOKEN);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@PostMapping("/kakao/create")
	public ResponseEntity<Map<String, Object>> create_kakao(@Valid @RequestBody CreateKakao kakao_info,
			HttpServletResponse response){
		Map<String, Object> result =new HashMap<String ,Object>();
		logger.info("카카오 새게정 만들기");
		result =kakao.create_kakao(kakao_info);
		Map<String, Object> TOKEN = (Map<String, Object>) result.get("data");
        logger.info("토큰 결과 :" +TOKEN );
		response.addHeader("Authorization", TOKEN.get("access_token").toString());
		
	    ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", TOKEN.get("refresh_token").toString())
	            .httpOnly(false)
	            .secure(false) // HTTPS 환경에서만 사용할 경우
	            .path("/")
	            .maxAge(30 * 24 * 60 * 60) // 30일
	            .sameSite("Strict") // 또는 "Lax"
	            .build();

	    response.addHeader("Set-Cookie", refreshTokenCookie.toString());
	    TOKEN.remove("access_token");
	    TOKEN.remove("refresh_token");
	    result.put("data", TOKEN);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@GetMapping("/oauth/naver")
	public ResponseEntity<Map<String, Object>> naver_login(@ModelAttribute @Valid NaverCode infos, 
			HttpServletRequest requests, HttpServletResponse response) {
		Map<String, Object> result_data = new HashMap<String, Object>();
		result_data =naver.Naver_Login(infos, requests );
		if (result_data.get("code").equals(401)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result_data);
		}
		else if (result_data.get("code").equals(201)) {
			logger.info("신규 회원 로그인 성공");
			JSONObject res_data = new JSONObject(result_data);
			logger.info("data : " + res_data.get("data"));
			JSONObject ACCESS_TOKEN = new JSONObject(res_data.get("data").toString());
			logger.info("access : " + ACCESS_TOKEN.get("access_token").toString());

			response.addHeader("Authorization", ACCESS_TOKEN.get("access_token").toString());
		    // ✅ refresh_token을 HttpOnly 쿠키로 설정
		    ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", ACCESS_TOKEN.get("refresh_token").toString())
		            .httpOnly(false)
		            .secure(false) // HTTPS 환경에서만 사용할 경우
		            .path("/")
		            .maxAge(30 * 24 * 60 * 60) // 30일
		            .sameSite("Strict") // 또는 "Lax"
		            .build();

		    response.addHeader("Set-Cookie", refreshTokenCookie.toString());

			return ResponseEntity.status(HttpStatus.CREATED).body(result_data);
		}
		
		else if(result_data.get("code").equals(301)) {
			logger.info("기존 계정 존재한다.");
			return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).body(result_data);
		}	
		logger.info("최종 로그인 정보 :" + result_data);
		
		if(result_data.get("code").equals(200) && result_data.get("Customcode").equals("01")) {
			JSONObject res_data = new JSONObject(result_data);
			logger.info("data : " + res_data.get("data"));
			JSONObject ACCESS_TOKEN = new JSONObject(res_data.get("data").toString());
			logger.info("access : " + ACCESS_TOKEN.get("access_token").toString());

			response.addHeader("Authorization", ACCESS_TOKEN.get("access_token").toString());
		    // ✅ refresh_token을 HttpOnly 쿠키로 설정
		    ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", ACCESS_TOKEN.get("refresh_token").toString())
		            .httpOnly(false)
		            .secure(false) // HTTPS 환경에서만 사용할 경우
		            .path("/")
		            .maxAge(30 * 24 * 60 * 60) // 30일
		            .sameSite("Strict") // 또는 "Lax"
		            .build();

		    response.addHeader("Set-Cookie", refreshTokenCookie.toString());

			return ResponseEntity.status(HttpStatus.OK).body(result_data);
			
		}
		return ResponseEntity.status(HttpStatus.OK).body(result_data);
	}
	/*
	@PostMapping("/kakalogout")
	public ResponseEntity<Map<String ,Object>> kakao_out(HttpServletRequest request,
			@RequestBody Map<String ,Object> log_info){
		logger.info("카카오 로그아웃");
		Map<String, Object> Response = new HashMap<String, Object>();
		Response = kakao.kakao_logout(request,"test");
		
		return ResponseEntity.status(HttpStatus.OK).body(Response);
	}
	
	//네이버 로그인
	@PostMapping("/login/naver/callback")
	public ResponseEntity<Map<String, Object>> naver_callback(@RequestBody Map<String, Object> login_info){
		Map<String,Object> response = new HashMap<String, Object>();
		
		return ResponseEntity.ok().body(response);
		
	}
	

	

	*/
	

}
