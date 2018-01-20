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

@WebServlet(description = "门店收银报表", urlPatterns = { "/Men_Dian_Shou_Yin_Bao_Biao" })
public class Men_Dian_Shou_Yin_Bao_Biao extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		String cStoreNo = request.getParameter("cStoreNo");
		String d1= request.getParameter("d1");
		String d2= request.getParameter("d2");
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(cStoreNo);
		try {
			JSONArray array = report_table.Select_mendianshouyin_report(GetConnection.getStoreConn(), d1, d2,cStoreNo);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"
						+ array.toString().replace(" ", "") + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":"
						+ array.toString() + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString().replace(" ", "") + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.flush();
		out.close();
	}

}
