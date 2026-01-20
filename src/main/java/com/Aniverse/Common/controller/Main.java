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
import com.Aniverse.Common.services.main.MainService;

@RestController
@RequestMapping("/Pets-social/Common/main")
public class Main {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MainService main;

	public Main(MainService main) {
      this.main = main;
	}
    //로그인 후 메인 페이지 렌더링 시 mongodb에 저장되어있는 이미지 및 동영상 파일 가져오기
	@GetMapping("/refresh-main")
	public ResponseEntity<Map<String, Object>> refresh(
			/*@RequestBody Map<String ,Object> info*/
			@RequestParam("Id") String id) {
		logger.info("메인 페이지 리로딩");
		logger.info("info: " + id);
		Map<String, Object> result_data = new HashMap<String, Object>();
		result_data = main.Mainpage_refresh( id);
		
		if(result_data.get("code").equals(201)) {
			
			return ResponseEntity.status(HttpStatus.CREATED).body(result_data);
		}

		return ResponseEntity.status(HttpStatus.OK).body(result_data);
	}
	

	
	


}
