package com.Aniverse.Common.services.user;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.dto.request.user.DupleEmail;
import com.Aniverse.Common.dto.request.user.DupleId;
import com.Aniverse.Common.dto.request.user.DupleNick;
import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;

@Service
public class DupleService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User_Mapper userinfo;
	
	public DupleService(User_Mapper userinfo) {
		this.userinfo = userinfo;
	}
	
	//중복 이메일 체크
	public Map<String, Object> check_email(DupleEmail email){
		Map<String , Object> result_response = new HashMap<String, Object>();
		logger.info("이메일 중복 확인");
		boolean email_check = false;
		email_check =userinfo.Search_email(email);
		logger.info("중복 아이디 확인 결과 :" + email_check);
		if(email_check == true) {
			logger.info("이메일 중복");
			result_response.put("Code", 400);
			result_response.put("Msg", "중복  이메일");
			throw new CustomException(ErrorCode.Duple_email, null);
		}
		else {
			logger.info("이메일 사용 가능");
			result_response.put("Code",200);
			result_response.put("Msg", "이메일 사용 가능");
		}
		
		return result_response;
	}
	
    //중복 아이디 체크
	public Map<String, Object> check_id(DupleId id) {
		Map<String , Object> result_response = new HashMap<String, Object>();
		logger.info("중복 아이디 검색");
		logger.info("중복 확인 id: " + id);
		boolean id_check = false;
		id_check =userinfo.Search_Id(id);
		logger.info("중복 아이디 확인 결과 :" + id_check);
		if(id_check == true) {
			logger.info("중복 아이디");
			result_response.put("Code", 400);
			result_response.put("resultMsg", "중복 id");
			throw new CustomException(ErrorCode.Duple_Id, null);
		}
		else {
			logger.info("아이디 사용 가능");
			result_response.put("Code", 200);
			result_response.put("Msg", "아이디 사용 가능");
		}
		
		return result_response;
	}
	
	//중복 닉네임 체크
	public Map<String, Object> check_nickname(DupleNick nickname) {
		Map<String , Object> result_response = new HashMap<String, Object>();
		logger.info("중복 닉네임 검색");
		boolean nickname_check = false;
		nickname_check = userinfo.Search_Nickname(nickname);
		logger.info("중복 닉네임 확인 결과 : " + nickname_check);
		
		if(nickname_check == true) {
			logger.info("중복 닉네임");
			result_response.put("Code", 400);
			result_response.put("Msg", "중복 닉네임");
			throw new CustomException(ErrorCode.Duple_nickname, null);
		}
		else {
			logger.info("닉네임 사용 가능");
			result_response.put("Code", 200);
			result_response.put("Msg", "닉네임 사용 가능");
		}
		return result_response;
	}

}
