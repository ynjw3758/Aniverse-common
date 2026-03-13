package com.Aniverse.Common.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Aniverse.Common.services.Pets.Pets_Services;

@RestController
@RequestMapping("/Pets-social/Common/")
public class Pets {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Pets_Services pet_service;
	
	public Pets(Pets_Services pet_service) {
		this.pet_service = pet_service;
	}
	
	@GetMapping("api/me/pets")
	public ResponseEntity<Map<String, Object>> getMyPets(
			 @RequestHeader(value = "X-User-Id", required = false) String userId){
		  logger.info("uuid :" + userId);
		Map<String, Object> Response = new HashMap<>();
		  Response = pet_service.getMyPets(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(Response);
		
	}
	
	@GetMapping("api/pet-categories")
	public ResponseEntity<Map<String, Object>> getCatagories(){
		Map<String, Object> Response = new HashMap<>();
		Response = pet_service.getCatagories();
		return ResponseEntity.status(HttpStatus.OK).body(Response);
		
	}
	
    @GetMapping("api/pet-categories/{catagoryCode}/breeds")
    public ResponseEntity<Map<String, Object>> getBreeds(@PathVariable("catagoryCode") String catagoryCode) {
        
		Map<String, Object> Response = new HashMap<>();
		Response  = pet_service.getBreeds(catagoryCode);
		return ResponseEntity.status(HttpStatus.OK).body(Response);
		
    }

}
