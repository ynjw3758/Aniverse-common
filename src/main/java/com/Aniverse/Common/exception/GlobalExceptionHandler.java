package com.Aniverse.Common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode code = ex.getErrorCode();
       
        ErrorResponse response = new ErrorResponse(code.getStatus(), code.getCode(), code.getMessage(), ex.getData());
        return ResponseEntity.status(code.getStatus()).body(response);
    }
    
    // JSON 파싱 에러 (형식 불일치, key 누락 등)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParse(HttpMessageNotReadableException ex) {
    	
        ErrorResponse error = new ErrorResponse(400, "E0011", "입력값이 올바르지 않습니다.", null);
        return ResponseEntity.badRequest().body(error);
    }

    // @Valid 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        ErrorResponse error = new ErrorResponse(400, "E0012", "필수 입력값이 누락되었거나 형식이 올바르지 않습니다.", null);
        return ResponseEntity.badRequest().body(error);
    }

    // 파라미터 타입 불일치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorResponse error = new ErrorResponse(400, "E0013", "요청 파라미터 형식이 잘못되었습니다.", null);
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        ErrorResponse error = new ErrorResponse(
            415,
            "E0014",  // 너가 지정한 에러 코드 체계에 따라
            "지원하지 않는 Content-Type 형식입니다.",
            null
        );
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherException(Exception ex) {
        ErrorResponse response = new ErrorResponse(500, ErrorCode.INTERNAL_SERVER_ERROR.getCode(), 
        		ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), null);
        return ResponseEntity.status(500).body(response);
    }

}
