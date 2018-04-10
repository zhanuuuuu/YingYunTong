package Offline_Pos;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;
import com.google.gson.Gson;

import DB.DB;
import DB.GetConnection;
import ModelRas.MD5key;
import Tool.String_Tool;

@WebServlet(description = "储值卡扣款", urlPatterns = { "/p_MoneyCard_Done" })
public class p_MoneyCard_Done extends HttpServlet {
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
		Connection conn = null;
		CallableStatement c = null;
		ResultSet rs = null;
		// @cardno varchar(32),
		// @fCustmoney money,
		// @cWHno varchar(32),
		// @dSaleDate datetime,
		// @cSaleSheetNo varchar(32),
		// @cOperNo varchar(32),
		// @dSaleDatetime varchar(32),
		// @cPosNo varchar(32),
		// @fMoney_o money,
		// @fLeftMoney_o money,@fLeftMoney money
		String cardno = request.getParameter("cardno");
		String fCustmoney = request.getParameter("fCustmoney");
		String cWHno = request.getParameter("cWHno");
		String dSaleDate = String_Tool.DataBaseYear_Month_Day();
		String cSaleSheetNo = request.getParameter("cSaleSheetNo");
		String cOperNo = request.getParameter("cOperNo");
		String dSaleDatetime = String_Tool.DataBaseTime() ;
		String cPosNo = request.getParameter("cPosNo");
		String fMoney_o = request.getParameter("fMoney_o");
		String fLeftMoney_o = request.getParameter("fLeftMoney_o");
		String sign = request.getParameter("sign");
		LoggerUtil.info(cardno);
		LoggerUtil.info(fCustmoney);
		LoggerUtil.info(cWHno);
		LoggerUtil.info(dSaleDate);
		LoggerUtil.info(cSaleSheetNo);
		LoggerUtil.info(cOperNo);
		LoggerUtil.info(dSaleDatetime);
		LoggerUtil.info(cPosNo);
		LoggerUtil.info(fMoney_o);
		LoggerUtil.info(fLeftMoney_o);
		HashMap<String, String> map=new HashMap<String, String>();
		String str="";
		if (MD5key.getMD5Pass(cardno + "ware13391810430").equals(sign)) {
			try {
				conn = GetConnection.getStoreConn();
				conn.setAutoCommit(false);  
				c = conn.prepareCall("{call p_MoneyCard_Done (?,?,?,?,?,?,?,?,?,?,?)}");
				c.setString(1, cardno);
				c.setString(2, fCustmoney);
				c.setString(3, cWHno);
				c.setString(4, dSaleDate);
				c.setString(5, cSaleSheetNo);
				c.setString(6, cOperNo);
				c.setString(7, dSaleDatetime);
				c.setString(8, cPosNo);
				c.setString(9, fMoney_o);
				c.setString(10, fLeftMoney_o);
				c.registerOutParameter("iReturn", java.sql.Types.LONGNVARCHAR);
				c.execute();
				int retCode = c.getInt("iReturn");

				map.put("result", ""+retCode);//
				Gson gson=new Gson();
				List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
				list.add(map);
				str=gson.toJson(list);
	            out.print("{\"resultStatus\":\"" + retCode + "\"," + "\"dData\":" + str + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + retCode + "\"," + "\"dData\":" + str + "}");
				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				try {
					map.put("result", "-1");//成功
					Gson gson=new Gson();
					List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
					list.add(map);
					str=gson.toJson(list);
					out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dData\":" + str + "}");
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				LoggerUtil.info("关闭连接");
				DB.closeRs_Con(rs, c, conn);
			}
		} else {
			map.put("result", "2");//
			Gson gson=new Gson();
			List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
			list.add(map);
			str=gson.toJson(list);
			out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dData\":" + str + "}");
		}
		out.flush();
		out.close();

	}

}
