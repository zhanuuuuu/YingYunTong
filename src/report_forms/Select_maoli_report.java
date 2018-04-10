package report_forms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.GetConnection;
import DB.report_table;

/**
 * Servlet implementation class Select_maoli_report
 */
@WebServlet(description = "查询毛利报表", urlPatterns = { "/Select_maoli_report" })
public class Select_maoli_report extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		//d1=2014-6-22&d2=2017-6-22&GroupNo=&NoType=1&GroupNoMax=0
		String cStoreNo = request.getParameter("cStoreNo");
		String d1= request.getParameter("d1");
		String d2= request.getParameter("d2");
		String GroupNo= request.getParameter("GroupNo");
		String NoType= request.getParameter("NoType");
		String GroupNoMax= request.getParameter("GroupNoMax");
		try {
			JSONArray array = report_table.Select_mao_li_report(GetConnection.getStoreConn(),cStoreNo, d1, d2,GroupNo,NoType,GroupNoMax);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
						+ array.toString().replace(" ", "") + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.flush();
		out.close();
	}

}
