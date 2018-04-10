package Select_Goods;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Head_Shop;

public class Box_union_goods extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		JSONArray array = Head_Shop.select_union_goods(GetConnection
				.getStoreConn());
		if (array != null && array.length() > 0) {
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
					+ array.toString() + "}");
		} else {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
					+ array.toString() + "}");
		}
		LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");

		out.flush();
		out.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		JSONArray array = Head_Shop.select_union_goods(GetConnection
				.getStoreConn());
		if (array != null && array.length() > 0) {
			out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
					+ array.toString() + "}");
		} else {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
					+ array.toString() + "}");
		}
		LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");

		out.flush();
		out.close();
	}

}
