package com.dufyun.springmvc.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/usercenter")
public class UserCenterController {

	
	@RequestMapping(value="/login" ,method=RequestMethod.POST)
	public String login(HttpServletRequest request,HttpServletResponse response){
		
		System.out.println(" login()  ...");
		
		return "";
	}
	
}
