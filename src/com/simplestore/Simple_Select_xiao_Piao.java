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

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

@WebServlet(description = "µ¥µê²éÑ¯Ð¡Æ±", urlPatterns = { "/Simple_Select_xiao_Piao" })
public class Simple_Select_xiao_Piao extends HttpServlet {
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
		Connection conn = GetConnection.getStoreConn();
		String cSheetNo=request.getParameter("cSaleSheetno");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String GoodsMessage=request.getParameter("GoodsMessage");
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			if(String_Tool.isEmpty(cSheetNo)){
				past = conn.prepareStatement("select dSaleDate,cSaleSheetno,cGoodsNo,cBarCode,cGoodsName,fQuantity,fLastSettle,cOperatorno ,cOperatorName from t_SaleSheetDetail  where (cBarCode=? or cGoodsName=? or cGoodsNo=? or cSaleSheetno=?) and dSaleDate between ? and ? ");
			    past.setString(1, GoodsMessage);
			    past.setString(2, GoodsMessage);
			    past.setString(3, GoodsMessage);
			    past.setString(4, GoodsMessage);
			    past.setString(5, startTime);
			    past.setString(6, endTime);
			}else{
				past = conn.prepareStatement("select * from t_SaleSheetDetail  where  cSaleSheetno=? ");
				past.setString(1, cSheetNo);
			}
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeAll(rs, past, null, conn);
		}
		out.flush();
		out.close();
	}
}
