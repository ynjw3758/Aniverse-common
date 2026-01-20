package com.Aniverse.Common.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DupleNick {
	
    @NotBlank(message = "닉네임는 필수입니다.")
    private String nickname;
    
    public void setNickname(String nickname) {
    	this.nickname = nickname;
    }
    
    public String getNickname() {
    	return nickname;
    }

}
