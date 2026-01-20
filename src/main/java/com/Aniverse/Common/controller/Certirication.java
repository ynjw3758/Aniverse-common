package com.Aniverse.Common.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.Aniverse.Common.dto.request.certification.SendMail;
import com.Aniverse.Common.dto.request.certification.VerifyCode;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;
import com.Aniverse.Common.services.certification.Common_Certification;
import com.Aniverse.Common.services.redis.Redis_Services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/Pets-social/Common/certifi")
public class Certirication {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Redis_Services redis;
	private User_Mapper mapper;
	private Common_Certification cm_Certifi;
	
	public Certirication(Redis_Services redis ,User_Mapper mapper,Common_Certification cm_Certifi) {
     this.redis = redis;
     this.mapper = mapper;
     this.cm_Certifi =cm_Certifi; 
	}
	
	@GetMapping("/verifiCertifi")
	public ResponseEntity<Map<String, Object>> verifiCertifi(@RequestParam("id") String id,
		    @RequestParam("code") String code){
		Map<String, Object> response = new HashMap<>();
		response = cm_Certifi.verifiCertifi(id, code);
		logger.info("인증번호 검증 결과 :" + response);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/sendemail")
	public ResponseEntity<Map<String, Object>> certifi_email(@Valid @RequestBody SendMail infos,
			HttpServletRequest request){
		Map<String,Object> response = new HashMap<String, Object>();
		String platformType = request.getHeader("X-Platform-Type");
		response =cm_Certifi.sendEmail(infos , platformType); 
		
		if(response.get("code").equals(404)) {
			logger.info("입력값 없음");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		logger.info("결과 :" + response);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/findpw")
	public ResponseEntity<Map<String, Object>> findpw(@RequestBody Map<String, Object> info) {
		logger.info("요청 파라미터 : " + info);
		Map<String, Object> Result_Data = new HashMap<String, Object>();
		Result_Data = cm_Certifi.findpw(info);
		if (Result_Data.get("resultcode").equals(400)) {
			logger.error("인증번호 발송 실패");
			return ResponseEntity.badRequest().body(Result_Data);
		}

		return ResponseEntity.ok().body(Result_Data);
	}

	// 인증번호 생성
	@PostMapping("/send-sms")
	public ResponseEntity<Map<String, Object>> user_info(@RequestBody Map<String, Object> userInfo)
			/*throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException*/ {
		// Map<String , Object> reuslt_data = new HashMap<String, Object>();
		logger.info("사용자 인증번호 ");
		logger.info("인증번호 생성 파라미터: " + userInfo);
		// Response response = new Response();
		Map<String, Object> result_data = new HashMap<String, Object>();
		/*
		result_data = certifies.SendMassage(userInfo);
		logger.info("test :" + result_data);
		if (result_data.get("resultcode").equals(400)) {
			logger.info("중복 핸드폰 번호 ");
			return ResponseEntity.badRequest().body(result_data);
		}
*/
		return ResponseEntity.ok().body(result_data);
	}
	


	// 비밀번호 리셋을 위한 인증번호 api
	@PostMapping("/reset-pass")
	public ResponseEntity<Map<String, Object>> re_password(@RequestBody Map<String, Object> reset)
			/*throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException*/ {

		logger.info("인증번호 조회");
		logger.info("조회 데이터 : " + reset);
		Map<String, Object> result_data = new HashMap<String, Object>();
		result_data = redis.SMSgetData(reset);
		logger.info("result :" + result_data);

		if (result_data.get("resultCode").equals(400)) {
			logger.error("인증번호가 다릅니다");
			return ResponseEntity.badRequest().body(result_data);
		}
		String id = "";
		id = reset.get("phonnumber").toString();
		id = mapper.user_id(id);
		result_data.put("id", id);
		return ResponseEntity.ok().body(result_data);
	}

	// 인증번호 확인
	@PostMapping("/verifi-sms")
	public ResponseEntity<Map<String, Object>> verification_sms(@RequestBody Map<String, Object> check_info)
			/*throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException*/ {

		logger.info("인증번호 조회");
		logger.info("조회 데이터 : " + check_info);
		Map<String, Object> result_data = new HashMap<String, Object>();
		result_data = redis.SMSgetData(check_info);
		logger.info("result :" + result_data);

		if (result_data.get("resultCode").equals(400)) {
			logger.error("인증번호가 다릅니다");
			return ResponseEntity.badRequest().body(result_data);
		}

		return ResponseEntity.ok().body(result_data);
	}
}
