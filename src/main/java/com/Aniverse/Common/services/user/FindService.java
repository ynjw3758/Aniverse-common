package com.Aniverse.Common.services.user;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.dto.request.user.FindId;
import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.mapper.UserInfo.User_Mapper;

@Service
public class FindService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User_Mapper userinfo;
	
	public FindService(User_Mapper userinfo) {
		this.userinfo = userinfo;
	}
	
	
	//사용자 id찾기
	public Map<String , Object> findid(FindId info){
		
	Map<String , Object> result_data = new HashMap<String , Object>();
	      logger.info("사용자 id찾기");
	      result_data = userinfo.findId(info);
	      logger.info("사용자 아이디 조회 결과 :" + result_data);
	      if(result_data !=null ) {
	    	  logger.info("아이디 찾기 성공");
	    	  result_data.put("resultCode", 200);
	    	  result_data.put("resultMsg", "아이디 찾기 완료");
	    	  result_data.put("id", result_data.get("id").toString());
	    	  
	      }
	      else {
	    	  logger.info("아이디 찾기 실패");
	    	  throw new CustomException(ErrorCode.EXIST_NOT_ID, null);
	      }
	return result_data;
	}

}
