package Select_Goods;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Head_Shop;


@WebServlet(description = "查询最近销售", urlPatterns = { "/Select_Last_sale" })
public class Select_Last_sale extends HttpServlet {
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
		String cGoodsNo=request.getParameter("cGoodsNo");
		JSONArray array = Head_Shop.select_last_Sale(GetConnection.getStoreConn(), cGoodsNo);
		if (array != null && array.length() > 0) {
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString() + "}");
		} else {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":"+ array.toString() + "}");
		}
		LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");

		out.flush();
		out.close();
	}

}
