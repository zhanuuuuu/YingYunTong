package com.simplestore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "收银员报表", urlPatterns = { "/Simple_cashier_report_forms" })
public class Simple_cashier_report_forms extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		ResultSet rs=null;
		Connection conn=GetConnection.getStoreConn();
		String Start_Time=request.getParameter("Start_Time");
		String End_Time=request.getParameter("End_Time");
		LoggerUtil.info(Start_Time);
		LoggerUtil.info(End_Time);
		CallableStatement c=null;
		try{
			c=conn.prepareCall("{Call p_Account_Receiver('',?,?)}");
			c.setString(1,Start_Time);
			c.setString(2, End_Time);
			rs=c.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array!=null&&array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
			}
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeCallState(c);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
