package Yan_Pei_Song_Chu_Ku;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DBYan_Huo_update;
import DB.GetConnection;

@WebServlet(asyncSupported=true,description = "总部提交配送验货单", urlPatterns = {"/Upload_Pei_Song_Yan_Huo_Dan1"})
public class Upload_Pei_Song_Yan_Huo_Dan1 extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
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
		String data = request.getParameter("data");
		LoggerUtil.info("提交的数据"+data);
		try {
			LoggerUtil.info("讲哈");
			JSONObject obj = new JSONObject(data);
			JSONArray goods_array = obj.getJSONArray("goods_array");
			JSONArray pallet_array = obj.getJSONArray("pallet_array");
			JSONArray cha_yi_array=obj.getJSONArray("cha_yi_array");
			JSONArray chu_ku_no_array=obj.getJSONArray("chu_ku_no_array");
			boolean a = DBYan_Huo_update.insert_into_yan_huo_dan(GetConnection.getStoreConn(), goods_array, pallet_array,cha_yi_array,chu_ku_no_array);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"" + "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
