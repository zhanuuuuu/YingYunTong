package com.springmvc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class DefaultExceptionHandler implements HandlerExceptionResolver {
	
	private static final Log logger = LogFactory.getLog(DefaultExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) {
		ModelAndView mv = new ModelAndView();
		/* ʹ��response���� */
		response.setStatus(HttpStatus.OK.value()); // ����״̬��
		response.setContentType(MediaType.APPLICATION_JSON_VALUE); // ����ContentType
		response.setCharacterEncoding("UTF-8"); // ��������
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		try {
			response.getWriter().write("{\"success\":false,\"msg\":\"" + ex.getMessage() + "\"}");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.debug("�쳣:" + ex.getMessage(), ex);
		return mv;
	}
}
