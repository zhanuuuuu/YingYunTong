package supplier_bidding_head_shop;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.DB_Supplier_Bidding;
import DB.GetConnection;


@WebServlet(description = "确认竞价单", urlPatterns = { "/Ok_bidding_Order" })

public class Ok_bidding_Order extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String data=request.getParameter("data");
			System.out.println(data);
			JSONArray array=new JSONArray(data);
			boolean a=DB_Supplier_Bidding.Head_Store_Ok_bidding_Order(GetConnection.getBiddingConn(),array);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ 1+ "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ 0 + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\","+ "\"dDate\":" + 0 + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ e.getMessage() + "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
