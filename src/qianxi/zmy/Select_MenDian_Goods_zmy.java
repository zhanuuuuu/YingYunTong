package qianxi.zmy;

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

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

/**
 * Servlet implementation class Select_MenDian_Goods_zmy
 */
@WebServlet(description = "下载门店商品信息", urlPatterns = { "/Select_MenDian_Goods_zmy" })
public class Select_MenDian_Goods_zmy extends HttpServlet {
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
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement c = null;
		String Number_of_pages=request.getParameter("Number_of_pages");
		String cStoreNo=request.getParameter("cStoreNo");
		
		try {
			
			if (Number_of_pages==null || cStoreNo==null ){
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" +null + "}");
				
				return;
			}else{
			conn = GetConnection.getStoreConn();
			c = conn.prepareCall(" {call Select_MenDian_Goods_ZMY(?,?,?) }");
			c.setString(1, cStoreNo);
			c.setString(2, Number_of_pages);
			// 0 表示门店更新商品；1 表示 总部更新商品
			if(cStoreNo.equals("000")){
				
				c.setInt(3,1);	
			}else{
				c.setInt(3,0);	
			}
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"resultStatus\":\"" + -2 + "\"," + "\"dDate\":" + null + "}");
		} finally {
			DB.closeResultSet(rs);
		    DB.closeCallState(c);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}

}
