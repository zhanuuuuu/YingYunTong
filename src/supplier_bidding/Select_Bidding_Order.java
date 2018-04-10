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
import bean.Bidding_Order;


@WebServlet(description = "²éÑ¯¾º¼Ûµ¥", urlPatterns = { "/Select_Bidding_Order" })
public class Select_Bidding_Order extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cSupNo=request.getParameter("cSupNo");
			String dDate=request.getParameter("dDate");
			LoggerUtil.info(cSupNo);
			LoggerUtil.info(dDate);
			ArrayList<Bidding_Order> list=DB_Supplier_Bidding.select_Select_Bidding_Order(GetConnection.getBiddingConn(), cSupNo, dDate);
			if(list!=null&&list.size()>0){
				 Gson gson=new Gson();
				 String str= gson.toJson(list);
				 out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ str + "}");
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
