package Yan_Pei_Song_Chu_Ku;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import Tool.iStatus_Change;

public class Recovery_Box extends HttpServlet {


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
		String data=request.getParameter("data");
		LoggerUtil.info(data);
		try {
		/*	托盘：不能使用 -1， 可以使用0，正在使用1，随车返程中2
			配送箱：不能使用 -1， 可以使用0，正在使用1，随车返程中2*/
			
			JSONObject obj=new JSONObject(data);
			String userType=obj.getString("userType");
			String user_no = obj.getString("user_no");
			String user_name = obj.getString("user_name");
			String cStore_No=obj.getString("cStore_No");
			String cStore_Name=obj.getString("cStore_Name");
			String fage=obj.getString("fage");
			int a;
			String iStatus = "1";
			if (userType.equals("仓库管理员")) {
				iStatus = "0";
			}
			 if (userType.equals("资讯室")) {
				iStatus = "0";
			}
			 if (userType.equals("司机")) {
				iStatus = "2";
			}
			if(fage.equals("1")){
				JSONArray pallet_no_array=obj.getJSONArray("box_no_array");
				a = iStatus_Change.Set_Box_not_Use(
						GetConnection.getStoreConn(), pallet_no_array, iStatus, user_no,
						user_name,cStore_No,cStore_Name,userType);
			}
			else{
				String box_no = obj.getString("box_no");
				a = iStatus_Change.Set_Box_not_Use(
						GetConnection.getStoreConn(), box_no, iStatus, user_no,
						user_name,cStore_No,cStore_Name,userType);
				
			}
			if (a >=1) {
				out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"" + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"" + "}");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
