package supplier_bidding;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;
import com.google.gson.Gson;

import DB.DB_Supplier_Bidding;
import DB.GetConnection;
import bean.Head_affirm_Order;

@WebServlet(description = "查询商家客户订单内容", urlPatterns = {"/Select_Customer_OrderContent"})
public class Select_Customer_OrderContent extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cSheetNo=request.getParameter("cSheetNo");
			LoggerUtil.info(cSheetNo);
			ArrayList<Head_affirm_Order> list=(ArrayList<Head_affirm_Order>) DB_Supplier_Bidding.Select_Customer_OrderContent(GetConnection.getBiddingConn(),cSheetNo);
			if(list!=null&&list.size()>0){
				 Gson gson=new Gson();
				 String str= gson.toJson(list);
				 out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ str + "}");
				 LoggerUtil.info(str);
			}
			else{
				 out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\""+ "" + "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}