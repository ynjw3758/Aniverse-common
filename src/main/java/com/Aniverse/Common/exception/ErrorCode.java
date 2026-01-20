package com.Aniverse.Common.exception;

import java.util.Map;

public enum ErrorCode {
	///유저 서비스 에러코드 범위(E0010~)
	INVALID_ID_OR_PASSWORD(400,"E0010", "아이디 또는 비밀번호가 일치하지 않습니다."),
	EXIST_NOT_ID(400,"E0011", "아이디가 존재하지 않습니다."),
	Duple_Id(400,"E0020", "아이디가 존재합니다." ),
	Duple_nickname(400,"E0021", "닉네임ㅣ 존재합니다."),
	Duple_email(400,"E0022", "이메일이 존재합니다."),
	SAME_PASSWORD(409, "E0023", "이전 비번과 동일"),
	
	INTERNAL_SERVER_ERROR(500,"E9999", "서버 내부 오류가 발생했습니다."),//500에러 
	
	//인증 서비스 에러(E0030~)
	FAIL_SEND_MAIL(400,"E0030", "이메일이 전송 실패."),
	ISNOT_INFO(400,"E0031", "정보가 옳바르지 않습니다"),
	EXPIRED_CODE(400,"E0032" , "인증번호 만료"),
	INVALID_CODE(400,"E0033", "인증번호 불일치"),
	
	//카카오 로그인
	USER_NOT_INFO(400,"E0034", "아이디로 회원정보를 찾을 수없다."),
	IS_NOT_CODE(401,"E0035", "인증코드를 확인 할 수없습니다."),
	KAKAO_NEED_LINK(409, "E0040", "동일한 메일이 존재합니다."),
	
	//네이버 로그인
	NAVER_NEED_LINK(409, "E0041", "동일한 메일이 존재합니다.");
	
	private final int status;
    private final String code;
    private final String message;


    ErrorCode(int status ,String code, String message) {
    	this.status = status;
        this.code = code;
        this.message = message;
    }
    public int getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }

}
