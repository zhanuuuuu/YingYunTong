package com.qian.xi.function;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import Tool.String_Tool;

@WebServlet(description = "²éÑ¯ÅÌµãÅäÖÃ", urlPatterns = { "/Select_Pan_Dian_t_Config" })
public class Select_Pan_Dian_t_Config extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		PreparedStatement past=null;
		ResultSet rs=null;
		Connection conn=GetConnection.getStoreConn();
		String cStoreNo=request.getParameter("cStoreNo");
		LoggerUtil.info(cStoreNo);
		try{
			if(String_Tool.isEmpty(cStoreNo)){
				past=conn.prepareStatement("select * from t_Config where cID='ÌõÂë³Ó'  ");
		        rs=past.executeQuery();
			}
			else{
				past=conn.prepareStatement("select * from t_Config where cStoreNo=? and cID='ÌõÂë³Ó'  ");
				past.setString(1, cStoreNo);
		        rs=past.executeQuery();
			}
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array!=null&&array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
