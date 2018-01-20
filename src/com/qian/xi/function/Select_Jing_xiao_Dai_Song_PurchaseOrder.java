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
@WebServlet(description = "千禧直通验货", urlPatterns = { "/Select_Jing_xiao_Dai_Song_PurchaseOrder" })
public class Select_Jing_xiao_Dai_Song_PurchaseOrder extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String dBeginDate = request.getParameter("dBeginDate");
			String dEndDate = request.getParameter("dEndDate"); //
			String cStoreNo = request.getParameter("cStoreNo"); //
			String cSheetStateNo=request.getParameter("cSheetStateNo");
			/*
			 * cSheetStateNo=1代表经销代送订单。
			 */
			System.out.println(dBeginDate);
			System.out.println(dEndDate);
			System.out.println(cStoreNo);
			System.out.println(cSheetStateNo);
			JSONArray array = Select_Zhi_Pei_Supplier(GetConnection.getStoreConn(), cStoreNo,dBeginDate, dEndDate,cSheetStateNo);

			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	public static JSONArray Select_Zhi_Pei_Supplier(Connection storeConn, String cStoreNo, String dBeginDate,
			String dEndDate,String cSheetStateNo) {
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = storeConn.prepareStatement(
					"select cSupplierNo,cSupplier,cSheetno,cStoreNo,cStoreName from WH_Stock where dDate between ? and ? and bExamin=1 and  isnull(bReceive,0)='0' and isnull(bFresh,0)=0  and  iSheetType= ?  and cStoreNo in (select cStoreNo from t_store where cPsStoreNo=?)    ");// and  iSheetType= ? 																				// bExamin='1'
			past.setString(1, dBeginDate);
			past.setString(2, dEndDate);
			past.setString(3, cSheetStateNo);
			past.setString(4, cStoreNo);
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
