package Online_Manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DB.GetConnection;
import DB.Select_Online_Manager;

/**
 * Servlet implementation class Select_Yi_Pei_Song
 */
@WebServlet(description = "≤È—Ø“—≈‰ÀÕ", urlPatterns = { "/Select_Yi_Pei_Song" })
public class Select_Yi_Pei_Song extends HttpServlet {
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
			String cStoreNo = request.getParameter("cStoreNo");
			String start=request.getParameter("starttime");
			String end=request.getParameter("endtime");
			String fage=request.getParameter("fage");
			fage="2";
			cStoreNo="000";
			System.out.println(cStoreNo);
			System.out.println(start);
			System.out.println(end);
			String str = Select_Online_Manager.Yi_Pei_Song_select_Order(GetConnection.getonlineConn(), fage, cStoreNo,start,end);
			out.print(str);
		} catch (Exception e) {

			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
