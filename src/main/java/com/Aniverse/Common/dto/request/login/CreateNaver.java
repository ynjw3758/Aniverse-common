package com.Aniverse.Common.dto.request.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNaver {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    @NotBlank(message = "이름은 필수입니다.")
    private String username;
    
    @NotBlank(message = "이메일는 필수입니다.")
    private String email;
    
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    
    @NotBlank(message = "프로필 사진은 필수입니다.")
    private String profile;
    
    @NotBlank(message = "생일은 필수입니다.")
    private String birthday;
    
    @NotBlank(message = "핸드폰번호은 필수입니다.")
    private String phone;
    
    @NotBlank(message = "성별은 필수입니다.")
    private String gender;
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public void setGender(String gender) {
    	this.gender =gender;
    }
    
    public String getGender() {
    	return gender;
    }
    
    public void setPhone(String phone) {
    	this.phone =phone;
    }
    
    public String getPhone() {
    	return phone;
    }
    
    public void setBirthday(String birthday) {
    	this.birthday =birthday;
    }
    
    public String getBirthday() {
    	return birthday;
    }
    
    public void setProfile(String profile) {
    	this.profile =profile;
    }
    
    public String getProfile() {
    	return profile;
    }
    
    public void setNickname(String nickname) {
    	this.nickname =nickname;
    }
    
    public String getNickname() {
    	return nickname;
    }
    
    public void setEmail(String email) {
    	this.email =email;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setId(String id) {
    	this.id =id;
    }
    
    public String getId() {
    	return id;
    }

}
