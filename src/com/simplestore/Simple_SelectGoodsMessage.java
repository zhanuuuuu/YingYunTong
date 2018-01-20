package com.simplestore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;


@WebServlet(description = "��Ʒ��Ϣ��ѯ", urlPatterns = { "/Simple_SelectGoodsMessage" })
public class Simple_SelectGoodsMessage extends HttpServlet {
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
		String cBarCode=request.getParameter("cBarCode");
		CallableStatement c=null;
		try{
			c=conn.prepareCall("{Call P_x_SetCheckWh_byGoodsType_log_App(?,?,?)}");
			c.setString(1, Start_Time);
			c.setString(2, End_Time);
			c.setString(3, cBarCode);
			rs=c.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array!=null&&array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
			}
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
