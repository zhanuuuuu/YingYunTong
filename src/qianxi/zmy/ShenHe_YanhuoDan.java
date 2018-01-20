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

import DB.GetConnection;
import Tool.ResultSet_To_JSON;

/**
 * Servlet implementation class ShenHe_YanhuoDan
 */
@WebServlet(description = "根据单号审核验货单（手机审核功能）", urlPatterns = { "/ShenHe_YanhuoDan" })
public class ShenHe_YanhuoDan extends HttpServlet {
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
		
		String cSheetno=request.getParameter("cSheetno");
		
		if(cSheetno!=null){
			
			PreparedStatement past=null;
			Connection conn=GetConnection.getStoreConn();
			
			try{
				conn.setAutoCommit(false);
				past=conn.prepareStatement("UPDATE WH_StockVerify "+
										"	SET bExamin= 1     "+
										"	WHERE cSheetno=? ");
				past.setString(1, cSheetno);
				
				int updateCount=past.executeUpdate();
				
				if(updateCount>0){
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + cSheetno + "}");  
				}
				else{
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + cSheetno + "}");  
				}
				conn.commit();
				conn.setAutoCommit(true);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				
				DB.DB.closePreparedStatement(past);
				DB.DB.closeConn(conn);
			}
			
		}else{
			out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" + null + "}");
		}
		
		out.flush();
		out.close();
		
	}

}
