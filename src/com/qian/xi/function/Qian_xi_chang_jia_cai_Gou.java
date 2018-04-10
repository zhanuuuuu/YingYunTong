package com.qian.xi.function;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DBYan_Huo_update;
import DB.GetConnection;

@WebServlet(description = "千禧厂家采购", urlPatterns = { "/Qian_xi_chang_jia_cai_Gou" })
public class Qian_xi_chang_jia_cai_Gou extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		String cStoreNo = request.getParameter("cStoreNo");
		String cStoreName = request.getParameter("cStoreName");
		LoggerUtil.info(cStoreNo);
		LoggerUtil.info(cStoreName);
		LoggerUtil.info(data);
		try {
			JSONArray array = new JSONArray(data);// 千禧厂家采购
			boolean a = DBYan_Huo_update.Qianxi_Chang_Jia_Cai_Gou(GetConnection.getStoreConn(), array, cStoreNo,
					cStoreName);
			if (a) {
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"" + "}");
				out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
			} else {
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"" + "}");
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			}
		} catch (Exception e) {
			LoggerUtil.info("{\"resultStatus\":\"" + "服务器异常" + "\"" + "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
