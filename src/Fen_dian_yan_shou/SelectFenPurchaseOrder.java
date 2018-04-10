package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.Fen_dian_Update;
import DB.GetConnection;

public class SelectFenPurchaseOrder extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
/*
 * (non-Javadoc)
 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * ²É¹ºµ¥
 */

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
			
			LoggerUtil.info(dBeginDate);
			LoggerUtil.info(dEndDate);
			LoggerUtil.info(cStoreNo);

			JSONArray array = Fen_dian_Update.Select_Zhi_Pei_Supplier(GetConnection.getStoreConn(), cStoreNo,
					dBeginDate, dEndDate);

			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");

			}
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");

		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
