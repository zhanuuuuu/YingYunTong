package report_forms;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import DB.GetConnection;
import DB.report_table;


@WebServlet(description = "类别销售毛利", urlPatterns = { "/Type_Sales_Gross_margin" })
public class Type_Sales_Gross_margin extends HttpServlet {
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
		
//		String cStoreNo = request.getParameter("cStoreNo");
//		String d1= request.getParameter("d1");
//		String d2= request.getParameter("d2");
//		String GroupNo= request.getParameter("GroupNo");
//		String NoType= request.getParameter("NoType");
//		String GroupNoMax= request.getParameter("GroupNoMax");
		
		String d1= request.getParameter("d1");
		String d2= request.getParameter("d2");
		String cStoreNo= request.getParameter("cStoreNo");
		String GroupNo= request.getParameter("GroupNo");
		String NoType= request.getParameter("NoType");
		String GroupNoMax= request.getParameter("GroupNoMax");
		try {
			JSONArray array = report_table.Select_mao_li_report(GetConnection.getStoreConn(), cStoreNo,d1, d2,GroupNo,NoType,GroupNoMax);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":"+ array.toString() + "}");
			}
			System.out.println(array.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.flush();
		out.close();
	}
}
