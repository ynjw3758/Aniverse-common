package com.Aniverse.Common.dto.request.login;

import jakarta.validation.constraints.NotBlank;


import lombok.Data;

@Data
public class LoginRequest {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    
    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getPassword() {
    	return password;
    }

    public void setPassword(String password) {
    	this.password = password;
    }
}
