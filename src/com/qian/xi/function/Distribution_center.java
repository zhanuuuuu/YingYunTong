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
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "查询所有的配送中心", urlPatterns = { "/Distribution_center" })
public class Distribution_center extends HttpServlet {
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
		PreparedStatement past=null;
		ResultSet rs=null;
		Connection conn=GetConnection.getStoreConn();
		try{
			past=conn.prepareStatement("select * from t_PsStore");
			rs=past.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array!=null&&array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.DB.closeResultSet(rs);
			DB.DB.closePreparedStatement(past);
			DB.DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}

}
