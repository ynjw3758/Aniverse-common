package com.Aniverse.Common.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DupleEmail {
	
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
	
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public String getEmail() {
    	return email;
    }

}
