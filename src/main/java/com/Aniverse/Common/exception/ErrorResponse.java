package com.Aniverse.Common.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
	
    private int status;
    private String errorcode;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, Object> data;
    
    public ErrorResponse(int status, String errorcode, String message ,Map<String, Object> data) {
        this.status = status;
        this.errorcode = errorcode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }
    
    public String getErrorcode() {
        return errorcode;
    }

    public void setError_code(String errorcode) {
        this.errorcode = errorcode;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
    	this.timestamp = timestamp;
    }
    
    public LocalDateTime getTimestamp() {
    	return timestamp;
    }
    
    public void setStatus(int status) {
    	this.status = status;
    }
    
    public int getStatus() {
    	return status;
    }
    
    public void setMessage(String message) {
    	this.message = message;
    }
    
    public String getMessage() {
    	return message;
    }
    
    public void setData(Map<String, Object> data) {
    	this.data = data;
    }
    
    public Map<String, Object> getData(){
    	
    	return data;
    }

    
}
