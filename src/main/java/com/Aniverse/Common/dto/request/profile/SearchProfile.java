package com.Aniverse.Common.dto.request.profile;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class SearchProfile {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    @NotBlank(message = "상대아이디는 필수입니다.")
    private String userid;
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getId() {
    	return id;
    }
    
    
    public void setUserid(String userid) {
    	this.userid = userid;
    }
    
    public String getUserid() {
    	return userid;
    }

}
