package com.Aniverse.Common.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class DupleId {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getId() {
    	return id;
    }

}
