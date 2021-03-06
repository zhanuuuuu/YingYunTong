package com.qian.xi.function;
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
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

@WebServlet(description = "千禧直通验货", urlPatterns = { "/Qian_Xi_Select_Zhi_Tong_Yan_Huo" })
public class Qian_Xi_Select_Zhi_Tong_Yan_Huo extends HttpServlet {
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
		String data = request.getParameter("data");
		String cStoreNo = request.getParameter("cStoreNo");
		String cGroupTypeNo=request.getParameter("cGroupTypeNo");
		LoggerUtil.info(data);
		LoggerUtil.info(cStoreNo);
		try {
			conn = GetConnection.getStoreConn();
			if (String_Tool.isEmpty(data)) {// 查询采购单，供应商。
				c = conn.prepareCall(" {call p_StoreGoodsCollectSup_w(?,?) }");
				c.setString(1, cStoreNo);
				c.setString(2, cGroupTypeNo);
			} else {
				JSONObject obj = new JSONObject(data);
				c = conn.prepareCall(" {call select_cai_gou_goods(?) }");
				c.setString(1, obj.getString("cSheetno"));
			}
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
		    DB.closeCallState(c);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
