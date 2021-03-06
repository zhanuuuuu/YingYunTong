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

@WebServlet(description = "会员礼金扣款", urlPatterns = { "/p_Vip_Consume_Present" })  
public class p_Vip_Consume_Present extends HttpServlet {
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
		String cVipNo = request.getParameter("cVipNo");
		String dConsum = request.getParameter("dConsum");
		String cSalesheetNo = request.getParameter("cSalesheetNo");
		String fMoney_consume = request.getParameter("fMoney_consume");
		String cOpertorNo = request.getParameter("cOpertorNo");
		
		String cOpername=new String(request.getParameter("cOpername").getBytes("ISO8859-1"), "UTF-8");
		cOpername=java.net.URLDecoder.decode(cOpername,"GBK"); 
		
		String dOperate = request.getParameter("dOperate");
		String cPostion = request.getParameter("cPostion");
		String cStyle=new String(request.getParameter("cStyle").getBytes("ISO8859-1"), "UTF-8");
		cStyle=java.net.URLDecoder.decode(cStyle,"GBK");  
		
		String cDetail=new String(request.getParameter("cDetail").getBytes("ISO8859-1"), "UTF-8");
		cDetail=java.net.URLDecoder.decode(cDetail,"GBK"); 
		String sign = request.getParameter("sign");

		LoggerUtil.info(cVipNo);
		LoggerUtil.info(dConsum);
		LoggerUtil.info(cSalesheetNo);
		LoggerUtil.info(fMoney_consume);
		LoggerUtil.info(cOpertorNo);
		LoggerUtil.info(cOpername);
		LoggerUtil.info(dOperate);
		LoggerUtil.info(cPostion);
		LoggerUtil.info(cStyle);
		LoggerUtil.info(cDetail);
		LoggerUtil.info(sign);
		HashMap<String, String> map = new HashMap<String, String>();
		String str = "";
		if (MD5key.getMD5Pass(cVipNo + "ware13391810430").equals(sign)) {
			try {
				conn = GetConnection.getStoreConn();
				conn.setAutoCommit(false);
				c = conn.prepareCall("{call p_Vip_Consume_Present (?,?,?,?,?,?,?,?,?,?,?)}");
				c.setString(1, cVipNo);
				c.setString(2, dConsum);
				c.setString(3, cSalesheetNo);
				c.setString(4, fMoney_consume);
				c.setString(5, cOpertorNo);
				c.setString(6, cOpername);
				c.setString(7, dOperate);
				c.setString(8, cPostion);
				c.setString(9, cStyle);
				c.setString(10, cDetail);
				c.registerOutParameter("iReturn", java.sql.Types.LONGNVARCHAR);
				c.execute();
				int retCode = c.getInt("iReturn");

				map.put("result", "" + retCode);
				Gson gson = new Gson();
				List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				list.add(map);
				str = gson.toJson(list);

				out.print("{\"resultStatus\":\"" + retCode + "\"," + "\"dData\":" + str + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + retCode + "\"," + "\"dData\":" + str + "}");
				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				try {
					map.put("result", "-1");
					Gson gson = new Gson();
					List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
					list.add(map);
					str = gson.toJson(list);
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
			map.put("result", "-1");
			Gson gson = new Gson();
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			list.add(map);
			str = gson.toJson(list);
			out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dData\":\"" + "签名出错" + "\"}");
		}
		out.flush();
		out.close();
	
	}

}
