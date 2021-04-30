package com.psa.grpc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psa.grpc.utils.RestServiceUtils;

@RestController
public class TestController {
	
	@Autowired RestServiceUtils rest;
	
	
	
	 @Value("${spring.profiles.active}")
	 private String profile;
	    
	
	@GetMapping("/")
	public String test()
	{
		//String profile ;
		return "Hello , This is "+ profile+ " profile";
	}	
	 
	@GetMapping(value  = "/123")
	public Object test2()
	{
		try {
			 return rest.getOrgnizationMst();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return "ERROR";
	}	
	
	@GetMapping(value = "/234")
	public Object test3()
	{
		try {
			Map<String, Object> map  =new HashMap<>();
			map.put("member_type", "user");
			map.put("member_id", "17433405");
			
			 return rest.getOrgnizationMemeberships(map);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return "ERROR";
	}	
	 
}
