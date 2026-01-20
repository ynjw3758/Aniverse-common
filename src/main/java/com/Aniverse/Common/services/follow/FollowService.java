package com.Aniverse.Common.services.follow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.mapper.UserInfo.User_Mapper;

@Service
public class FollowService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User_Mapper userinfo;
	
	public FollowService(User_Mapper userinfo) {
		this.userinfo = userinfo;
	}
	
	//친구 리스트 가져오기
	public Map<String ,Object> freindly_list(String id){
		List<Map<String, Object>> friend_list = new ArrayList<Map<String, Object>>();
		Map<String ,Object> data = new HashMap<String, Object>();
		logger.info("아이디 : " + id);
		friend_list=userinfo.Matfalow_List(id);
		
		logger.info("맞팔로워 조회ㅏ :" + friend_list);
		if(friend_list.size() !=0) {
			data.put("code", 200);
			data.put("msg", "success");
			data.put("data", friend_list);
		}
		else {
			data.put("code", 201);
			data.put("msg", "empty");
			data.put("data", "null");
		}
		
		return data;
	}
	
    public Map<String, Object> follower(Map<String, Object> info){
    	
    	Map<String,Object> response = new HashMap<String, Object>();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fileDate = sdf.format(date);
    	if(info.get("type").equals("connect")) {
    		logger.info("팔로우 신청 :");
    		info.put("relationdate", fileDate);
    		userinfo.Follower_connect(info);
    		
    	}
    	else {
    		logger.info("팔로우 취소");
    		logger.info("넘어온 데이터 :" + info);
    		userinfo.Folloer_cancel(info);
    		
    		
    	}
    	response.put("resultcode", 200);
    	response.put("resultmsg", "팔로워 취소 완료");
    	
    	return response;
    }

}
