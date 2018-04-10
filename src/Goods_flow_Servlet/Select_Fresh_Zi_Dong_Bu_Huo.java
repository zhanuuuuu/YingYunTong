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

public class Select_Fresh_Zi_Dong_Bu_Huo extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String Store_array=request.getParameter("Store_array");
		String Sup_array =request.getParameter("cSupNo_array");
		String dDate1 =request.getParameter("dDate1");
		LoggerUtil.info(dDate1);
		LoggerUtil.info( Store_array);
		LoggerUtil.info(Sup_array);
		JSONArray array;
		try {
			JSONArray array1=new JSONArray(Store_array);
			JSONArray array2=new JSONArray(Sup_array);
			array = DBupdate.Select_Fresh_Zi_dong(GetConnection.getStoreConn(),dDate1,array1, array2);

			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ "0" + "}");
			}
			LoggerUtil.info(array.toString());
	
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		out.flush();
		out.close();
	
	}

}
