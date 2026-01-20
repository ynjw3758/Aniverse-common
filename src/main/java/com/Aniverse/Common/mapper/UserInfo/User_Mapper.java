package com.Aniverse.Common.mapper.UserInfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.Aniverse.Common.dto.request.certification.SendMail;
import com.Aniverse.Common.dto.request.user.DupleEmail;
import com.Aniverse.Common.dto.request.user.DupleId;
import com.Aniverse.Common.dto.request.user.DupleNick;
import com.Aniverse.Common.dto.request.user.FindId;

@Mapper
@Repository
public interface User_Mapper {

	public String beforepassword(String Id); //이전 비번 조회
	public void reset_password(Map<String ,Object> info);//비밀번호 변경
	public Map<String ,Object> user_info(String id); //일반 계정 정보 조회
	public List<Map<String, Object>> Matfalow_List(String Id); //맞팔로워 리스트 조회
	public void Follower_connect(Map<String , Object> info); //팔로워 추가
	public void Folloer_cancel(Map<String , Object> info); //팔로워 취소
	public Map<String, Object> friendfind(Map<String, Object> loginfo); //아이디 찾기
	public Map<String, Object> findId(FindId info); //아이디 찾기
	public boolean Connect_Email(String email); //연동 이메일 체크
	public boolean Search_email(DupleEmail Email); //중복 이메일 조회
	public boolean Search_Id(DupleId Id); //중복 아이디 조회
	public boolean Search_Nickname(DupleNick Nickname); //중복 닉네임 조회
	public boolean Search_Phone(String Phone_number); //중복 핸드폰번호 조회
	public boolean exist_info(SendMail info); //이메일 검증을 위한 고객 정보 조회
	public void kakao_insert(Map<String , Object> info); //카카오 정보 저장
	public Map<String ,Object> Kakao_user_info(String id); //카카오 계정 정보 조회
	public void naver_insert(Map<String , Object> info); //네이버 정보 저장
	public Map<String ,Object> Naver_user_info(String id); //네이버 계정 정보 조회
	public void dtlink(String email); //연동 캔슬 업데이트
	public Map<String ,Object> getUserLoginInfo(String Id); //일반 로그인 아이디 체크
	public String user_id(String Phone_number);
	public void Sign_insert(Map<String, Object> userinfo); //회원가입
	public Map<String, Object> Connecting_Id(String Email);//이메일과 등록된 id
	public boolean Naver_Check_id(String id);
	public boolean Kakao_Check_id(String id);
	
}
