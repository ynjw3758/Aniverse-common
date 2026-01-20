package com.Aniverse.Common.exception;

import java.util.Map;

public class CustomException extends RuntimeException{
	
    private final ErrorCode errorCode;
    private final Map<String, Object> data; // 추가된 필드

    public CustomException(ErrorCode errorCode ,Map<String, Object> data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
}
