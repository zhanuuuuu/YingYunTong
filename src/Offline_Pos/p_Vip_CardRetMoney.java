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


@WebServlet(description = "���ַ���", urlPatterns = { "/p_Vip_CardRetMoney" })
public class p_Vip_CardRetMoney extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		//request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		CallableStatement c = null;
		ResultSet rs = null;
		String cVipNo = request.getParameter("cVipNo");
		String dConsum = request.getParameter("dConsum");
		String cSalesheetNo = request.getParameter("cSalesheetNo");
		String fCardRetMoney = request.getParameter("fCardRetMoney");
		String fScore_join = request.getParameter("fScore_join");
		
		String cOpertorNo= request.getParameter("cOpertorNo");
		
		String cOpername=new String(request.getParameter("cOpername").getBytes("ISO8859-1"), "UTF-8");
		cOpername=java.net.URLDecoder.decode(cOpername,"GBK"); 
		
		String dOperate = request.getParameter("dOperate");
		String cPostion = request.getParameter("cPostion");
		String cStyle=new String(request.getParameter("cStyle").getBytes("ISO8859-1"), "UTF-8");
		cStyle=java.net.URLDecoder.decode(cStyle,"GBK");  
		
		String cDetail=new String(request.getParameter("cDetail").getBytes("ISO8859-1"), "UTF-8");
		cDetail=java.net.URLDecoder.decode(cDetail,"GBK"); 
		String sign = request.getParameter("sign");

		System.out.println(cVipNo);
		System.out.println(dConsum);
		System.out.println(cSalesheetNo); 
		System.out.println(fCardRetMoney); 		
		System.out.print(fScore_join);
	    System.out.println(cOpertorNo);
		System.out.println(cOpername);
		System.out.println(dOperate);
		System.out.println(cPostion);
		System.out.println(cStyle);
		System.out.println(cDetail);
		
		System.out.println(sign);
		HashMap<String, String> map = new HashMap<String, String>();
		String str = "";
		if (MD5key.getMD5Pass(cVipNo + "ware13391810430").equals(sign)) {
			try {
				conn = GetConnection.getStoreConn();
				conn.setAutoCommit(false);
				c = conn.prepareCall("{call p_Vip_CardRetMoney (?,?,?,?,?,?,?,?,?,?,?,?)}");
				c.setString(1, cVipNo);
				c.setString(2, dConsum);
				c.setString(3, cSalesheetNo);
				c.setString(4, fCardRetMoney);
				c.setString(5, fScore_join);
				c.setString(6, cOpertorNo);
				c.setString(7, cOpername);
				c.setString(8, dOperate);
				c.setString(9, cPostion);
				c.setString(10, cStyle);
				c.setString(11, cDetail);
				c.registerOutParameter("iReturn", java.sql.Types.LONGNVARCHAR);
				c.execute();
				int retCode = c.getInt("iReturn");

				map.put("result", "" + retCode);
				Gson gson = new Gson();
				List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				list.add(map);
				str = gson.toJson(list);

				out.print("{\"resultStatus\":\"" + retCode + "\"," + "\"dData\":" + str + "}");
				System.out.println("{\"resultStatus\":\"" + retCode + "\"," + "\"dData\":" + str + "}");
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
				System.out.println("�ر�����");
				DB.closeRs_Con(rs, c, conn);
			}
		} else {
			map.put("result", "-1");
			Gson gson = new Gson();
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			list.add(map);
			str = gson.toJson(list);
			out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dData\":\"" + "ǩ������" + "\"}");
		}
		out.flush();
		out.close();
	}

}
