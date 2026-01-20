package com.Aniverse.Common.mapper.ReactCommon;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReactCommon_Mapper {
	
	/*------좋아요 체크 유무 확인----*/
	public boolean isHeart_Check(Map<String, Object> infos);
	
	public int comment_cnt(String contentid);
	
	public boolean Comment_exist(String contentid);

}
