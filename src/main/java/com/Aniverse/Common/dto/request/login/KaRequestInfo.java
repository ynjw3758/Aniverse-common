package com.Aniverse.Common.dto.request.login;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KaRequestInfo {
	
    @NotBlank(message = "code는 필수입니다.")
    private String code;
    
    @NotBlank(message = "clientType는 필수입니다.")
    private String clientType;
    
    public void setClineType(String clientType) {
        this.clientType = clientType;    	
    }
    
    public String getClientType() {
    	return clientType;
    }
    
    public void setCode(String code) {
    	this.code = code;
    }
    
    public String getCode() {
    	return code;
    }

}
