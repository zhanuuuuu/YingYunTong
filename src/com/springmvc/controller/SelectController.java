package com.springmvc.controller;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

@Controller
public class SelectController {

	@RequestMapping("/Spring_Select_operation") // 和下面含义相同
	public void Pos_Monitor_Oper(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String name = request.getParameter("name");

		Connection conn1 = GetConnection.getStoreConn();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			PreparedStatement past1 = conn1.prepareStatement("select posip from t_Posstation where posname= ? ");
			past1.setString(1, name);
			ResultSet rs0 = past1.executeQuery();
			String posip = null;
			if (rs0.next()) {
				posip = rs0.getString("posip");
			}
			DB.closeRs_Con(rs0, past1, conn1);
			conn = GetConnection.getPosstationConn(posip);
			past = conn.prepareStatement("select * from Pos_Monitor_Oper where dDatetime between ? and  ? ");
			past.setString(1, starttime);
			past.setString(2, endtime);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			out.flush();
			out.close();
			DB.closeRs_Con(rs, past, conn);
		}
	}

	@RequestMapping(value = "/Spring_Select_cashierdesk")
	public void cashierdesk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String cStoreNo = request.getParameter("cStoreNo");
		LoggerUtil.info(cStoreNo);
		Connection conn = GetConnection.getStoreConn();
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement("select * from t_Posstation where cStoreNo=?");
			past.setString(1, cStoreNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		out.flush();
		out.close();
	}

	@RequestMapping("/Spring_Select_All_Supper") // 查询所有的商家
	public void Spring_Select_All_Supper(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = GetConnection.getStoreConn();
		ResultSet rs = null;
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement("select cSupNo,cSupName from t_Supplier ");
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
		out.flush();
		out.close();
	}
	
	
	@RequestMapping(value = "/error_404", produces = "text/html;charset=UTF-8")  
	@ResponseBody  
	public String error_404() throws Exception {   
	     return "{\"msg\":\"找不到页面\",\"code\":\"1000001\"}";  
	}  
	/** 
	 * 服务器异常 
	 * @return 
	 * String 
	 */  
	@RequestMapping(value ="/error_500", produces = "text/html;charset=UTF-8")  
	public String error_500() {   
		         return "{\"msg\":\"服务器处理失败\",\"code\":\"1000002\"}";  
	}  

}
