package com.Aniverse.Common.services.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aniverse.Common.dto.request.user.Sign;
import com.Aniverse.Common.mapper.UserInfo.Setting_Mapper;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;
import com.Aniverse.Common.services.generation.Generation_Services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SignupService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private PasswordEncoder passwordEncoder; 
	private User_Mapper userinfo;
	private Setting_Mapper setting;
	private Generation_Services generation;
	
	public SignupService(PasswordEncoder passwordEncoder ,User_Mapper userinfo,
			             Setting_Mapper setting, Generation_Services generation) {
		this.passwordEncoder = passwordEncoder;
		this.userinfo = userinfo;
		this.setting = setting;
		this.generation =generation;
	}
	
	
	//회원가입
	@Transactional
	public Map<String, Object> receive_info(Sign user_info){
		Map<String , Object> result_response = new HashMap<String, Object>();
		Map<String , Object> setting_Init = new HashMap<String, Object>();
		logger.info("회원가입 메소드 ");
		logger.info("회원가입 비밀번호 암호화 전 : " + user_info.getPassword());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> user_infos = mapper.convertValue(user_info, new TypeReference<Map<String, Object>>() {});
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String create_date = sdf.format(date);
		
		String Hash_Password = "";
		Hash_Password = passwordEncoder.encode(user_info.getPassword());
		logger.info("암호화 후 데이터 : " + Hash_Password);
		user_infos.put("Password", Hash_Password);
		user_infos.put("log_kind", "N");
		String uuid = generation.uuid();
		logger.info("uuid : " + uuid);
		user_infos.put("uuid", uuid);
		user_infos.put("Phone", "01097977894");
		user_infos.put("createdate", create_date);
		logger.info("회원가입 데이터 :" + user_infos);
		setting_Init.put("userid", user_info.getId());
		setting_Init.put("date", create_date);
		userinfo.Sign_insert(user_infos);
		setting.Initsetting(setting_Init);
		 result_response.put("resultCode", 200);
		 result_response.put("resultMsg", "회원 가입 완료");;
		return result_response;
	}

}
