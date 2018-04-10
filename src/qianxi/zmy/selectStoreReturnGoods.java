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
 * Servlet implementation class selectStoreReturnGoods
 */
@WebServlet(description = "门店查询退货的单据号，根据单据号找到商品", urlPatterns = { "/selectStoreReturnGoods" })
public class selectStoreReturnGoods extends HttpServlet {
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
		String storeNo=request.getParameter("cStoreNo");
		Connection conn=null;
		PreparedStatement past=null;
		ResultSet rs=null;
		
		try {
			conn=GetConnection.getStoreConn();
			if (storeNo!=null) {
				
				String sql = "{ call selectStoreReturnGoodsSheetno (?)}"; // //
				past = conn.prepareStatement(sql);
				past.setString(1, storeNo);
				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} 
			else {
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" + null + "}");
			}
		
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -2 + "\"," + "\"dDate\":" + null + "}");  //代表系统异常
			e.printStackTrace();
		}finally {
			DB.closeRs_Con(rs, null, conn);
			out.flush();
			out.close();
		}
		
	}

}
