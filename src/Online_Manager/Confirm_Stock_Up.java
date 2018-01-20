package Online_Manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DB.DB;
import DB.GetConnection;

@WebServlet(description = "确认备货", urlPatterns = { "/Confirm_Stock_Up" })
public class Confirm_Stock_Up extends HttpServlet {
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
		String cSheetno = request.getParameter("cSheetno");
		Connection conn = GetConnection.getonlineConn();
		PreparedStatement past = null;
		try {
			past = conn.prepareStatement(" Update  Order_Table  set  Pay_state='4' where cSheetno =?  ");
			past.setString(1, cSheetno);
			int a = past.executeUpdate();
			out.print("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":\"" + a + "\"}");
			System.out.println("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":\"" + a + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(null, past, conn);
		}
		out.flush();
		out.close();
	}
}
