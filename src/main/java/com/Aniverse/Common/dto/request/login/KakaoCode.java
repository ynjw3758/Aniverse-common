package com.Aniverse.Common.dto.request.login;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KakaoCode {
	
    @NotBlank(message = "code는 필수입니다.")
    private String code;
    
    public void setCode(String code) {
    	this.code = code;
    }
    
    public String getCode() {
    	return code;
    }

}
