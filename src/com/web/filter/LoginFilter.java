package com.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import Tool.ReadConfig;

//@WebFilter(urlPatterns = "/LoginFilter",asyncSupported = true) 
public class LoginFilter implements Filter {
	
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		chain.doFilter(req, res);
//		Object user = req.getSession().getAttribute("user");
//		if(user == null) {
//			req.getRequestDispatcher("/Login.jsp").forward(req, response);
//		} else {
//			chain.doFilter(request, response);
//		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		//控制日志打印的
		
		String str=new ReadConfig().getprop().getProperty("Logger");
		System.out.println(str);
		LoggerUtil.info(str);
		boolean b=Boolean.parseBoolean(str);
		LoggerUtil.setLogger(b);
	}
}
