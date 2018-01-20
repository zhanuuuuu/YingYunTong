package Select_Goods;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import DB.GetConnection;
import DB.Head_Shop;

@WebServlet(description = "查询一品多码", urlPatterns = { "/Yi_pin_duo_ma" })
public class Yi_pin_duo_ma extends HttpServlet {
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
		JSONArray array = Head_Shop.select_union_goods(GetConnection.getStoreConn());
		if (array != null && array.length() > 0) {
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString() + "}");
		} else {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":"+ array.toString() + "}");
		}
		System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");

		out.flush();
		out.close();
	}

}
