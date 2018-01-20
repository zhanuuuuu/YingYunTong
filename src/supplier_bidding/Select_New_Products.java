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


@WebServlet(description = "²éÑ¯ÐÂÆ·", urlPatterns = { "/Select_New_Products" })
public class Select_New_Products extends HttpServlet {
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
			String cSupNo=request.getParameter("cSupNo");
			System.out.println(cSupNo);
			JSONArray array=DB_Supplier_Bidding.select_New_Products(GetConnection.getBiddingConn(), cSupNo);
			if (array!=null&&array.length()>0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
						+ array.toString() + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
