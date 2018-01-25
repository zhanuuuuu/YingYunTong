package qianxi.zmy;

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

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

/**
 * Servlet implementation class selectStoreReturnGoodsBySheetno
 */
@WebServlet(description = "�ŵ��˻����ݵ��Ų�ѯ������Ʒ", urlPatterns = { "/selectStoreReturnGoodsBySheetno" })
public class selectStoreReturnGoodsBySheetno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String sheetno=request.getParameter("cSheetno");
		Connection conn=null;
		PreparedStatement past=null;
		ResultSet rs=null;
		
		try {
			conn=GetConnection.getStoreConn();
			if (sheetno!=null) {
				
				String sql = " { call selectStoreReturnGoods (?)}"; // //
				past = conn.prepareStatement(sql);
				past.setString(1, sheetno);
				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} 
			else {
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" + null + "}");
			}
		
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -2 + "\"," + "\"dDate\":" + null + "}");  //����ϵͳ�쳣
			e.printStackTrace();
		}finally {
			DB.closeRs_Con(rs, null, conn);
			out.flush();
			out.close();
		}
		
	}


}
