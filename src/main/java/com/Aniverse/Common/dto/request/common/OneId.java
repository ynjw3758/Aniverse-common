package com.Aniverse.Common.dto.request.common;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class OneId {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }

}
