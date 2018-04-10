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
@WebServlet(description = "Ç®°ü³äÖµ²ßÂÔ", urlPatterns = { "/Wallet_recharge_strategy" })

public class Wallet_recharge_strategy extends HttpServlet {
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
		String Pay_Money = request.getParameter("Pay_Money");
		String excess_Money = request.getParameter("excess_Money");
		String ID=request.getParameter("ID");
		String fage=request.getParameter("fage");
		int a=0;
	    if(fage.equals("1")){
	    	a= Select_Online_Manager.wallet_recharge_strategy(GetConnection.getWalletConn(),Pay_Money,excess_Money);
	    }
	    else{
	    	a= Select_Online_Manager.wallet_updaterecharge_strategy(GetConnection.getWalletConn(),Pay_Money, excess_Money,ID);
	    }
	    LoggerUtil.info("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":"+ a + "}");
		out.print("{\"resultStatus\":\"" + a + "\"," + "\"dDate\":"+ a + "}");
		out.flush();
		out.close();
	} 
}
