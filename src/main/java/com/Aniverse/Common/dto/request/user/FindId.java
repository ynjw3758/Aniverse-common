package com.Aniverse.Common.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FindId {
	
    @NotBlank(message = "이름은 필수입니다.")
    private String username;
    
    @NotBlank(message = "이메일는 필수입니다.")
    private String email;
    
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public String getEmail() {
    	return email;
    }

}
