package Online_Manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Select_Online_Manager;

@WebServlet(description = "²éÑ¯,ÐÞ¸Ä", urlPatterns = { "/Del_Wallet_recharge_strategy" })
public class Del_Wallet_recharge_strategy extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String ID = request.getParameter("ID");
		int a= Select_Online_Manager.delwallet_recharge_strategy(GetConnection.getWalletConn(),ID);
	    LoggerUtil.info("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":"+ a + "}");
		out.print("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":"+ a + "}");
		out.flush();
		out.close();
	} 
}
