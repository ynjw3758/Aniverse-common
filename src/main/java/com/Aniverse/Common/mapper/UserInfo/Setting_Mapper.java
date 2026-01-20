package com.Aniverse.Common.mapper.UserInfo;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface Setting_Mapper {
	

	public boolean search_private(String userid); //비굥개 계정인지 조회
	public void Initsetting(Map<String, Object> info); //회원가입 후 설정

}
