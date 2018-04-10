package report_forms;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import DB.report_table;

public class Report_Servlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// action表示用来区别到底是什么操作
		String action = request.getParameter("action");

		// 发布商品
		if (action.equals("login")) {
			login(request, response);
		} else if (action.equals("all_Store_Sale")) {
			All_StoreSale(request, response);
		}
	}

	// 发布商品
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		HashMap<String, String> map = (HashMap<String, String>) request.getAttribute("user");
		if (map == null) {
			HttpSession session = request.getSession();
			Connection conn = GetConnection.getStoreConn();
			ResultSet rs = null;
			PreparedStatement past = null;
			try {
				String user = request.getParameter("name");
				String pass = request.getParameter("pass");
				past = conn.prepareStatement("select [User],Name,userType,cStoreNo,cStoreName from t_Pass where [User]=? and Pass =?");
				past.setString(1, user);
				past.setString(2, pass);
				LoggerUtil.info(user);
				LoggerUtil.info(pass);
				rs = past.executeQuery();
				Map<String, String> user_map = new HashMap<String, String>();
				if (rs.next()) {
					String User = rs.getString("User");
					String Name = rs.getString("Name");
					String userType = rs.getString("userType");
					String cStoreNo = rs.getString("cStoreNo");
					String cStoreName = rs.getString("cStoreName");
					user_map.put("User", User);
					user_map.put("Name", Name);
					user_map.put("userType", userType);
					user_map.put("cStoreNo", cStoreNo);
					user_map.put("cStoreName", cStoreName);
					session.setAttribute("user", user_map);
					//System.out.print(user_map);
					request.getRequestDispatcher("jsps/main.jsp").forward(request, response);
				} else {
					response.sendRedirect("Login.jsp");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DB.closeRs_Con(rs, past, conn);
			}
		} else {
			response.sendRedirect("Login.jsp");
		}
	}

	// 发布商品
	public void All_StoreSale(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		String date1 = request.getParameter("dDate1");
		String date2 =  request.getParameter("dDate2");
		String storeno = request.getParameter("storeno");
		JSONArray array = report_table.Select_Horizontal_report_table(GetConnection.getStoreConn(), date1, date2, storeno);
		if (array != null && array.length() > 0) {
			
		} else {
			
		}

	}
}
