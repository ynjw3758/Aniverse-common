package com.Aniverse.Common.services.redis;

import org.springframework.stereotype.Service;

import com.Aniverse.Common.exception.CustomException;
import com.Aniverse.Common.exception.ErrorCode;

import java.time.Duration;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.data.redis.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Service
public class Redis_Services {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int LIMIT_TIME = 3*60;
    private RedisTemplate<String, Object> refreshTokenRedisTemplate;
    private RedisTemplate<String, Object> certCodeRedisTemplate;
    
    public Redis_Services(@Qualifier("certCodeRedisTemplate") RedisTemplate<String, Object> certCodeRedisTemplate,
    		@Qualifier("refreshTokenRedisTemplate") RedisTemplate<String, Object> refreshTokenRedisTemplate) {
    	   this.certCodeRedisTemplate = certCodeRedisTemplate;
    	   this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
    	   
    }
    
    public Map<String, Object> SMSgetData(Map<String, Object> certification_info){
    	logger.info("redis key 조회 " );
    	Map<String, Object> result_data = new HashMap<String, Object>();
    	
    	String Respons_data = "";
    	Respons_data = (String) certCodeRedisTemplate.opsForValue().get(certification_info.get("phonnumber").toString());
    	logger.info("인증번호 : " + Respons_data);
    	logger.info("전송돠된 인증번호 : " + certification_info.get("certifi_number").toString());
    	boolean value_check = false;
    	
    	
    	if(Respons_data.equals("")) {
    		logger.info("redis의 저장된 값을 찾을 수 없습니다 ");
    	    result_data.put("resultCode", 400);
    	    result_data.put("resultMsg", "인증번호 불일치");
    	}
    	else {
    		
    		if(certification_info.get("certifi_number").equals(Respons_data)) {
    	    logger.info("인증번호 확인");
    	    result_data.put("resultCode", 200);
    	    result_data.put("resultMsg", "인증번호 일치");
    	    certCodeRedisTemplate.delete(certification_info.get("phonnumber").toString());
    		}
    		else {
        	    logger.info("인증번호가 다릅니다");
        	    result_data.put("resultCode", 400);
        	    result_data.put("resultMsg", "인증번호 불일치");
    		}
    	}
        return result_data;
    }
    
    public void Delete_Certifi(String id) {
    	certCodeRedisTemplate.delete(id);
    }
    
    public Map<String, Object> Certifi_validate( String id ,String code){
    	Map<String, Object> info = new HashMap<>();
    	String Certifi_Code = "";
    	Certifi_Code = (String) certCodeRedisTemplate.opsForValue().get(id);
    	logger.info("Certifi_Code : " + Certifi_Code);
    	if(Certifi_Code == null) {
    		logger.info("인증번호 자체가 없다");
    		throw new CustomException(ErrorCode.EXPIRED_CODE, null);
    	}else if(!Certifi_Code.equals(code)){
    		logger.info("인증번호 불일치");
    		throw new CustomException(ErrorCode.INVALID_CODE, null);
    		
    	}else if(Certifi_Code.equals(code)) {
    		info.put("result", "SUCCESS");
    		info.put("message", "인증번호가 일치합니다.");
    		certCodeRedisTemplate.delete(id);
    	}
    	return info;
    	
    }
    
    public void Certifi_insert(Map<String, Object> info){
        // 전달받은 값 파싱
        String userId = (String) info.get("Id");
        String certCode = (String) info.get("Certification");
        logger.info("이메일 인증번호 :" + info.get("Certification"));
        // 저장 (3분 만료 예시)
        certCodeRedisTemplate.opsForValue().set(userId, certCode, Duration.ofMinutes(3));
    }
    
    public void setData(String key, String value,Long expiredTime){

        final ValueOperations<String, Object> valueOperations = refreshTokenRedisTemplate.opsForValue();
        if(key.equals("refresh_token")) {
        	logger.info("refresh_token redis 저장");
        	valueOperations.set(key, value , Duration.ofMinutes(10));
            final String KEY_VALUE = (String) valueOperations.get(key);
            logger.info("get : " + KEY_VALUE);
        	return;
        	
        }
        valueOperations.set(key, value , Duration.ofMinutes(10));
        
        final String KEY_VALUE = (String) valueOperations.get(key);
        logger.info("get : " + KEY_VALUE);
        return;
    }
    
    public void InsertToken(String key, String value,Long expiredTime){

        final ValueOperations<String, Object> valueOperations = refreshTokenRedisTemplate.opsForValue();
        valueOperations.set(key, value , Duration.ofMillis(expiredTime));
        
        final String KEY_VALUE = (String) valueOperations.get(key);
        logger.info("get : " + KEY_VALUE);
        return;
    }

}
