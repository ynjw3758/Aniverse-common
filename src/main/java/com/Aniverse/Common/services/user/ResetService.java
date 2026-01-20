package com.Aniverse.Common.services.user;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Aniverse.Common.dto.request.user.Resetpass;
import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;

@Service
public class ResetService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User_Mapper userinfo;
	private PasswordEncoder passwordEncoder; 
	
	
	public ResetService(User_Mapper userinfo, PasswordEncoder passwordEncoder) {
		
		this.userinfo = userinfo;
		this.passwordEncoder = passwordEncoder;
	}
	
    @Transactional
    public Map<String, Object> reset_password(Resetpass user_info){
    	logger.info("비밀번호 리셋");
    	Map<String, Object> result_data = new HashMap<String , Object>();

    	String new_pass = "";
    	String beforepass ="";
    	boolean issame=false;
    	beforepass = userinfo.beforepassword(user_info.getId());
    	new_pass =passwordEncoder.encode(user_info.getPassword());
    	issame = passwordEncoder.matches(user_info.getPassword(), beforepass);
    	
    	if(issame) {
        	throw new CustomException(ErrorCode.SAME_PASSWORD, null);
    	}else {
        	logger.info("새로운 비밀번호 : " + new_pass);
        	Map<String , Object> info = new HashMap<>();
        	info.put("New_password", new_pass);
        	info.put("id", user_info.getId());
        	userinfo.reset_password(info);
        	result_data.put("Code", 200);
        	result_data.put("Msg", "success");
        	return result_data;
    	}

    	}

}
