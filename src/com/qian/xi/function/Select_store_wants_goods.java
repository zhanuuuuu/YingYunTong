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
import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "门店要货商品(配送中心掉采购单验货商品,并根据商品确定这个商品每个店铺要多少)", urlPatterns = { "/Select_store_wants_goods" })
public class Select_store_wants_goods extends HttpServlet {
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
		String cStoreNo=request.getParameter("cStoreNo");
		String cGroupTypeNo=request.getParameter("cGroupTypeNo");
		CallableStatement c = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = GetConnection.getStoreConn();
			c = conn.prepareCall("{call p_StoreGoodsCollect_w (?,?)}");
			c.setString(1, cStoreNo);
			c.setString(2, cGroupTypeNo);
			rs = c.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
			DB.closeRs_Con(rs, c, conn);
		}
	}
}
