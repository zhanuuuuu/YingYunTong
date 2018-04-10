package com.simplestore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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

@WebServlet(description = "查询总部所有的信息", urlPatterns = { "/Simple_Select_Goods" }, asyncSupported = true)
public class Simple_Select_Goods extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		AsyncContext actx = request.startAsync();
		actx.setTimeout(30 * 3000);
		actx.start(new MyThread(actx));

	}

	class MyThread implements Runnable {
		private AsyncContext actx;

		// 构造
		public MyThread(AsyncContext actx) {
			this.actx = actx;
		}

		public void run() {
			try {
				// 等待5秒，模拟处理耗时的业务
				Thread.sleep(4 * 1000);
				ServletRequest request = actx.getRequest();
				ServletResponse response = actx.getResponse();
				PrintWriter out = response.getWriter();
				Connection conn = GetConnection.getStoreConn();
				String Number_of_pages = request.getParameter("Number_of_pages");
				LoggerUtil.info(Number_of_pages);
				PreparedStatement past = null;
				ResultSet rs = null;
				try {
					String sql = String_Tool.sql("cGoodsNo",
							"select a.cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fNormalPrice,fCKPrice,cSupNo,fQty_Cur=b.EndQty from t_Goods a left join t_goodsKuCurQty_wei b on a.cGoodsNo=b.cGoodsNo where cBarcode not like '%X%'",
							Integer.parseInt(Number_of_pages));
					past = conn.prepareStatement(sql);
					rs = past.executeQuery();
					JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
					if (array.length() > 0) {
						out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString() + "}");
					} else {
						out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
					}
					System.err.println("数组的长度" + array.length());
				} catch (Exception e) {
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":\"" + 0 + "\"}");
					e.printStackTrace();
				} finally {
					out.flush();
					out.close();
					DB.closeRs_Con(rs, past, conn);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
