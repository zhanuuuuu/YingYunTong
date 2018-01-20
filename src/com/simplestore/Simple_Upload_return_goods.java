package com.simplestore;
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
import org.json.JSONObject;
import DB.DB;
import DB.GetConnection;
import Tool.GetcSheetno;
import Tool.ReadConfig;
import Tool.String_Tool;


@WebServlet(description = "提交退货", urlPatterns = { "/Simple_Upload_return_goods" })
public class Simple_Upload_return_goods extends HttpServlet {
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
		String data = request.getParameter("data");
		System.out.println(data);
		try {
			JSONArray array=new JSONArray(data);
			boolean a =insert_into_tui_huo(GetConnection.getStoreConn(), array);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			} 
			else {
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\""+ "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	public boolean insert_into_tui_huo(Connection conn, JSONArray array) { // 提交分店退货
		PreparedStatement past = null;
		String strrandom = null;
		double Sum_Money = 0;
		PreparedStatement past1 = null;
		try {
			conn.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			JSONObject obj = array.getJSONObject(0);
			//dbo.f_GenRbdSheetno(@cYear,@cSupplierNo))
			PreparedStatement past_cSheetNo = conn.prepareStatement(
					"select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource") + ".dbo.f_GenRbdSheetno('"+ String_Tool.DataBaseYear() + "','" + obj.getString("cSupplierNo") + "')");
			ResultSet rs1 = past_cSheetNo.executeQuery();
			if (rs1.next()) {
				strrandom = rs1.getString("SheetNo");
			}
			System.out.println(strrandom);
			
			
			past1 = conn.prepareStatement(
					"INSERT INTO wh_RbdWarehouseDetail (cSheetno,iLineNo,cGoodsName,cBarcode,fQuantity,fInPrice,fInMoney,dProduct,cGoodsNo,cUnit) values (?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);

				past1.setString(1, "" + strrandom);
				past1.setString(2, "" + (i + 1));
				past1.setString(3, obj1.getString("cGoodsName"));
				past1.setString(4, obj1.getString("cBarcode"));
				past1.setString(5, obj1.getString("fQuantity"));
				double fQuantity=Double.parseDouble(obj1.getString("fQuantity"));
				double fInPrice= Double.parseDouble(obj1.getString("fInPrice"));
				double fInMoney=fQuantity*fInPrice;
				past1.setString(6, "" + (Double.parseDouble(obj1.getString("fInPrice"))));
				past1.setString(7, "" + Double.parseDouble(obj1.getString("fInMoney")));
				Sum_Money = fInMoney + Sum_Money;
				past1.setString(8, String_Tool.DataBaseTime());
				past1.setString(9, obj1.getString("cGoodsNo"));
				past1.setString(10, obj1.optString("cUnit"));//
				past1.addBatch();
				if (i % 200 == 0) {
					past1.executeBatch();
				}
			}
			past1.executeBatch();
			DB.closePreparedStatement(past1);
			past = conn.prepareStatement(
					"INSERT INTO wh_RbdWarehouse (dDate,cSheetno,cSupplierNo,cSupplier,cOperatorNo,cOperator,cWhNo,cWh,dFillin,cFillinTime,cTime,cFillEmpNo,cFillEmp,bExamin,fMoney) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, "" + strrandom);
			past.setString(3, obj.getString("cSupplierNo"));
			past.setString(4, obj.getString("cSupplier"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperator"));
			past.setString(7, obj.optString("cWhNo"));
			past.setString(8, obj.optString("cWh"));
			past.setString(9, String_Tool.DataBaseYear_Month_Day()); // dFillin,cFillinTime,cTime
			past.setString(10, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(11, String_Tool.DataBaseH_M_S()); // dFillin,cFillinTime,cTime
			past.setString(12, obj.getString("cOperatorNo")); // dFillin,cFillinTime,cTime
			past.setString(13, obj.getString("cOperator"));
			past.setString(14, obj.optString("bExamin"));
			past.setString(15, "" + Sum_Money);
			past.execute();
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
				e.printStackTrace();
				return false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return false;
	}
}
