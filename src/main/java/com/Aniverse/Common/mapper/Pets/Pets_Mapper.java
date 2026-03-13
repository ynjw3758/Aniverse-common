package com.Aniverse.Common.mapper.Pets;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.Aniverse.Common.dto.pets.PetBreedResponse;
import com.Aniverse.Common.dto.pets.PetCategoryResponse;

import java.util.*;
@Mapper
@Repository
public interface Pets_Mapper {
	
	public List<Map<String, Object>> getPets(@Param("userId") UUID userId);
	public List<PetCategoryResponse> getCatagory();
	public List<PetBreedResponse> getBreed(String catagoryCode);

}
