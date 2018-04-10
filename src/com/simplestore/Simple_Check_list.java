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

@WebServlet(description = "更新任务子单", urlPatterns = { "/Simple_Check_list" })
public class Simple_Check_list extends HttpServlet {
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
		String cStoreNo = request.getParameter("cStoreNo");
		String action = request.getParameter("action");
		LoggerUtil.info(cStoreNo);
		LoggerUtil.info(action);
		Connection conn = GetConnection.getStoreConn();
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			String sql = "";
			if (action != null && action.equals("RQCK")) { // 更新任务
				sql = "select cCheckTask,cCheckTaskNo,dCheckTask from t_CheckTast  where dCheckTask>=cast(CONVERT(VARCHAR(10),GETDATE()-1,120) as datetime)  ";
				past = conn.prepareStatement(sql);
			} else { // 跟新子单
				sql = " select a.cCheckTaskNo,a.cCheckTask,a.cZoneNo,a.cZoneName from t_CheckTast_zone a,t_CheckTast b where a.cCheckTaskNo=b.cCheckTaskNo and  b.dCheckTask>=cast(CONVERT(VARCHAR(10),GETDATE()-1,120) as datetime) ";
				past = conn.prepareStatement(sql);
			}
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"data\":" + "" + "}");
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}
}
