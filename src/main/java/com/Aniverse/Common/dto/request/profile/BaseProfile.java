package com.Aniverse.Common.dto.request.profile;

import org.hibernate.validator.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseProfile {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    @NotBlank(message = "타입은 필수입니다.")
    private String type;
    
    @NotBlank(message = "상대아이디는 필수입니다.")
    private String userid;
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getId() {
    	return id;
    }
    
    public void setType(String type) {
    	this.type = type;
    }
    
    public String getType() {
    	return type;
    }
    
    
    public void setUserid(String userid) {
    	this.userid = userid;
    }
    
    public String getUserid() {
    	return userid;
    }

}
