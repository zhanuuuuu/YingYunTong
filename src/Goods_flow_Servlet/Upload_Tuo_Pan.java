package Goods_flow_Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DBupdate;
import DB.GetConnection;

public class Upload_Tuo_Pan extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
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
		LoggerUtil.info(data);
		try {
			JSONArray array=new JSONArray(data);
			LoggerUtil.info(array.toString());
			boolean a = DBupdate.insert_into_Tuo_Pan(GetConnection.getStoreConn(), array);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			} 
			else {
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			}
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\""+ "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}


}
