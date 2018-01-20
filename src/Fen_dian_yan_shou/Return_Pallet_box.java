package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.Fen_dian_Update;
import DB.GetConnection;

public class Return_Pallet_box extends HttpServlet {

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

		String data = request.getParameter("data").replace("\n", "")
				.replace("\r", "");
		System.out.println(data);
		try {
			JSONObject obj = new JSONObject(data);

			String user_no = obj.getString("user_no");
			String user_name = obj.getString("user_name");
			String cStore_No = obj.getString("cStore_No");
			String cStore_Name = obj.getString("cStore_Name");
			String iQty = obj.getString("iQty");
			String cDistDev_Return = obj.getString("cDistDev_Return");

			String TruckLicenseTag = obj.getString("TruckLicenseTag");
			String cDriverNo = obj.getString("cDriverNo");
			String cDriverName = obj.getString("cDriverName");
			String TruckNo = obj.getString("TruckNo");
			int a=Fen_dian_Update.Insert_Return_Pallet(GetConnection.getStoreConn(),
					user_no, user_name, cStore_No, cStore_Name, iQty,
					cDistDev_Return, TruckLicenseTag, cDriverNo, cDriverName,
					TruckNo);
			out.print("{\"resultStatus\":\"" + a + "\"" + "}");
			System.out.println("{\"resultStatus\":\"" + a + "\"" + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
