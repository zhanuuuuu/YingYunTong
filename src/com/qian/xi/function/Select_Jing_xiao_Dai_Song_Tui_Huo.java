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
import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

@WebServlet(description = "经销代送退货", urlPatterns = { "/Select_Jing_xiao_Dai_Song_Tui_Huo" })
public class Select_Jing_xiao_Dai_Song_Tui_Huo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String dBeginDate = request.getParameter("dBeginDate");
			String dEndDate = request.getParameter("dEndDate"); 
			String cStoreNo = request.getParameter("cStoreNo"); 
			String cSheetNo = request.getParameter("cSheetNo");
			System.out.println(dBeginDate);
			System.out.println(dEndDate);
			System.out.println(cStoreNo);
			System.out.println("单号为null为查商品"+cSheetNo);
			JSONArray array = null;
			if (String_Tool.isEmpty(cSheetNo)) {
				array = Tui_Huo(GetConnection.getStoreConn(), cStoreNo, dBeginDate, dEndDate);
			} else {
				array = Tui_Huo_Goods(GetConnection.getStoreConn(),cSheetNo);
			}
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");

			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");

		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	private JSONArray Tui_Huo_Goods(Connection storeConn, String cSheetNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement("select * from wh_RbdWarehouseDetail where cSheetno=?");
			past.setString(1, cSheetNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(storeConn);
		}
		return null;
	}

	public static JSONArray Tui_Huo(Connection storeConn, String cStoreNo, String dBeginDate, String dEndDate) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement("select cSupplierNo,cSupplier,cSheetno from wh_RbdWarehouse where dDate between ? and ? and cStoreNo<> ? and bExamin='1' and  isnull(bReceive,0)='0'  ");// and																																	// bExamin='1'
			past.setString(1, dBeginDate);
			past.setString(2, dEndDate);
			past.setString(3, cStoreNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(storeConn);
		}
		return null;
	}

}
