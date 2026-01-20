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

import com.Aniverse.Common.dto.request.common.OneId;
import com.Aniverse.Common.dto.request.login.NaverCode;
import com.Aniverse.Common.services.follow.FollowService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/Pets-social/Common/follow")
public class Follow {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private FollowService follow;
	
	public Follow(FollowService follow) {
		this.follow = follow;
	}
	
	
	
	@GetMapping("/MatList")
	public ResponseEntity<Map<String, Object>> MatList(@RequestParam("id") String id
			){
		Map<String, Object> response = new HashMap<String, Object>();
		logger.info("아니 이거 뭐여 :" + id);
		response =follow.freindly_list(id); 
		logger.info("결과 값 : " + response);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	@PostMapping("/follower")
	public ResponseEntity<Map<String , Object>> follower(HttpServletRequest request ,
			@RequestBody Map<String ,Object> info){
		logger.info("팔로워 정보 :"+ info);
		ResponseEntity<Map<String, Object>> check_token;
		Map<String, Object> result =new HashMap<String, Object>();
			result = follow.follower(info);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
