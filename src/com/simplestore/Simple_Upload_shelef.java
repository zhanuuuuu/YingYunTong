package com.simplestore;
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
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.String_Tool;

@WebServlet(description = "打印价签", urlPatterns = { "/Simple_Upload_shelef" })
public class Simple_Upload_shelef extends HttpServlet {
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
		String data = request.getParameter("data");
		LoggerUtil.info(data);
		Connection conn = GetConnection.getStoreConn();
		try {
			JSONArray array = new JSONArray(data);
			PreparedStatement past = conn.prepareStatement(
					" INSERT  INTO t_ShelfGoods (cShelfNo,cShelfName,cShelfPart,cGoodsNo,dInDate,cGoodsName,cBarcode,cInTime) values (?,?,?,?,?,?,?,?)");
			PreparedStatement past1 = conn.prepareStatement(
					"INSERT INTO t_GoodsPrint_wei (cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fNormalPrice,fVipPrice,fCKPrice,cZoneNo,cZoneName,Isprint,fquantity)values(?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);

				PreparedStatement pasts = conn.prepareStatement("select * from t_ShelfGoods  where cGoodsNo =? and cShelfNo=?  ");
				pasts.setString(1, obj.getString("cGoodsNo"));
				pasts.setString(2, obj.getString("cShelfNo"));
				ResultSet rs = pasts.executeQuery();
				if (rs.next()) {
					LoggerUtil.info("已经插入了就不插入了");
				} else {
					past.setString(1, obj.getString("cShelfNo"));
					past.setString(2, obj.getString("cShelfName"));
					past.setString(3, "");
					past.setString(4, obj.getString("cGoodsNo"));
					past.setString(5, String_Tool.DataBaseYear_Month_Day());
					past.setString(6, obj.getString("name"));
					past.setString(7, obj.getString("cBarcode"));
					past.setString(8, String_Tool.DataBaseH_M_S());
					past.addBatch();
				}
				DB.closeResultSet(rs);
				DB.closePreparedStatement(pasts);
				
				
				PreparedStatement pasts1 = conn.prepareStatement("select * from t_GoodsPrint_wei  where cGoodsNo =? and cZoneNo=?  ");
				pasts1.setString(1, obj.getString("cGoodsNo"));
				pasts1.setString(2, obj.getString("cShelfNo"));
				ResultSet rs1 = pasts1.executeQuery();
				
				if(rs1.next()) {
					LoggerUtil.info("已经插入了就不插入了");
				}else {
					past1.setString(1, obj.getString("cGoodsNo"));
					past1.setString(2, obj.getString("name"));
					past1.setString(3, obj.getString("cBarcode"));
					past1.setString(4, obj.getString("cUnit"));// obj.getString("cUnit")
					past1.setString(5, obj.getString("cSpec"));
					past1.setString(6, obj.getString("fNormalPrice"));
					past1.setString(7, obj.getString("fCKPrice"));// obj.getString("fVipPrice")
					past1.setString(8, obj.getString("fCKPrice"));
					past1.setString(9, obj.getString("cShelfNo"));
					past1.setString(10, obj.getString("cShelfName"));
					past1.setString(11, "0");
					past1.setString(12, obj.getString("fQty"));
					past1.addBatch();
				}
				DB.closeResultSet(rs1);
				DB.closePreparedStatement(pasts1);
			}
			
			
			past.executeBatch();
			past1.executeBatch();
			out.print("{\"resultStatus\":\"" + 1 + "\"}");
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -1 + "\"}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
