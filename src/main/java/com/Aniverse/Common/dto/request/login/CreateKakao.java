package com.Aniverse.Common.dto.request.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateKakao {
	
    @NotBlank(message = "아이디는 필수입니다.")
    private String id;
    
    @NotBlank(message = "이메일는 필수입니다.")
    private String email;
    
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    
    @NotBlank(message = "프로필 사진은 필수입니다.")
    private String profile;
    
    @NotBlank(message = "섬네일은 필수입니다.")
    private String thumbnail;
    
    @NotBlank(message = "성별은 필수입니다.")
    private String gender;
    
    public void setGender(String gender) {
    	this.gender =gender;
    }
    
    public String getGender() {
    	return gender;
    }
    
    public void setThumbnail(String thumbnail) {
    	this.thumbnail =thumbnail;
    }
    
    public String getThumnail() {
    	return thumbnail;
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
