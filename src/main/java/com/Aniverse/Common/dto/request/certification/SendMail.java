package com.Aniverse.Common.dto.request.certification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendMail {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    @NotBlank(message = "이메일는 필수입니다.")
    private String email;
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getId() {
    	return id;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public String getEmail() {
    	return email;
    }

}
