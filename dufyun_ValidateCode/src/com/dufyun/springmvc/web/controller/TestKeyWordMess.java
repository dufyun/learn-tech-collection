package com.dufyun.springmvc.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.spec.EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestKeyWordMess {
	
@RequestMapping(value="/testName",method=RequestMethod.GET)
public String testName(Model m ,HttpServletRequest request) throws UnsupportedEncodingException{
		
		//1.一次encodeURI编码使用的方式(默认已知到服务器的编码方式为iso-8859-1)
		//String name = new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
		//2.两次encodeURI编码，进行解码的方式 可以没有值
		String name = request.getParameter("name");
		if(!StringUtils.isEmpty(name)){
			String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
			name = URLDecoder.decode(name,"utf-8");
			Pattern p = Pattern.compile(regEx);
			Matcher matcher = p.matcher(name);
			name = matcher.replaceAll(",").trim();
			
		}
		
		System.out.println(name);
	
		return "";
	}
	@RequestMapping(value="/testCookie",method=RequestMethod.GET)
	public String testCookie(Model m ,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
			Cookie cookie = new Cookie("userName", "helloworld");
			cookie.setMaxAge(1000);
			
			response.addCookie(cookie);
			
			
		return "hello";
	}
	
	
}
