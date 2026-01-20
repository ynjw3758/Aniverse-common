package com.Aniverse.Common.dto.request.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NaverCode {
	
    @NotBlank(message = "code는 필수입니다.")
    private String code;
    
    @NotBlank(message = "state는 필수입니다.")
    private String state;
    
    public void setState(String state) {
    	this.state = state;
    }
    
    public String getState() {
    	return state;
    }
    
    public void setCode(String code) {
    	this.code = code;
    }
    
    public String getCode() {
    	return code;
    }

}
