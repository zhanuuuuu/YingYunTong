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

import com.google.gson.Gson;

import DB.DB;
import DB.GetConnection;
import ModelRas.MD5key;
import Tool.String_Tool;

@WebServlet(description = "会员存款", urlPatterns = { "/Vip_Save" })
public class Vip_Save extends HttpServlet {
	
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
	
		PrintWriter out = response.getWriter();
		Connection conn = null;
		CallableStatement c = null;
		ResultSet rs = null;
		// @cVipNo varchar(32),
		// @dSaledate datetime,
		// @cSalesheetNo varchar(32),
		// @fMoney_save money,
		// @cOpertorNo varchar(32),
		// @cOperName varchar(32),
		// @dSaveTime datetime,
		// @cPostion varchar(32),
		// @cStyle varchar(32),
		// @cStoreNo varchar(32)
		String cVipNo = request.getParameter("cVipNo");
		String dSaledate = String_Tool.DataBaseYear_Month_Day();
		String cSalesheetNo = request.getParameter("cSalesheetNo");
		String fMoney_save = request.getParameter("fMoney_save");
		String cOpertorNo = request.getParameter("cOpertorNo");
	
		String cOperName = new String(request.getParameter("cOperName").getBytes("ISO8859-1"), "UTF-8");
		cOperName=  java.net.URLDecoder.decode(cOperName,"GBK");  
		
		String dSaveTime = String_Tool.DataBaseTime();
		String cPostion = request.getParameter("cPostion");
		String cStyle = request.getParameter("cStyle");
		cStyle = new String(request.getParameter("cStyle").getBytes("ISO8859-1"), "UTF-8");;
		cStyle=  java.net.URLDecoder.decode(cStyle,"GBK");  
		String cStoreNo = request.getParameter("cStoreNo");
		String sign = request.getParameter("sign");
		System.out.println(cVipNo);
		System.out.println(dSaledate);
		System.out.println(cSalesheetNo);
		System.out.println(fMoney_save);
		System.out.println(cOpertorNo);
		System.out.println(cOperName);
		System.out.println(dSaveTime);
		System.out.println(cPostion);
		System.out.println(cStyle);
		System.out.println(cStoreNo);
		System.out.println(sign);
		String str = "";
		HashMap<String, String> map = new HashMap<String, String>();
		if (MD5key.getMD5Pass(cVipNo + "ware13391810430").equals(sign)) {
			try {
				conn = GetConnection.getStoreConn();
				conn.setAutoCommit(false);
				c = conn.prepareCall("{call p_Vip_Save (?,?,?,?,?,?,?,?,?,?,?)}");
				c.setString(1, cVipNo);
				c.setString(2, dSaledate);
				c.setString(3, cSalesheetNo);
				c.setString(4, fMoney_save);
				c.setString(5, cOpertorNo);
				c.setString(6, cOperName);
				c.setString(7, dSaveTime);
				c.setString(8, cPostion);
				c.setString(9, cStyle);
				c.setString(10, cStoreNo);
				c.registerOutParameter("iReturn", java.sql.Types.LONGNVARCHAR);
				c.execute();
				int retCode = c.getInt("iReturn");

				map.put("cVipNo", cVipNo);
				map.put("result", "" + retCode);
				map.put("cOperName", "" + cOperName);
				Gson gson = new Gson();
				List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				list.add(map);
				str = gson.toJson(list);
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":" + str + "}");
				
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":" + str + "}");
				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				map.put("cVipNo", cVipNo);
				map.put("result", "-1");
				Gson gson = new Gson();
				List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				list.add(map);
				str = gson.toJson(list);
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dData\":" + str + "}");
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				System.out.println("关闭连接");
				DB.closeRs_Con(rs, c, conn);
			}
		} else {
			map.put("cVipNo", cVipNo);
			map.put("result", "2");
			Gson gson = new Gson();
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			list.add(map);
			str = gson.toJson(list);
			out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dData\":" + str + "}");
			
		}
		out.flush();
		out.close();
	}

}
