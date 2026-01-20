package com.Aniverse.Common.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Aniverse.Common.dto.request.user.DupleEmail;
import com.Aniverse.Common.dto.request.user.DupleId;
import com.Aniverse.Common.dto.request.user.DupleNick;
import com.Aniverse.Common.dto.request.user.FindId;
import com.Aniverse.Common.dto.request.user.Resetpass;
import com.Aniverse.Common.dto.request.user.Sign;
import com.Aniverse.Common.services.user.DupleService;
import com.Aniverse.Common.services.user.FindService;
import com.Aniverse.Common.services.user.ResetService;
import com.Aniverse.Common.services.user.SignupService;
import com.Aniverse.Common.services.user.UpdateService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/Pets-social/Common/user")
public class User {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private SignupService sign;
	private DupleService duple;
	private ResetService reset;
	private UpdateService update;
	private FindService find;
	
	public User(SignupService sign ,DupleService duple, ResetService reset,
			      UpdateService update,FindService find) {
		this.sign = sign;
		this.duple = duple;
		this.reset = reset;
		this.update = update;
		this.find = find;
	}
	
	
	
    //비밀번호 리셋
	@PostMapping("/resetpw")
	public ResponseEntity<Map<String, Object>> reset(@Valid @RequestBody Resetpass info) {
		logger.info("비밀번호 초기화 : " );
		Map<String, Object> result_data = new HashMap<String, Object>();
		result_data = reset.reset_password(info);
		logger.info("결과 :" + result_data);
		 return ResponseEntity.status(HttpStatus.OK).body(result_data);
	}
	
	   @PostMapping("/chang-nick")
	   public ResponseEntity<Map<String, Object>>update_nickname(@RequestBody Map<String ,Object> infos,
			   HttpServletRequest request){
		   Map<String, Object> result= new HashMap<>();
		   
		   result =update.update_nickname(infos); 
		   return ResponseEntity.status(HttpStatus.OK).body(result);
	   }
	   
	    //아이디 찾기
		@PostMapping("/findId")
		public ResponseEntity<Map<String, Object>> findid(@Valid @RequestBody FindId info) {
			logger.info("사용자 id 찾기 ");
			logger.info("사용자 id 정보 : " + info);
			Map<String, Object> resuslt_data = new HashMap<String, Object>();
			resuslt_data = find.findid(info);

			return ResponseEntity.ok().body(resuslt_data);
		}
		
		// 회원가입 완료
		@PostMapping("/sign")
		public ResponseEntity<Map<String, Object>> sign(@Valid @RequestBody Sign user_info) {
			logger.info("회원가입 진행 : " + user_info.getId());
			Map<String, Object> result_data = new HashMap<String, Object>();
			logger.info("요청받은 데이터 : " + user_info);
			result_data = sign.receive_info(user_info);
			logger.info("결과 :" + result_data);

			return ResponseEntity.ok().body(result_data);
		}
		
		@GetMapping("/dupl-email")
		public ResponseEntity<Map<String ,Object>> dupl_Eamil(@ModelAttribute DupleEmail email){
			Map<String, Object> result_data = new HashMap<String, Object>();
			logger.info("email :"+email);
			result_data = duple.check_email(email);
			logger.info("결과 : " + result_data);
			if (result_data.get("Code").equals(400)) {
				logger.info("중복 확인");
				return ResponseEntity.badRequest().body(result_data);
			}
			return ResponseEntity.ok().body(result_data);
		}

		//중복 아이디
		@GetMapping("/dupl-id")
		public ResponseEntity<Map<String, Object>> duplicate_id(@ModelAttribute DupleId id) {
			Map<String, Object> result_data = new HashMap<String, Object>();
			result_data = duple.check_id(id);
			logger.info("결과 : " + result_data);
			if (result_data.get("Code").equals(400)) {
				logger.info("중복 확인");
				return ResponseEntity.badRequest().body(result_data);
			}

			return ResponseEntity.ok().body(result_data);
		}

		//중복 닉네임
		@GetMapping("/dupl-nick")
		public ResponseEntity<Map<String, Object>> duplicate_nickname(@ModelAttribute DupleNick name) {
			Map<String, Object> result_data = new HashMap<String, Object>();
			logger.info("요청 데이터 :" + name);
			result_data = duple.check_nickname(name);
			logger.info("결과 : " + result_data);
			if (result_data.get("Code").equals(400)) {
				logger.info("중복 확인");
				return ResponseEntity.badRequest().body(result_data);
			}

			return ResponseEntity.ok().body(result_data);

		}

}
