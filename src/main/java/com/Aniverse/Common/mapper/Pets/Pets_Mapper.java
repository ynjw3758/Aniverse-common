package com.Aniverse.Common.mapper.Pets;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.*;
@Mapper
@Repository
public interface Pets_Mapper {
	
	public List<Map<String, Object>> getPets(UUID userId);

}
