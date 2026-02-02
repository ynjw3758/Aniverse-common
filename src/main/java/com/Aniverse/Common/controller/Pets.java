package com.Aniverse.Common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	

}
