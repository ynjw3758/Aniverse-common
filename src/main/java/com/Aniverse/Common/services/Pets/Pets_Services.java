package com.Aniverse.Common.services.Pets;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;
import com.Aniverse.Common.mapper.Pets.Pets_Mapper;

@Service
public class Pets_Services {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Pets_Mapper pets_mapper;
	
	public Pets_Services(Pets_Mapper pets_mapper) {
		this.pets_mapper = pets_mapper;
	}
	
	public Map<String, Object>getPetsInfo(String userid){
		Map<String, Object> result = new HashMap<String, Object>();
		UUID userId;
		try {
			userId = UUID.fromString(userid);
		}catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_UUID, null);
	    }
		List<Map<String, Object>> petsInfo = pets_mapper.getPets(userId);
		if(petsInfo.isEmpty()) {
			logger.info("등록된 펫 정보가 없습니다");
			result.put("code", 200);
			result.put("msg", "data is not found");
			result.put("data", null);
		}else {
			logger.info("등록된 펫 정보가 있습니다");
			result.put("msg", "data is exist");
			result.put("data", petsInfo);
			
		}
		
		return result;
	}
	

}
