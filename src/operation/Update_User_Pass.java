package operation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Operation_update;

public class Update_User_Pass extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String UserNo = request.getParameter("UserNo");
		String OldPass=request.getParameter("OldPass");
		String NewPass=request.getParameter("NewPass");
		LoggerUtil.info(UserNo+"---"+OldPass+"---"+NewPass);
		try {
			String a = Operation_update.Update_Pass(GetConnection.getStoreConn(),UserNo,OldPass, NewPass);
			out.print("{\"resultStatus\":\"" + a + "\"}");
			LoggerUtil.info("{\"resultStatus\":\"" + a + "\"}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
