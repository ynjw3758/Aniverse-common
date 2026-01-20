package com.Aniverse.Common.dto.request.user;



import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Sign {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    @NotBlank(message = "닉네임는 필수입니다.")
    private String nickname;
    
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    
    @NotBlank(message = "비밀번호은 필수입니다.")
    private String password;
    
    @NotBlank(message = "주소은 필수입니다.")
    private String address;
    
    public void setAddress(String address) {
    	this.address = address;
    }
    
    public String getAddress() {
    	return address;
    }
    
    public void setPasswrod(String password) {
    	this.password = password;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setNickname(String nickname) {
    	this.nickname = nickname;
    }
    
    public String getNickname() {
    	return nickname;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getId() {
    	return id;
    }
    

}
