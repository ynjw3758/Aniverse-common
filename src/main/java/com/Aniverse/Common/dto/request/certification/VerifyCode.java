package com.Aniverse.Common.dto.request.certification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyCode {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    @NotBlank(message = "코드는 필수입니다.")
    private String code;
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getId() {
    	return id;
    }
    
    public void setCode(String code) {
    	this.code = code;
    }
    
    public String getCode() {
    	return code;
    }

}
