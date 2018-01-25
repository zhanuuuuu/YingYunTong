package qianxi.zmy;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DB.DB;
import DB.GetConnection;
import Tool.String_Tool;

/**
 * Servlet implementation class storeSureSendToDriver
 */
@WebServlet(description = "门店接收员确认发货给司机", urlPatterns = { "/storeSureSendToDriver" })
public class storeSureSendToDriver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("content-type", "text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		
		String cStoreOperaterno=request.getParameter("cStoreOperaterno");
		
		String cSheetno=request.getParameter("cSheetno");
		Connection conn=null;
		PreparedStatement ps=null;
		
		
		conn=GetConnection.getStoreConn();
		
		try {
			if(cStoreOperaterno==null || cSheetno==null){
				
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" +null+ "}");
				return;
			}
		
			conn.setAutoCommit(false);
			ps= conn.prepareStatement(
					"UPDATE WH_cStoreReturnGoods "+
				"	SET cStoreState=1,cStoreOperaterno=?,cStoredate=? "+
				"	WHERE cSheetno=?");
			ps.setString(1, cStoreOperaterno);
			ps.setString(2, String_Tool.DataBaseTime());
			ps.setString(3, cSheetno);
			
//			for (int i = 0; i < array.length(); i++) {
//				JSONObject obj1 = array.getJSONObject(i);
//				ps.setString(1, ""  );
//				ps.setString(2, "" + (i + 1));
//				
//				if(i%200==0){
//					ps.addBatch();
//				}
//			} 
//			ps.executeBatch();
			ps.execute();
			DB.closePreparedStatement(ps);
			
			conn.commit();
			conn.setAutoCommit(true);
			
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + null + "}");
			
		} catch (SQLException e) {
			
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			out.print("{\"resultStatus\":\"" + -2 + "\"," + "\"dDate\":" + null + "}");
			e.printStackTrace();
		}finally{
			DB.closeConn(conn);
			out.flush();
			out.close();
		}
		
		
		
		
	
	}

}
