package com.Aniverse.Common.services.user;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public UpdateService() {
		
	}
	
    public Map<String, Object> update_nickname(Map<String, Object> info){
    	Map<String, Object> response = new HashMap<>();
    	
    	
    	return response;
    	
    }

}
