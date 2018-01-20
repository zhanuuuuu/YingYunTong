package Offline_Pos;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
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

@WebServlet(description = "会员积分", urlPatterns = { "/Vip_AddScore_Pos" })

public class Vip_AddScore_Pos extends HttpServlet {
	
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
		PrintWriter out = response.getWriter();
		Connection conn = null;
		CallableStatement c = null;
		String cVipNo = request.getParameter("cVipNo");
		String fAddScore = request.getParameter("fAddScore");
		String sign = request.getParameter("sign");
		System.out.println(cVipNo);
		System.out.println(fAddScore);
		System.out.println(sign);
		String str="";
		HashMap<String, String> map=new HashMap<String, String>();
		if (MD5key.getMD5Pass(cVipNo + "ware13391810430").equals(sign)) {
			try {
				map.put("cVipNo", cVipNo);
				map.put("fAddScore", fAddScore);
				conn = GetConnection.getStoreConn();
				c = conn.prepareCall("{call p_Vip_AddScore_Pos (?,?)}");
				c.setString(1, cVipNo);
				c.setString(2, fAddScore);
				int a = c.executeUpdate();
				if (a == 1) {
					map.put("result", "0");//成功
					Gson gson=new Gson();
					List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
					list.add(map);
					str=gson.toJson(list);
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":" + str + "}");
					
					System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dData\":" + str + "}");
				} else {
					map.put("result", "1");//失败
					Gson gson=new Gson();
					List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
					list.add(map);
					str=gson.toJson(list);
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dData\":" + str + "}");
					
					System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dData\":" + str + "}");
				}
			} catch (Exception e) {
				map.put("result", "-1");
				Gson gson=new Gson();
				List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
				list.add(map);
				str=gson.toJson(list);
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dData\":" + str  + "}");
			
				e.printStackTrace();
			} finally {
				System.out.println("关闭连接");
				out.flush();
				out.close();
				DB.closeRs_Con(null, c, conn);
			}
		} else {
			map.put("result", "2");
			Gson gson=new Gson();
			List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
			list.add(map);
			str=gson.toJson(list);
			out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dData\":" + str + "}");
			out.flush();
			out.close();
		}
	
	}
}
