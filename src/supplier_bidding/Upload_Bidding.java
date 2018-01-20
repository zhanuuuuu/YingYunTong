package supplier_bidding;
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
import Tool.String_Tool;

@WebServlet("/Upload_Bidding")
public class Upload_Bidding extends HttpServlet {
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
		try {
			String data=request.getParameter("data");
			String Head_cStoreNo=request.getParameter("Head_cStoreNo");
			String Head_cStoreName=request.getParameter("Head_cStoreName");
			if(String_Tool.isEmpty(Head_cStoreNo)){
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ 0 + "}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\","+ "\"dDate\":" + "µêÆÌ±àºÅ²»ÄÜÎª¿Õ" + "}");
				return ;
			}
			
			System.out.println(data);
			JSONArray array=new JSONArray(data);
			boolean a=DB_Supplier_Bidding.Upload_Bidding(GetConnection.getBiddingConn(),array,Head_cStoreNo,Head_cStoreName);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ 1+ "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\","+ "\"dDate\":" + 0 + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ 0 + "}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\","+ "\"dDate\":" + 0 + "}");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
