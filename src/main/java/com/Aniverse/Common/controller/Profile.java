package com.Aniverse.Common.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Aniverse.Common.dto.request.login.LoginRequest;
import com.Aniverse.Common.dto.request.profile.BaseProfile;
import com.Aniverse.Common.dto.request.profile.SearchProfile;
import com.Aniverse.Common.services.profile.ProfileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/Pets-social/Common/profile")
public class Profile {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ProfileService profile;
	 
	 
	public Profile(ProfileService profile) {
		this.profile = profile;
	}
	
	@GetMapping("/Smallprofile")
	public ResponseEntity<Map<String, Object>> small_profile(HttpServletRequest request, 
			@Valid @RequestBody SearchProfile  infos){
		Map<String, Object> result = new HashMap<String, Object>();
		result =profile.Small_Profile(infos.getId(),  infos.getUserid()); 
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@GetMapping("/SearchProfile")
	public ResponseEntity<Map<String, Object>>Mypage(HttpServletRequest request,
			@Valid @RequestBody BaseProfile  infos){
		Map<String, Object> result = new HashMap<String, Object>();

		result = profile.Profile(infos.getId() , infos.getType(), infos.getUserid());
		logger.info("아니 뭐야 ? " + result);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
